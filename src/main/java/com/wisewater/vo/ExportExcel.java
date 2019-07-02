package com.wisewater.vo;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExportExcel<T> {

	public void exportExcel(Collection<T> dataset, OutputStream out) {
		exportExcel("测试POI导出EXCEL文档", null, dataset, out, "yyyy-MM-dd", 10000);
	}

	public void exportExcel(String[] headers, Collection<T> dataset, OutputStream out) {
		exportExcel("测试POI导出EXCEL文档", headers, dataset, out, "yyyy-MM-dd", 10000);
	}

	public void exportExcel(String[] headers, Collection<T> dataset, OutputStream out, String pattern) {
		exportExcel("测试POI导出EXCEL文档", headers, dataset, out, pattern, 10000);
	}

	@SuppressWarnings("unchecked")
	public void exportExcel(String title, String[] headers, Collection<T> dataset, OutputStream out, String pattern,
			int limitCount) {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();

		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();

		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 12);

		// 把字体应用到当前的样式
		style.setFont(font);

		// 组织数据，并导出
		Collection<Collection<T>> split = split(dataset, limitCount);
		int i = 0;
		for (Collection<T> subDataset : split) {
			i++;
			String appendTitle = (i - 1) * 10000 + "~" + i * 10000;
			exportDataToWorkBook(workbook, style, font, headers, subDataset, pattern, title + appendTitle);
		}

		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				workbook = null; // help GC
			}

		}
	}

	private void exportDataToWorkBook(HSSFWorkbook workbook, HSSFCellStyle style, HSSFFont font, String[] headers,
			Collection<T> subDataset, String pattern, String title) {

		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth((short) 15);

		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (short i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);

			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		// 遍历集合数据，产生数据行
		Iterator<T> it = subDataset.iterator();
		int index = 0;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			T t = (T) it.next();
			// 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
			Field[] fields = t.getClass().getDeclaredFields();
			for (short i = 0; i < fields.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(style);
				Field field = fields[i];
				String fieldName = field.getName();
				String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				try {
					Class tCls = t.getClass();
					Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
					Object value = getMethod.invoke(t, new Object[] {});
					if (value instanceof byte[]) {
						// 利用HSSFPatriarch将图片写入EXCEL
						HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

						short columnIndex = (short) cell.getColumnIndex();
						int rowIndex = cell.getRowIndex();

						// 图片一导出到单元格B2中
						HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, columnIndex, rowIndex,
								(short) (columnIndex + 1), rowIndex + 1);

						// 插入图片
						patriarch.createPicture(anchor,
								workbook.addPicture((byte[]) value, HSSFWorkbook.PICTURE_TYPE_JPEG));
					} else {
						// 判断值的类型后进行强制类型转换
						String textValue = null;
						if (value instanceof Integer) {
							int intValue = (Integer) value;
							cell.setCellValue(intValue);
						} else if (value instanceof Long) {
							long longValue = (Long) value;
							cell.setCellValue(longValue);
						} else if (value instanceof Boolean) {
							boolean bValue = (Boolean) value;
							textValue = "1";
							if (!bValue) {
								textValue = "0";
							}
						} else if (value instanceof Date) {
							Date date = (Date) value;
							SimpleDateFormat sdf = new SimpleDateFormat(pattern);
							textValue = sdf.format(date);
						} else if (value instanceof byte[]) {

						} else {

							// 其它数据类型都当作字符串简单处理
							if (value == null) {
								textValue = "";
							} else {
								textValue = value.toString();
							}

						}

						if (textValue != null) {
							Pattern p = Pattern.compile("^//d+(//.//d+)?$");
							Matcher matcher = p.matcher(textValue);
							if (matcher.matches()) {
								// 是数字当作double处理
								cell.setCellValue(Double.parseDouble(textValue));
							} else {
								HSSFRichTextString richString = new HSSFRichTextString(textValue);
								richString.applyFont(font);
								cell.setCellValue(richString);
							}
						}
					}

				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} finally {
					// 清理资源
				}
			}

		}
	}

	/**
	 * 拆分集合
	 * 
	 * @param <T>
	 * @param resList
	 *            要拆分的集合
	 * @param count
	 *            每个集合的元素个数
	 * @return 返回拆分后的各个集合
	 */
	private <T> Collection<Collection<T>> split(Collection<T> resList, int count) {

		if (resList == null || count < 1)
			return null;
		Collection<Collection<T>> ret = new ArrayList<Collection<T>>();
		int size = resList.size();
		if (size <= count) { // 数据量不足count指定的大小
			ret.add(resList);
		} else {
			int pre = size / count;
			int last = size % count;
			// 前面pre个集合，每个大小都是count个元素
			for (int i = 0; i < pre; i++) {
				Collection<T> itemList = new ArrayList<T>();
				for (int j = 0; j < count; j++) {
					// itemList.add(((ArrayList<Collection<T>>) resList).get(i * count + j));
					itemList.add((T) resList.toArray()[i * count + j]);
				}
				ret.add(itemList);
			}
			// last的进行处理
			if (last > 0) {
				Collection<T> itemList = new ArrayList<T>();
				for (int i = 0; i < last; i++) {
					itemList.add((T) resList.toArray()[pre * count + i]);
				}
				ret.add(itemList);
			}
		}
		return ret;

	}

}
