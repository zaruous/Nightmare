/********************************
 *	프로젝트 : ETScriptHelper
 *	패키지   : com.kyj.fx.b.ETScriptHelper
 *	작성일   : 2021. 7. 7.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.nightmare;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.nightmare.actions.ec.ec.scripts.EquipmentScriptDVO;
import com.kyj.fx.nightmare.comm.DbUtil;
import com.kyj.fx.nightmare.comm.ValueUtil;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class DbUpdate {
	
	/**
	 * @최초생성일 2021. 11. 19.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DbUpdate.class);
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 7. 7.
	 * @param updateItems
	 * @throws Exception
	 */
	public static void updateScript(List<EquipmentScriptDVO> updateItems) throws Exception {
		
		List<EquipmentScriptDVO> insert = new ArrayList<>();
		List<EquipmentScriptDVO> update = new ArrayList<>();
		for (EquipmentScriptDVO item : updateItems) {
			
			if (ValueUtil.isNotEmpty(item.get_status())) {
				if (item.getEventScriptGUID() == null) {
					insert.add(item);
				} else
					update.add(item);
			}
		}
		Connection con = null;
		try {
			con = DbUtil.getConnection();
			con.setAutoCommit(false);

			if (update.size() > 0) {
				int updateScripts = updateScripts(con, update);
				LOGGER.debug("update count : {} " , updateScripts);
			}
			if (insert.size() > 0) {
				int insertScripts = insertScripts(con, insert);
				LOGGER.debug("insert count : {} " , insertScripts);
			}
			con.commit();
		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			con.close();
		}
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 7. 16.
	 * @param con
	 * @param update
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws SQLException
	 */
	static int updateScripts(Connection con, List<EquipmentScriptDVO> update) throws UnsupportedEncodingException, SQLException {
		int r = -1;
		String sql = "update DMI_ET.dbo.ET_EventScripts  set CODE = ? " + " where 1=1 " + "and SCRIPTGUID=? " + "and EVENTGUID=?";
		try (PreparedStatement prepareStatement = con.prepareStatement(sql)) {
			for (EquipmentScriptDVO item : update) {
				if (item.getEventScriptGUID() == null || item.getEventGuid() == null)
					throw new RuntimeException("Event Script GUID or Event GUID empty.");
				prepareStatement.setObject(1, item.getCode().getBytes("UTF-16LE"), Types.BLOB);
				prepareStatement.setString(2, item.getScriptGuid());
				prepareStatement.setString(3, item.getEventGuid());
				r += prepareStatement.executeUpdate();
			}
		}
		return r;
	}

	public static int deleteScript(Connection con , List<EquipmentScriptDVO> deleteItems) throws Exception {
		int r = -1;
		String sql = "delete DMI_ET.dbo.ET_EventScripts  where 1=1  and SCRIPTGUID=?  and EVENTGUID=?";

		try (PreparedStatement prepareStatement = con.prepareStatement(sql)) {
			for (EquipmentScriptDVO item : deleteItems) {
				prepareStatement.setString(1, item.getScriptGuid());
				prepareStatement.setString(2,  item.getEventGuid());
				r += prepareStatement.executeUpdate();
			}
		}

		return r;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2021. 7. 19.
	 * @param con
	 * @param update
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws SQLException
	 */
	static int insertScripts(Connection con, List<EquipmentScriptDVO> update) throws UnsupportedEncodingException, SQLException {
		int r = -1;
		
		String sql = "insert into DMI_ET.dbo.ET_EventScripts ( EventScriptGUID ,ScriptGUID ,EventGUID ,ContinueOnFail ,Email ,Code  ) "
				+ "values ( ? , ? , ? ,'0' ,'' , ?  )";

		try (PreparedStatement prepareStatement = con.prepareStatement(sql)) {
			for (EquipmentScriptDVO item : update) {
				
				prepareStatement.setString(1, UUID.randomUUID().toString().toUpperCase() );
				prepareStatement.setString(2, item.getScriptGuid());
				prepareStatement.setString(3, item.getEventGuid());
				prepareStatement.setObject(4, item.getCode().getBytes("UTF-16LE"), Types.BLOB);
				r += prepareStatement.executeUpdate();
			}
		}
		return r;
	}
}
