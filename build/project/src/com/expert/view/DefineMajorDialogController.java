package com.expert.view;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.expert.dao.CollogeAndMajorDao;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DefineMajorDialogController {
	
	private CollogeAndMajorDao cmDao;
	
	private Stage dialoStage;
	
	@FXML
	private ChoiceBox<String> thcollege;
	
	@FXML
	private TextField thMajor;
	
	@FXML
	private void initialize() {
		cmDao = new CollogeAndMajorDao();
		List<String> colloges = cmDao.getColloge();
		thcollege.setItems(FXCollections.observableArrayList(colloges));
	}
	
	
	@FXML
	private void handleOK() {
		if (inputValue()) {
			cmDao.addColloge(thcollege.getValue(), thMajor.getText().trim());
			MainLayoutController.resetMajor = true;
			dialoStage.close();
		}
	}
	
	
	private boolean inputValue() {
		String errorMsg = "";
		if (StringUtils.isBlank(thcollege.getValue())) {
			errorMsg += "未选择学院\n";
		} else if (StringUtils.isBlank(thMajor.getText())) {
			errorMsg += "专业输入为空";
		}
		if (errorMsg.length() == 0) {
			return true;
		} else {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("警告");
			alert.setHeaderText(null);
			alert.setContentText(errorMsg);
			alert.showAndWait();
			return false;
		}
	}


	@FXML
	private void handCancel( ) {
		dialoStage.close();
	}


	public void setDialogStage(Stage dialogStage) {
		this.dialoStage = dialogStage;
	}
}
