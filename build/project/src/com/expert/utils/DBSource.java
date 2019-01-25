package com.expert.utils;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

public class DBSource {

	public static BasicDataSource dataSource = new BasicDataSource();
	
	static {
		dataSource.setDriverClassName("org.sqlite.JDBC");
		dataSource.setUrl("jdbc:sqlite:expertsys.db");
	}
	
	public static DataSource getDatasource() {
		return dataSource;
	}
}
