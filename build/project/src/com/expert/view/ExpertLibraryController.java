package com.expert.view;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;

import com.expert.model.Expert;
import com.expert.utils.DBSource;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ExpertLibraryController {

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

	private MainLayoutController controller;
	
	private QueryRunner queryRunner = new QueryRunner(DBSource.getDatasource());

	@FXML
	private void initialize() {
		thIdColumn.setCellValueFactory(cellData -> cellData.getValue().thIdProperty());
		thNameColumn.setCellValueFactory(cellData -> cellData.getValue().thNameProperty());
		thSexColumn.setCellValueFactory(cellData -> cellData.getValue().thSexProperty());
		thAgeColumn.setCellValueFactory(cellData -> cellData.getValue().thAgeProperty());
		thFieldColumn.setCellValueFactory(cellData -> cellData.getValue().thFieldProperty());
		thProfessionalTitleColumn.setCellValueFactory(cellData -> cellData.getValue().thProfessionalTitleProperty());
	}

	@FXML
	private void handleDeletePerson() {
		int selectedIndex = expertTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			try {
				Expert selectedPerson = expertTable.getSelectionModel().getSelectedItem();
				expertTable.getItems().remove(selectedIndex);
				String sql = "delete from expert where th_id = ?";
				queryRunner.update(sql, selectedPerson.getThId());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		} else {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("警告");
			alert.setHeaderText(null);
			alert.setContentText("删除为空");
			alert.showAndWait();
		}
	}

	@FXML
	private void handleNewExpert() {
		Expert tempExpert = new Expert();
		boolean okClicked = controller.showExpertEditDialog(tempExpert);
		if (okClicked) {
			try {
				Object[] params = { tempExpert.getThId(), tempExpert.getThName(), tempExpert.getThSex(),
						tempExpert.getThAge(), tempExpert.getThField(), tempExpert.getThProfessionalTitle() };
				String sql = "insert into expert (th_id, th_name, th_sex, th_age, th_field, th_professional_title) values (?,?,?,?,?,?)";
				queryRunner.update(sql, params);
				controller.getExpertData().add(tempExpert);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	private void handleEditExpert() {
		Expert selectedPerson = expertTable.getSelectionModel().getSelectedItem();
		if (selectedPerson != null) {
			boolean okClicked = controller.showExpertEditDialog(selectedPerson);
			if (okClicked) {
				Object[] params = { selectedPerson.getThId(), selectedPerson.getThName(), selectedPerson.getThSex(),
						selectedPerson.getThAge(), selectedPerson.getThField(), selectedPerson.getThProfessionalTitle(),selectedPerson.getThId() };
				String sql = "update expert set th_id = ?,th_name = ?,th_sex = ?,th_age = ?,th_field = ?,th_professional_title = ? where th_id = ?"; 
				try {
					queryRunner.update(sql, params);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("警告");
			alert.setHeaderText(null);
			alert.setContentText("未选中修改人员");
			alert.showAndWait();
		}
	}

	public void setData(MainLayoutController controller) {
		this.controller = controller;
		expertTable.setItems(controller.getExpertData());
	}

}
