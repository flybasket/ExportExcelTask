package com.wisewater.vo;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.wisewater.framework.vo.ITaskProcesser;
import com.wisewater.framework.vo.TaskResult;
import com.wisewater.framework.vo.TaskResultType;
import com.wisewater.tool.FileUtil;

@Component
public class QueryExcelDataFromTask implements ITaskProcesser<ExcelQueryParam, List<ExcelDataRow>> {

	public TaskResult<List<ExcelDataRow>> taskExecute(ExcelQueryParam data) {

		// TODO 测试环境，正式需要根据索引值和页码从数据库中获取数据
		List<ExcelDataRow> rows = new ArrayList<ExcelDataRow>();
		byte[] buf = FileUtil.convertFileToByteArrayWithFilePath("E://test.jpg");
		for (int i = 0; i < data.getSize(); i++) {

			ExcelDataRow dataRow = new ExcelDataRow("032016100574", "10325", "10000", "珠海市/清华科技园", "12345",
					"2019/1/1 15:00:00", "一天", "赵缓", "珠海市香洲区清华科技园", "13566668888", buf);
			rows.add(dataRow);
		}
		if (buf != null)
			buf = null; // help GC
		return new TaskResult<List<ExcelDataRow>>(TaskResultType.Success, rows);

	}

}
