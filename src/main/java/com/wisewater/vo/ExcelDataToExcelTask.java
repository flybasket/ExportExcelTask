package com.wisewater.vo;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Component;

import com.wisewater.framework.vo.ITaskProcesser;
import com.wisewater.framework.vo.QueryDataItemVo;
import com.wisewater.framework.vo.TaskResult;
import com.wisewater.framework.vo.TaskResultType;

/**
 * 类说明：一个实际任务类，将数值加上一个随机数，并休眠随机时间
 */
@Component
public class ExcelDataToExcelTask
		implements ITaskProcesser<QueryDataItemVo<List<String>, List<ExcelDataRow>>, Integer> {

	public TaskResult<Integer> taskExecute(QueryDataItemVo<List<String>, List<ExcelDataRow>> data) {

		ExportExcel<ExcelDataRow> ex2 = new ExportExcel<ExcelDataRow>();
		TaskResultType reason = TaskResultType.Exception;
		String errMsg = "导出成功!";
		OutputStream out2 = null;
		try {

			String type = data.getType().get(0);
			String realPath = data.getType().get(1);

			ExportDataTypeEnum exportDataTypeEnum = ExportDataTypeEnum.valueOf(type);

			String filePath = realPath + exportDataTypeEnum.excelName() + ".xls";
			out2 = new FileOutputStream(filePath);
			ex2.exportExcel(exportDataTypeEnum.excelHeaders(), (Collection<ExcelDataRow>) data.getData(), out2);

			reason = TaskResultType.Success;
		} catch (NullPointerException e) {
			e.printStackTrace();
			errMsg = "导出失败!";
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			errMsg = "导出失败!";
		} catch (FileNotFoundException e) {

			e.printStackTrace();
			errMsg = "文件未找到!";
		} finally {
			if (out2 != null) {
				try {
					out2.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				out2 = null; // help GC
			}
			ex2 = null;
		}
		System.out.println(errMsg);
		return new TaskResult<Integer>(reason, 1, errMsg);
	}

}
