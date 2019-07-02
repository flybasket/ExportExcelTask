package com.wisewater.vo;

import org.springframework.stereotype.Component;

import com.wisewater.framework.vo.ITaskProcesser;
import com.wisewater.framework.vo.TaskResult;
import com.wisewater.framework.vo.TaskResultType;

import java.util.Random;

/**
 * 类说明：一个实际任务类，将数值加上一个随机数，并休眠随机时间
 */
@Component
public class OrderTask implements ITaskProcesser<Integer, Integer> {
	public final static String JOB_NAME = "订单处理";

	public TaskResult<Integer> taskExecute(Integer data) {
		Random r = new Random();
		int flag = r.nextInt(500);
		SleepTools.ms(flag);
		if (flag <= 300) {// 正常处理的情况
			Integer returnValue = data.intValue() + flag;
			return new TaskResult<Integer>(TaskResultType.Success, returnValue);
		} else if (flag > 301 && flag <= 400) {// 处理失败的情况
			return new TaskResult<Integer>(TaskResultType.Failure, -1, "Failure");
		} else {// 发生异常的情况
			try {
				throw new RuntimeException("异常发生了！！");
			} catch (Exception e) {
				return new TaskResult<Integer>(TaskResultType.Exception, -1, e.getMessage());
			}
		}
	}

}
