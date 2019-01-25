package com.expert.model;

import java.math.BigDecimal;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ProjectDetails {

	private final StringProperty project_id;
	private final StringProperty project_name;
	private final StringProperty project_time;
	private final IntegerProperty project_num;
	private final StringProperty project_location;
	private final StringProperty thField;
	private String Project_expertsId;
	public ProjectDetails(ProjectDetailsCopy projectDetailsCopy) {
		this.project_id = new SimpleStringProperty(new BigDecimal(projectDetailsCopy.getProject_id()).toString());
		this.project_name = new SimpleStringProperty(projectDetailsCopy.getProject_name());
		this.project_time = new SimpleStringProperty(projectDetailsCopy.getProject_time());
		this.project_num = new SimpleIntegerProperty(projectDetailsCopy.getProject_num());
		this.project_location = new SimpleStringProperty(projectDetailsCopy.getProject_location());
		this.thField = new SimpleStringProperty(projectDetailsCopy.getProject_fields());
		this.Project_expertsId = projectDetailsCopy.getProject_expertsId();
	}
	
	public StringProperty getProject_id() {
		return project_id;
	}
	public StringProperty getProject_name() {
		return project_name;
	}
	public StringProperty getProject_time() {
		return project_time;
	}
	public StringProperty getProject_location() {
		return project_location;
	}
	public StringProperty getThField() {
		return thField;
	}
	public String getProject_expertsId() {
		return Project_expertsId;
	}
	public IntegerProperty getProject_num() {
		return project_num;
	}
	
}
