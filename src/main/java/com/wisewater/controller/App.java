package com.wisewater.controller;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.wisewater.vo.ExcelDataRow;
import com.wisewater.vo.ExcelDataTable;
import com.wisewater.vo.ExportExcel;

public class App {

	public static void main(String[] args) {

		long start = System.currentTimeMillis();

		List<String> titles = Arrays.asList("设备编号", "设备名", "户号", "地区", "通讯卡号", "安装时间", "抄表频率", "户名", "地址", "联系方式", "抄表行码");

		List<ExcelDataRow> dataRows = new ArrayList<ExcelDataRow>();
		byte[] buf = null;
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream("E://test.jpg"));
			buf = new byte[bis.available()];
			while ((bis.read(buf)) != -1) {
				//
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				bis = null;
			}
		}

		for (int i = 0; i < 55000; i++) {

			ExcelDataRow dataRow = new ExcelDataRow("032016100574", "10325", "10000", "珠海市/清华科技园", "12345",
					"2019/1/1 15:00:00", "一天", "赵缓", "珠海市香洲区清华科技园", "13566668888", buf);
			dataRows.add(dataRow);
		}

		ExcelDataTable<ExcelDataRow> dataTable = new ExcelDataTable<ExcelDataRow>();
		dataTable.setTitles(titles);
		dataTable.setDataRows(dataRows);

		ExportExcel<ExcelDataRow> ex2 = new ExportExcel<ExcelDataRow>();
		try {

			// dataset2.add(new Book(1, "jsp", "leno", 300.33f, "1234567", "清华出版社", buf));
			// dataset2.add(new Book(2, "java编程思想", "brucl", 300.33f, "1234567", "阳光出版社",
			// buf));
			// dataset2.add(new Book(3, "DOM艺术", "lenotang", 300.33f, "1234567", "清华出版社",
			// buf));
			// dataset2.add(new Book(4, "c++经典", "leno", 400.33f, "1234567", "清华出版社", buf));
			// dataset2.add(new Book(5, "c#入门", "leno", 300.33f, "1234567", "汤春秀出版社", buf));

			// OutputStream out = new FileOutputStream("E://a.xls");
			OutputStream out2 = new FileOutputStream("E://b.xls");
			// ex.exportExcel(headers, dataset, out);
			ex2.exportExcel((String[]) titles.toArray(), (Collection<ExcelDataRow>) dataRows, out2);
			// out.close();
			out2.close();
			// JOptionPane.showMessageDialog(null, "导出成功!");
			System.out.println("excel导出成功！");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();
		System.out.println("耗时: " + (end - start) + "ms");

	}
}
