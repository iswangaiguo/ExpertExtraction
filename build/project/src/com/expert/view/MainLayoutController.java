package com.expert.view;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.expert.MainApp;
import com.expert.model.Expert;
import com.expert.model.ExpertCopy;
import com.expert.model.ProjectDetails;
import com.expert.model.ProjectDetailsCopy;
import com.expert.utils.DBSource;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainLayoutController {

	private ObservableList<Expert> expertData = FXCollections.observableArrayList();
	private ObservableList<ProjectDetails> projectDetailsList = FXCollections.observableArrayList();
	
	private MainApp mainApp;

	@FXML
	private BorderPane borderPane;

	private AnchorPane newPanel;

	private AnchorPane expertPanel;

	private AnchorPane historyPanel;
	
	

	public MainLayoutController() {
		try {
			QueryRunner queryRunner = new QueryRunner(DBSource.getDatasource());
			List<ExpertCopy> list = queryRunner.query("select * from expert",
					new BeanListHandler<ExpertCopy>(ExpertCopy.class));
			for (ExpertCopy expertCopy : list) {
				expertData.add(new Expert(expertCopy));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ObservableList<Expert> getExpertData() {
		return expertData;
	}
	
	public ObservableList<ProjectDetails> getProjectDetailsList() {
		return projectDetailsList;
	}

	@FXML
	private void creaeteNewProject() {
		if (newPanel == null) {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(MainLayoutController.class.getResource("NewProjectview.fxml"));
				newPanel = (AnchorPane) loader.load();
				NewProjectController controller = loader.getController();
				controller.setData(this);
				borderPane.setCenter(newPanel);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			borderPane.setCenter(newPanel);
		}
	}

	@FXML
	private void showExpertLibrary() {
		if (expertPanel == null) {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(MainLayoutController.class.getResource("ExpertLibrary.fxml"));
				expertPanel = (AnchorPane) loader.load();
				ExpertLibraryController controller = loader.getController();
				controller.setData(this);
				borderPane.setCenter(expertPanel);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			borderPane.setCenter(expertPanel);
		}
	}

	@FXML
	private void showHistory() {
		if (historyPanel == null) {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(MainLayoutController.class.getResource("ProjectHistory.fxml"));
				historyPanel = (AnchorPane) loader.load();
				ProjectHistoryController controller = loader.getController();
				controller.setData(this);
				borderPane.setCenter(historyPanel);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			borderPane.setCenter(historyPanel);
		}
	}

	public boolean showExpertEditDialog(Expert expert) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainLayoutController.class.getResource("ExpertEditDialog.fxml"));
			AnchorPane anchorPane = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Expert");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(mainApp.getStage());
			Scene scene = new Scene(anchorPane);
			dialogStage.setScene(scene);

			ExpertEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setExpert(expert);
			controller.setData(this);
			dialogStage.showAndWait();
			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	public void showSelectedExpert(ProjectDetailsCopy projectDetailsCopy) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainLayoutController.class.getResource("SelectedResult.fxml"));
			AnchorPane anchorPane = (AnchorPane)loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("抽取结果");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(mainApp.getStage());
			Scene scene = new Scene(anchorPane);
			dialogStage.setScene(scene);
			SelectedResultController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setData(projectDetailsCopy);
			controller.setHistoryEvent(projectDetailsList);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void initialize() {
		creaeteNewProject();
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}


}
