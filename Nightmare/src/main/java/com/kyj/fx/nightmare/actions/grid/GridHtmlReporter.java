/**
 * 
 */
package com.kyj.fx.nightmare.actions.grid;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.kyj.fx.nightmare.actions.ai.DefaultLabel;
import com.kyj.fx.nightmare.comm.report.HtmlUtil;
import com.kyj.fx.nightmare.comm.report.HtmlUtil.Report;

/**
 * 
 */
public class GridHtmlReporter {

	public void generate(List<DefaultLabel> list, OutputStream out) throws IOException {
		Report newReportInstance = new HtmlUtil.Report() {

			@Override
			protected String startBody() {
				String startBody = super.startBody();
				
				var sb = new StringBuilder();
				sb.append(startBody);
				
				
				for (DefaultLabel lbl : list) {
					String text = lbl.getText();
					if ("me".equals(lbl.getTip())) {
						sb.append("<h2>");
						sb.append(text);
						sb.append("</h2>");
						continue;
					}
					
					if(text.isBlank())
					{
						sb.append("<br/>");
					}
					else 
					{
						String[] split = text.split("\n");
						for(String row : split)
						{
							sb.append("<div>");
							sb.append(row);
							sb.append("</div>");	
						}	
					}
					
					
				}
				
				
				
				return sb.toString();
			}

			@Override
			public void write(OutputStream out1) throws IOException {
				super.write(out);
			}

		};

		newReportInstance.write(out);

//		HtmlUtil.toHtml(DefaultLabel.class, list, newReportInstance);
//		out.write(html.getBytes("utf-8"));
	}
}
