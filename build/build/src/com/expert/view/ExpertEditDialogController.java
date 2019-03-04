package com.expert.view;

import static java.util.stream.Collectors.toList;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.lang3.StringUtils;

import com.expert.model.Expert;
import com.expert.utils.DBSource;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ExpertEditDialogController {

	@FXML
	private TextField thIdField;
	@FXML
	private TextField thNameField;
	@FXML
	private TextField thPhoneField;
	@FXML
	private RadioButton thMale;
	@FXML
	private RadioButton thFemale;

	@FXML
	private ChoiceBox<String> thProfessionalTitle;

	@FXML
	private ChoiceBox<String> thcollege;

	@FXML
	private ChoiceBox<String> thmajor;

	private Stage dialogStage;
	private Expert expert;
	private boolean okClicked = false;
	private MainLayoutController controller;
	private final QueryRunner queryRunner = new QueryRunner(DBSource.getDatasource());
	
	private List<Object[]> cmnList;

	@FXML
	private void initialize() {
		thProfessionalTitle.setItems(FXCollections.observableArrayList("助理讲师", "讲师", "副教授", "教授"));
		try {
			String sql = "select * from colloge_major_name";
			cmnList = queryRunner.query(sql, new ArrayListHandler());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		thcollege.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				Optional<String> index = cmnList.stream().filter(item -> StringUtils.equals(newValue, (String) item[1]))
						.map(item -> (String) item[0]).findFirst();
				thmajor.setItems(FXCollections.observableArrayList(cmnList.stream()
						.filter(item -> ((String)item[0]).startsWith(index.get()))
						.map(item -> (String)item[1])
						.skip(1)
						.collect(toList())));
			}
		});
	}


	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setExpert(Expert expert) {
		this.expert = expert;
		thcollege.setItems(
				FXCollections.observableArrayList(cmnList.stream().filter(item -> ((String) item[0]).length() == 2)
						.map(item -> (String) item[1]).collect(toList())));
		if (expert.getThId().equals("")) {
			thIdField.setEditable(true);
			thIdField.setText("");
			thNameField.setText("");
			thPhoneField.setText("");
		} else {
			thIdField.setEditable(false);
			thIdField.setText(expert.getThId());
			thNameField.setText(expert.getThName());
			if (expert.getThSex().equals("男")) {
				thMale.setSelected(true);
			} else {
				thFemale.setSelected(true);
			}
			thPhoneField.setText(expert.getThPhone());
			
			Optional<String> index = cmnList.stream().filter(item -> StringUtils.equals(expert.getThField(), (String) item[1]))
					.map(item -> ((String) item[0]).substring(0, 2)).findFirst();
			
			thcollege.setValue(cmnList.stream().filter(item -> ((String)item[0]).equals(index.get())).map(i -> (String)i[1]).findFirst().get());
			
			thmajor.setItems(FXCollections.observableArrayList(cmnList.stream()
					.filter(item -> ((String)item[0]).startsWith(index.get()))
					.map(item -> (String)item[1])
					.skip(1)
					.collect(toList())));
			
			thmajor.setValue(expert.getThField());
			
			thmajor.setValue(expert.getThField());
			thProfessionalTitle.setValue(expert.getThProfessionalTitle());
		}
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleOk() {
		if (isInputValid()) {
			expert.setThId(thIdField.getText());
			expert.setThName(thNameField.getText());
			expert.setThPhone(thPhoneField.getText());
			if (thMale.isSelected()) {
				expert.setThSex("男");
			} else {
				expert.setThSex("女");
			}
	        expert.setThField(thmajor.getValue());
			expert.setThProfessionalTitle(thProfessionalTitle.getValue());
			okClicked = true;
			dialogStage.close();
		}
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	private boolean isInputValid() {
		String errorMessage = "";

		if (!StringUtils.isNumeric((thIdField.getText()))) {
			errorMessage += "编号格式不对\n";
		} else {
			Iterator<Expert> iterator = controller.getExpertData().iterator();
			while (iterator.hasNext()) {
				if (iterator.next().getThId().equals(thIdField.getText())) {
					if (expert.getThId().equals(thIdField.getText())) {
						errorMessage += "";
					} else {
						errorMessage += "编号重复!\n";
					}
				}
			}
		}
		if (StringUtils.isBlank(thNameField.getText())) {
			errorMessage += "姓名不能为空!\n";
		}
		if (!thFemale.isSelected() && !thMale.isSelected()) {
			errorMessage += "性别不能为空!\n";
		}

		if (!StringUtils.isNumeric(thPhoneField.getText()) || thPhoneField.getText().length() != 11) {
			errorMessage += "电话格式不对!\n";
		}

        if (StringUtils.isAnyBlank(thcollege.getValue(), thmajor.getValue())){
            errorMessage += "专业不能为空!\n"; 
        } 

		if (StringUtils.isBlank(thProfessionalTitle.getValue())) {
			errorMessage += "职称不能为空!\n";
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("警告");
			alert.setHeaderText(null);
			alert.setContentText(errorMessage);
			alert.showAndWait();
			return false;
		}
	}

	public void setData(MainLayoutController controller) {
		this.controller = controller;
	}

}
