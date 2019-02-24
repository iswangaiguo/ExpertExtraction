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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ProjectHistoryController {

	@FXML
	private TableView<ProjectDetails> projectTable;

	@FXML
	private TableColumn<ProjectDetails, String> projectIdColumn;

	@FXML
	private TableColumn<ProjectDetails, String> projectNameColumn;

	@FXML
	private TableColumn<ProjectDetails, String> projectTimeColumn;

	@FXML
	private TableColumn<ProjectDetails, Number> projectNumColumn;

	@FXML
	private TableColumn<ProjectDetails, String> projectLocationColumn;
	
	@FXML
	private TableColumn<ProjectDetails, String> projectFieldsColumn;

	@FXML
	private TableView<Expert> expertTable;

	@FXML
	private TableColumn<Expert, String> thIdColumn;

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
	
	private QueryRunner queryRunner = new QueryRunner(DBSource.getDatasource());

	private ObservableList<ProjectDetails> projectDetailsList;
	
	private ObservableList<Expert> Selectedexpert = FXCollections.observableArrayList();
	
	@FXML
	private void initialize() {
	}

	public void setData(MainLayoutController controller) {
		try {
			String sql = "select * from projects";
			projectDetailsList = controller.getProjectDetailsList();
			projectDetailsList.clear();
			List<ProjectDetailsCopy> list = queryRunner.query(sql, new BeanListHandler<ProjectDetailsCopy>(ProjectDetailsCopy.class));
			for (ProjectDetailsCopy projectDetailsCopy : list) {
				projectDetailsList.add(new ProjectDetails(projectDetailsCopy));
			}
			projectDetailsList.sort((pd1, pd2) -> pd2.getProject_id().get().compareTo(pd1.getProject_id().get()));
			showDetails();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void showDetails() {
		projectTable.setItems(projectDetailsList);
		projectIdColumn.setCellValueFactory(cell -> cell.getValue().getProject_id());
		projectNameColumn.setCellValueFactory(cell -> cell.getValue().getProject_name());
		projectTimeColumn.setCellValueFactory(cell -> cell.getValue().getProject_time());
		projectNumColumn.setCellValueFactory(cell -> cell.getValue().getProject_num());
		projectLocationColumn.setCellValueFactory(cell -> cell.getValue().getProject_location());
		projectFieldsColumn.setCellValueFactory(cell -> cell.getValue().getThField());
		
		projectTable.getSelectionModel().selectedItemProperty()
			.addListener((observable, oldValue, newValue) -> showSelectedExpert(newValue));
		
	}

	private void showSelectedExpert(ProjectDetails newValue) {
		String sql = "select * from expert where th_id in (" + newValue.getProject_expertsId() + ")";
		try {
			List<ExpertCopy> list = queryRunner.query(sql, new BeanListHandler<ExpertCopy>(ExpertCopy.class));
			Selectedexpert.clear();
			for (ExpertCopy expertCopy : list) {
				Selectedexpert.add(new Expert(expertCopy));
			}
			expertTable.setItems(Selectedexpert);
			thIdColumn.setCellValueFactory(cellData -> cellData.getValue().thIdProperty());
			thNameColumn.setCellValueFactory(cellData -> cellData.getValue().thNameProperty());
			thSexColumn.setCellValueFactory(cellData -> cellData.getValue().thSexProperty());
			thAgeColumn.setCellValueFactory(cellData -> cellData.getValue().thAgeProperty());
			thFieldColumn.setCellValueFactory(cellData -> cellData.getValue().thFieldProperty());
			thProfessionalTitleColumn.setCellValueFactory(cellData -> cellData.getValue().thProfessionalTitleProperty());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
}
