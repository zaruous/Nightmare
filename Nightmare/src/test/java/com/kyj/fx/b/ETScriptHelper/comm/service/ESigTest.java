/********************************
 *	프로젝트 : ETHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper.comm.service
 *	작성일   : 2021. 11. 23.
 *	작성자   : KYJ (zaruous@naver.com)
 *******************************/
package com.kyj.fx.b.ETScriptHelper.comm.service;

import java.io.File;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import org.dom4j.Document;
import org.dom4j.Node;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.comm.DbUtil;
import com.kyj.fx.nightmare.comm.FileUtil;
import com.kyj.fx.nightmare.comm.ValueUtil;
import com.kyj.fx.nightmare.comm.service.ESig;
import com.kyj.fx.nightmare.comm.service.XMLUtils;

/**
 * @author KYJ (zaruous@naver.com)
 *
 */
class ESigTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(ESigTest.class);

	@Test
	public void xmlTest() throws Exception {
		String str = "<Person name='kim'/>";
		Document load = XMLUtils.load(str);
		Node selectSingleNode = load.selectSingleNode("//Person/@name");
		System.out.println(selectSingleNode.getText());
	}

	@Test
	public void ttest() throws SQLException, Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		sb.append("\n");
		sb.append("select * from DMI_DRE.dbo.DRE_RENDERING\n");
		sb.append("where 1=1 \n");
		sb.append("and id  in (\n");
		sb.append("\n");
		sb.append("select\n");
		sb.append("RENDERINGID AS RENDERINGID\n");
		sb.append("from DMI_DRE.dbo.DRE_VERSIONDETAIL \n");
		sb.append("where 1=1\n");
		sb.append("and versionid = ( select top 1 id from DMI_DRE.dbo.DRE_VERSION\n");
		sb.append("where 1=1 \n");
		sb.append("and name = 'MR_COM_DISPENSING_KG'\n");
		sb.append("and status = 2\n");
		sb.append("order by versionnumber desc)\n");
		sb.append(") \n");

		DbUtil.select(DbUtil.getConnection(), sb.toString(), 2, 2,
				new BiFunction<ResultSetMetaData, ResultSet, List<Map<String, String>>>() {

					@Override
					public List<Map<String, String>> apply(ResultSetMetaData t, ResultSet u) {
						var m = new HashMap<String, String>();
						try {
							int i = 0;
							while (u.next()) {

								String n = u.getString("FILEPATHFROM");
								String name = new File(n).getName();

								try (InputStream binaryStream = u.getBinaryStream("BLOB")) {

									byte[] b = ValueUtil.toByte(binaryStream);
									LOGGER.debug("{}", new String(b, "utf-8"));
									FileUtil.writeFile(name, binaryStream);

								} catch (Exception e) {
									e.printStackTrace();
								}

								// new ZipInputStream(null)

								// try (BufferedReader reader = new
								// BufferedReader(u.getNCharacterStream("BLOB")))
								// {
								// String temp = null;
								//
								// while( (temp = reader.readLine())!=null) {
								// LOGGER.debug(temp);
								// }
								//
								// } catch (Exception e) {
								// e.printStackTrace();
								// }

							}
						} catch (SQLException e) {
							e.printStackTrace();
						}

						return Arrays.asList(m);
					}
				});

	}

}
