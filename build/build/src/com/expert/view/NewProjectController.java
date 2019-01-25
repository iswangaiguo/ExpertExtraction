package com.expert.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.expert.model.Expert;
import com.expert.model.ProjectDetailsCopy;
import com.expert.utils.DBSource;
import com.expert.utils.TableString;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class NewProjectController {
	
	@FXML
	private TextField projectName;
	
	@FXML
	private DatePicker projectTime;
	
	@FXML
	private RadioButton projectThreePerson;
	
	@FXML
	private RadioButton projectFivePerson;
	
	@FXML
	private TextField projectLocation;
	
	@FXML
	private ScrollPane scrollPane;
	
	private List<CheckBox> list = new ArrayList<>();

	private MainLayoutController controller;
	
	@FXML
	private void choosePerson() {
		if (isInputValid()) {
			ProjectDetailsCopy projectDetailsCopy = new ProjectDetailsCopy();
			projectDetailsCopy.setProject_id(Double.parseDouble(new DateFormatUtils().format(new Date(),"yyyyMMddhhmmss")));
			projectDetailsCopy.setProject_name(projectName.getText());
			if (projectThreePerson.isSelected()) {
				projectDetailsCopy.setProject_num(3);
			} else {
				projectDetailsCopy.setProject_num(5);
			}
			projectDetailsCopy.setProject_location(projectLocation.getText());
			projectDetailsCopy.setProject_time(projectTime.getValue().toString());
			String selectedId = selectExpert(projectDetailsCopy);
			if (StringUtils.isBlank(selectedId)) {
				return;
			}else {
				projectDetailsCopy.setProject_expertsId(selectedId);
			}
			controller.showSelectedExpert(projectDetailsCopy);
		}
	}
	
	private String selectExpert(ProjectDetailsCopy projectDetailsCopy) {
		int num = projectDetailsCopy.getProject_num();
		List<Expert> experts = new ArrayList<>();
		List<CheckBox> collect = list.stream().filter(item -> item.isSelected()).collect(Collectors.toList());
		String selectedfield = "";
		for (CheckBox checkBox : collect) {
			selectedfield += checkBox.getText() + ",";
			experts.addAll(controller.getExpertData().stream()
					.filter(item -> item.getThField().equals(checkBox.getText())).collect(Collectors.toList()));
		}
		if (num > experts.size()) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("警告");
			alert.setHeaderText(null);
			alert.setContentText("所抽选专业人数小于" + num + "人");
			alert.showAndWait();
			return "";
		}
		projectDetailsCopy.setProject_fields(selectedfield.substring(0, selectedfield.length()-1));
		Random random = new Random();
		String selected = "";
		for(int i = 0; i < num; i++) {
			int selectedNum = random.nextInt(experts.size());
			if (i == num-1) {
				selected += experts.get(selectedNum).getThId();
			}else {
				selected += experts.get(selectedNum).getThId() + ",";
			}
			experts.remove(selectedNum);
		}
		System.out.println(selected);
		return selected;
	}

	private boolean isInputValid() {
		String errorMessage = "";
		if (StringUtils.isBlank(projectName.getText())) {
			errorMessage += "工程名为空\n";
		}
		if (projectTime.getValue() == null) {
			errorMessage += "未选择时间\n";
		}
		if (!projectThreePerson.isSelected() && !projectFivePerson.isSelected()) {
			errorMessage += "未选择人数\n";
		}
		if (StringUtils.isBlank(projectLocation.getText())) {
			errorMessage += "地点未填写\n";
		}
		if (list.stream().filter(item -> item.isSelected()).collect(Collectors.toList()).size() == 0) {
			errorMessage += "未选择抽取范围";
		}
		if (errorMessage.length() != 0) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("警告");
			alert.setHeaderText(null);
			alert.setContentText(errorMessage);
			alert.showAndWait();
			return false;
		}else {
			return true;
		}
	}

	@FXML
	private void initialize() {
		GridPane gridPane = new GridPane();
		gridPane.setHgap(20);
		gridPane.setVgap(3);
		for(int i = 0; i < TableString.FIELD.length; i++) {
			CheckBox checkBox = new CheckBox(TableString.FIELD[i]);
			list.add(checkBox);
			GridPane.setConstraints(checkBox, i%2, i/2);
			gridPane.getChildren().add(checkBox);
		}
		scrollPane.setContent(gridPane);
	}

	public void setData(MainLayoutController controller) {
		this.controller = controller; 
	}
	
}
