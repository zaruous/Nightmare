/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.util
 *	작성일   : 2018. 2. 1.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.nightmare.comm.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author KYJ
 *
 */
public class HtmlUtil {

	public static Report newReportInstance() {
		return new Report();
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 2. 2.
	 * @param claszz
	 * @param list
	 * @return 
	 * @return
	 * @throws IOException
	 */
	public static <T> String toHtml(Class<T> claszz, List<T> list) throws IOException {
		Report newReportInstance = newReportInstance();
		toHtml(claszz, list, newReportInstance);
		return newReportInstance.getOutput();
		
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 2. 2.
	 * @param claszz
	 * @param list
	 * @param report
	 * @return
	 * @throws IOException
	 */
	public static <T> void toHtml(Class<T> claszz, List<T> list, Report report) throws IOException {
		Field[] declaredFields = claszz.getDeclaredFields();
//		Report newReportInstance = newReportInstance();

		String[] headers = Stream.of(declaredFields).map(f -> f.getName()).toArray(String[]::new);
		report.th(headers);

		for (T v : list) {
			String[] array = Stream.of(declaredFields).map(f -> {
				Object value = null;
				try {
					if (!f.canAccess(v))
						f.setAccessible(true);
					value = f.get(v);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (value == null)
					return "";
				return value;
			}).toArray(String[]::new);
			report.td(array);
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		report.write(out);
//		return out.toString("utf-8");
	}


	public static class Report {

		public Report() {

		}

		protected String startHtml() {
			return "<html>";
		}

		protected String endHtml() {
			return "</html>";
		}

		protected String startHead() {
			return "<head>";
		}

		protected String headBody() {
			return "<meta charset=\"UTF-8\">";
		}

		protected String endHead() {
			return "</head>";
		}

		protected String startBody() {
			return "<body>";
		}

		protected String endBody() {
			return "</body>";
		}

		protected String startTable() {
			return "<table border='" + tbBorder() + "'>";
		}

		protected String endTable() {
			return "</table>";
		}

		protected int tbBorder() {
			return 1;
		}

		private String output;
		public void write(OutputStream out) throws IOException {

			StringBuffer sb = new StringBuffer();

			sb.append(startHtml());

			sb.append(startHead());

			sb.append(headBody());

			sb.append(endHead());

			sb.append(startBody());

			sb.append(startTable());

			sb.append(tableBody.toString());

			sb.append(endTable());

			sb.append(endBody());

			sb.append(endHtml());
			output = sb.toString();
			out.write(output.getBytes("utf-8"));
		}
		

		public final String getOutput() {
			return output;
		}

		StringBuffer tableBody = new StringBuffer();

		public String th(String... args) {

			for (String str : args) {
				tableBody.append("<th>").append(str).append("</th>");
			}
			return tableBody.toString();
		}

		public String td(String... args) {

			tableBody.append("<tr>");
			for (String str : args) {
				tableBody.append("<td>").append(str).append("</td>");
			}
			tableBody.append("</tr>");
			return tableBody.toString();
		}
	}
}
