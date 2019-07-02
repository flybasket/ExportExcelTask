package com.wisewater.framework.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

import com.wisewater.framework.CheckJobProcesser;

/**
 * 类说明：提交给框架执行的工作实体类,工作：表示本批次需要处理的同性质任务(Task)的一个集合
 */
public class JobInfo<R> {
	// 工作名，用以区分框架中唯一的工作，
	private final String jobName;
	private final int jobLength;// 工作中任务的列表
	private final ITaskProcesser<?, ?> taskProcesser;// 处理工作中任务的处理器

	private AtomicInteger successCount;// 任务的成功次数
	private AtomicInteger taskProcessCount;// 工作中任务目前已经处理的次数

	// 存放每个任务的处理结果，供查询用,why?
	private LinkedBlockingDeque<TaskResult<R>> taskDetailQueus;
	private final long expireTime;// 保留的工作的结果信息供查询的时间
	// 检查过期工作的处理器
	private static CheckJobProcesser checkJob = CheckJobProcesser.getInstance();

	private final int queryDataJobLength; // 工作中获取数据的列表
	private final ITaskProcesser<?, ?> queryDataTaskProcesser; // 处理工作中获取数据的处理器
	private AtomicInteger queryDataTaskProcessCount;// 工作中获取数据的处理次数（忽略失败的）
	private List<R> queryData = new ArrayList<R>();
	private static Object queryDataAddDeleteSafe = new Object();
	private final String extendJobInfo;

	public JobInfo(String jobName, int jobLength, ITaskProcesser<?, ?> taskProcesser, long expireTime) {
		this.jobName = jobName;
		this.jobLength = jobLength;
		this.queryDataJobLength = 0;
		successCount = new AtomicInteger(0);
		taskProcessCount = new AtomicInteger(0);
		this.taskProcesser = taskProcesser;
		this.queryDataTaskProcesser = null;
		this.extendJobInfo = null;
		taskDetailQueus = new LinkedBlockingDeque<TaskResult<R>>(jobLength);
		this.expireTime = expireTime;
	}

	public JobInfo(String jobName, String extendJobInfo, int jobLength, int queryDataJobLength,
			ITaskProcesser<?, ?> taskProcesser, ITaskProcesser<?, ?> queryDataTaskProcesser, long expireTime) {
		this.jobName = jobName;
		this.extendJobInfo = extendJobInfo;
		this.jobLength = jobLength;
		this.queryDataJobLength = queryDataJobLength;
		this.successCount = new AtomicInteger(0);
		this.taskProcessCount = new AtomicInteger(0);
		queryDataTaskProcessCount = new AtomicInteger(0);
		this.taskProcesser = taskProcesser;
		this.queryDataTaskProcesser = queryDataTaskProcesser;
		taskDetailQueus = new LinkedBlockingDeque<TaskResult<R>>(jobLength);
		this.expireTime = expireTime;
	}

	public int getSuccCount() {
		return successCount.get();
	}

	public List<R> getQueryDataList() {
		return new ArrayList<R>(queryData);
	}

	public int getTaskProcessCount() {
		return taskProcessCount.get();
	}

	public String getJobName() {
		return jobName;
	}

	public String getExtendJobInfo() {
		return extendJobInfo;
	}

	// 提供工作中失败的次数
	public int getFailCount() {
		return taskProcessCount.get() - successCount.get();
	}

	public ITaskProcesser<?, ?> getTaskProcesser() {
		return taskProcesser;
	}

	public ITaskProcesser<?, ?> getQueryDataTaskProcesser() {
		return queryDataTaskProcesser;
	}

	// 提供工作的整体进度信息
	public String getTotalProcess() {
		return "Success[" + successCount.get() + "]/Current[" + taskProcessCount.get() + "] Total[" + jobLength + "]";
	}

	public int getJobLength() {
		return jobLength;
	}

	public boolean isComplete() {
		return taskProcessCount.get() == jobLength;
	}

	// 提供工作中每个任务的处理结果
	public List<TaskResult<R>> getTaskDetail() {
		List<TaskResult<R>> taskDetailList = new LinkedList<TaskResult<R>>();
		TaskResult<R> taskResult;
		while ((taskResult = taskDetailQueus.pollFirst()) != null) {
			taskDetailList.add(taskResult);
		}
		return taskDetailList;
	}

	// 每个任务处理完成后，记录任务的处理结果，因为从业务应用的角度来说，
	// 对查询任务进度数据的一致性要不高
	// 我们保证最终一致性即可，无需对整个方法加锁
	public void addTaskResult(TaskResult<R> result) {
		if (result != null) {
			if (TaskResultType.Success.equals(result.getResultType())) {
				successCount.incrementAndGet();
			} else {
				taskDetailQueus.addLast(result);
			}
		}

		taskProcessCount.incrementAndGet();
		// 工作的所有任务都处理完成后，提供一定的时间供查询结果，
		// 所以将工作推入定时缓存，到期将工作从框架中清除
		if (taskProcessCount.get() == jobLength) {
			checkJob.putJob(jobName, expireTime);
		}
	}

	// 每个任务处理完成后，记录任务的处理结果，因为从业务应用的角度来说，
	// 对查询任务进度数据的一致性要不高
	// 我们保证最终一致性即可，无需对整个方法加锁
	public boolean addQueryDataTaskResultAndReturnCompleteState(TaskResult<R> result) {
		synchronized (queryDataAddDeleteSafe) {
			// 收集获取到的数据
			queryData.addAll((Collection<? extends R>) result.getReturnValue());
		}

		// 累计查询任务的数量
		queryDataTaskProcessCount.incrementAndGet();

		// 工作的所有任务都处理完成后，提供一定的时间供查询结果，
		// 所以将工作推入定时缓存，到期将工作从框架中清除
		return queryDataTaskProcessCount.get() == queryDataJobLength;
	}

	public void clearQueryData() {
		synchronized (queryDataAddDeleteSafe) {
			// 清除数据
			queryData.clear();
		}

	}

}
