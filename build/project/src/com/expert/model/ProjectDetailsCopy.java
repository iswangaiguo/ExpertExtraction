package com.expert.model;

public class ProjectDetailsCopy {

	private Double project_id;
	
	private String project_name;
	
	private String project_time;
	
	private int project_num;
	
	private String project_location;
	
	private String Project_expertsId;

	private String Project_fields;
	
	public ProjectDetailsCopy() {
	}
	
	public ProjectDetailsCopy(Double project_id, String project_name, String project_time, int project_num,
			String project_location, String project_expertsId) {
		this.project_id = project_id;
		this.project_name = project_name;
		this.project_time = project_time;
		this.project_num = project_num;
		this.project_location = project_location;
		Project_expertsId = project_expertsId;
	}

	public Double getProject_id() {
		return project_id;
	}

	public void setProject_id(Double project_id) {
		this.project_id = project_id;
	}
	
	public String getProject_name() {
		return project_name;
	}

	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}

	public String getProject_time() {
		return project_time;
	}

	public void setProject_time(String project_time) {
		this.project_time = project_time;
	}

	public String getProject_location() {
		return project_location;
	}

	public void setProject_location(String project_location) {
		this.project_location = project_location;
	}

	public String getProject_expertsId() {
		return Project_expertsId;
	}

	public void setProject_expertsId(String project_expertsId) {
		Project_expertsId = project_expertsId;
	}

	public int getProject_num() {
		return project_num;
	}

	public void setProject_num(int project_num) {
		this.project_num = project_num;
	}

	public String getProject_fields() {
		return Project_fields;
	}

	public void setProject_fields(String project_fields) {
		Project_fields = project_fields;
	}
	


}
