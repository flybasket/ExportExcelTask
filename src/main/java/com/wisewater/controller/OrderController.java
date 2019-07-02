package com.wisewater.controller;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wisewater.framework.PendingJobPool;
import com.wisewater.framework.vo.TaskResult;
import com.wisewater.vo.ExcelDataToExcelTask;
import com.wisewater.vo.ExcelQueryParam;
import com.wisewater.vo.ExportDataTypeEnum;
import com.wisewater.vo.OrderTask;
import com.wisewater.vo.QueryExcelDataFromTask;

/**
 * 类说明：用户注册的控制器
 */
@Controller
@RequestMapping(produces = "text/html;charset=UTF-8")
public class OrderController {
	private static final String SUCCESS = "suc";
	private static final String FAILUER = "failure";

	@Autowired
	private PendingJobPool pool;
	@Autowired
	private OrderTask orderTask;
	@Autowired
	private ExcelDataToExcelTask dataToExcelTask;
	@Autowired
	private QueryExcelDataFromTask queryExcelDataFromTask;

	@RequestMapping("/index")
	public String userReg() {
		return "order";
	}

	@RequestMapping("/submitOrder")
	@ResponseBody
	public String saveUser(@RequestParam("orderNumber") int orderNumber, @RequestParam("taskName") String taskName,
			HttpServletRequest request) {

		// 订单数量为30000，每次1000条
		int total = 30000;
		int limit = 1000;

		int pageSize = total / limit + total % limit;

		String realPath = request.getServletContext().getRealPath("/assets/excel/");

		String jobName = ExportDataTypeEnum.ExportDataTypeCounter.toString();
		pool.registerJob(jobName, realPath, 1, pageSize, dataToExcelTask, queryExcelDataFromTask, 10);

		for (int i = 0; i < pageSize; i++) {
			ExcelQueryParam param = new ExcelQueryParam(i, limit, ExportDataTypeEnum.ExportDataTypeCounter);
			pool.putQueryDataTask(jobName, param);
		}
		return SUCCESS;
	}

	@RequestMapping(value = "/queryProgess")
	@ResponseBody
	public String queryProgess(@RequestParam("query_taskName") String taskName) {
		try {
			return pool.getTaskProgess(taskName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return taskName + "已执行完成!";
	}

	@RequestMapping(value = "/queryTaskProgress")
	@ResponseBody
	public String queryTaskProgress(@RequestParam("query_taskName") String taskName) {
		boolean status = true;
		try {
			status = pool.isCompleteTask(taskName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status ? "true" : "false";
	}

	@RequestMapping(value = "/queryDetail")
	@ResponseBody
	public String queryDetail(@RequestParam("query_taskName") String taskName) {
		List<TaskResult<String>> taskDetail = null;
		try {
			taskDetail = pool.getTaskDetail(taskName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (taskDetail != null && !taskDetail.isEmpty()) {
			// System.out.println(taskDetail);
			return taskDetail.toString();
		}
		return taskName + "任务已过期!";
	}

	@RequestMapping(value = "/download")
	public void download(@RequestParam("query_taskName") String taskName, HttpServletRequest request,
			HttpServletResponse response) {
		// 第六步，下载excel

		OutputStream out = null;
		try {

			// 1.弹出下载框，并处理中文
			/**
			 * 如果是从jsp页面传过来的话，就要进行中文处理，在这里action里面产生的直接可以用 String filename =
			 * request.getParameter("filename");
			 */
			/**
			 * if (request.getMethod().equalsIgnoreCase("GET")) { filename = new
			 * String(filename.getBytes("iso8859-1"), "utf-8"); }
			 */
			String fileName = taskName + ".xls";
			response.addHeader("content-disposition",
					"attachment;filename=" + java.net.URLEncoder.encode(fileName, "utf-8"));

			// 2.下载
			out = response.getOutputStream();
			String path3 = request.getServletContext().getRealPath("/assets/excel/") + fileName;

			// inputStream：读文件，前提是这个文件必须存在，要不就会报错
			InputStream is = new FileInputStream(path3);

			byte[] b = new byte[4096];
			int size = is.read(b);
			while (size > 0) {
				out.write(b, 0, size);
				size = is.read(b);
			}
			out.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		int total = 31001;
		int limit = 1000;

		int pageSize = total / limit + total % limit;
		System.out.println("pageSize: " + pageSize);
	}

}
