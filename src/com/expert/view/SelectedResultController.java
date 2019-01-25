package com.expert.view;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.expert.model.Expert;
import com.expert.model.ExpertCopy;
import com.expert.model.ProjectDetails;
import com.expert.model.ProjectDetailsCopy;
import com.expert.utils.DBSource;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class SelectedResultController {

	@FXML
	private TableView<Expert> expertTable;

	@FXML
	private TableColumn<Expert, Number> thIdColumn;

	@FXML
	private TableColumn<Expert, String> thNameColumn;

	@FXML
	private TableColumn<Expert, String> thSexColumn;

	@FXML
	private TableColumn<Expert, Number> thAgeColumn;

	@FXML
	private TableColumn<Expert, String> thFieldColumn;

	@FXML
	private TableColumn<Expert, String> thProfessionalTitleColumn;

	private ProjectDetailsCopy projectDetailsCopy;

	private QueryRunner queryRunner = new QueryRunner(DBSource.getDatasource());

	private ObservableList<Expert> Selectedexpert = FXCollections.observableArrayList();

	private Stage stage;
	
	private ObservableList<ProjectDetails> projectDetailsList;

	@FXML
	private void initialize() {
		expertTable.setItems(Selectedexpert);
		thIdColumn.setCellValueFactory(cellData -> cellData.getValue().thIdProperty());
		thNameColumn.setCellValueFactory(cellData -> cellData.getValue().thNameProperty());
		thSexColumn.setCellValueFactory(cellData -> cellData.getValue().thSexProperty());
		thAgeColumn.setCellValueFactory(cellData -> cellData.getValue().thAgeProperty());
		thFieldColumn.setCellValueFactory(cellData -> cellData.getValue().thFieldProperty());
		thProfessionalTitleColumn.setCellValueFactory(cellData -> cellData.getValue().thProfessionalTitleProperty());
	}

	public void setData(ProjectDetailsCopy projectDetailsCopy) {
		try {
			
			this.projectDetailsCopy = projectDetailsCopy;
			String sql = "select * from expert where th_id in (" + projectDetailsCopy.getProject_expertsId() + ")";
			List<ExpertCopy> list = queryRunner.query(sql, new BeanListHandler<ExpertCopy>(ExpertCopy.class));
			for (ExpertCopy expertCopy : list) {
				Selectedexpert.add(new Expert(expertCopy));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handleOk() {
		String sql = "insert into projects (project_id, project_name, project_time, project_num,"
				+ "project_location,Project_fields, Project_expertsId) values (?, ?, ?, ?, ?, ?, ?)";
		Object params[] = {projectDetailsCopy.getProject_id(), projectDetailsCopy.getProject_name(), projectDetailsCopy.getProject_time(),
					projectDetailsCopy.getProject_num(), projectDetailsCopy.getProject_location(),projectDetailsCopy.getProject_fields(),
					 projectDetailsCopy.getProject_expertsId()};
		projectDetailsList.add(new ProjectDetails(projectDetailsCopy));
		try {
			queryRunner.update(sql, params);
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setContentText("保存成功");
			alert.showAndWait();
			stage.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handleCancel() {
		stage.close();
	}
	
	public void setDialogStage(Stage stage) {
		this.stage = stage;
	}

	public void setHistoryEvent(ObservableList<ProjectDetails> projectDetailsList) {
		this.projectDetailsList = projectDetailsList;
	}

}
