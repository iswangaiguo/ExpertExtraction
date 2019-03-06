package com.expert.dao;

import static java.util.stream.Collectors.toList;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;

import com.expert.utils.DBSource;

public class CollogeAndMajorDao {

	private QueryRunner queryRunner;
	public CollogeAndMajorDao() {
		super();
		queryRunner = new QueryRunner(DBSource.getDatasource());
	}

	
	public List<String> getColloge() {
		try {
			String sql = "select * from colloge_major_name where length(cm_id) = 2";
			List<Object[]> queryResult = queryRunner.query(sql, new ArrayListHandler());
			return queryResult.stream().map(item -> (String)item[1]).collect(toList());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


	public void addColloge(String thColloge, String thMajor) {
		try {
			String collogeIdSql = "select cm_id from colloge_major_name where cm_name = '" + thColloge + "'";
			Object[] cmIdArr = queryRunner.query(collogeIdSql, new ArrayHandler());
			String collogeId = (String)cmIdArr[0];
				String majorIdSql = "select cm_id from colloge_major_name where cm_id like '" + collogeId + "%' and length(cm_id) > 2";  
				List<Object[]> majorIds = queryRunner.query(majorIdSql, new ArrayListHandler());
				Optional<Integer> lastId = majorIds.stream().map(item -> Integer.parseInt((String) item[0])).max(Integer::compare);
				String newCmId = "";
				if (collogeId.startsWith("0")) {
					String temp = lastId.get().toString();
					newCmId = collogeId + (Integer.parseInt(temp.substring(1, temp.length())) + 1);
				} else {
					String temp = lastId.get().toString();
					newCmId = collogeId + (Integer.parseInt(temp.substring(2, temp.length())) + 1);
				}
			Object[] param = {newCmId, thMajor};
			String insertMajorSql = "insert into colloge_major_name (cm_id ,cm_name) values (?, ?)";
			queryRunner.update(insertMajorSql, param);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
}
