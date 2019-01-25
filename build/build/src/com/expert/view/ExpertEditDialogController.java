package com.expert.view;

import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import com.expert.model.Expert;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ExpertEditDialogController {

		@FXML
	    private TextField thIdField;
	    @FXML
	    private TextField thNameField;
	    @FXML
	    private TextField thAgeField;
	    @FXML
	    private RadioButton thMale;
	    @FXML
	    private RadioButton thFemale;
	    @FXML
	    private TextField thFieldField;
	    @FXML
	    private TextField thProfessionalTitleField;
	    
	    private Stage dialogStage;
	    private Expert expert;
	    private boolean okClicked = false;
		private MainLayoutController controller;
	    
	    @FXML
	    private void initialize() {
	    }
	    
	    public void setDialogStage(Stage dialogStage) {
	        this.dialogStage = dialogStage;
	    }
	    
	    public void setExpert(Expert expert) {
	    	this.expert = expert;
	    	if (expert.getThId() == 0) {
	    		thIdField.setEditable(true);
	    		thIdField.setText("");
	  	        thNameField.setText("");
	  	        thAgeField.setText("");
	  	        thFieldField.setText("");
	  	        thProfessionalTitleField.setText("");
			} else {
				thIdField.setEditable(false);
				thIdField.setText(Integer.toString(expert.getThId()));
				thNameField.setText(expert.getThName());
				if (expert.getThSex().equals("男")) {
					thMale.setSelected(true);
				}else {
					thFemale.setSelected(true);
				}
				thAgeField.setText(Integer.toString(expert.getThAge()));
				thFieldField.setText(expert.getThField());
				thProfessionalTitleField.setText(expert.getThProfessionalTitle());
			}
	    }
	    
	    public boolean isOkClicked() {
	        return okClicked;
	    }
	    
	    @FXML
	    private void handleOk() {
	        if (isInputValid()) {
	        	expert.setThId(Integer.parseInt(thIdField.getText()));
	        	expert.setThName(thNameField.getText());
	        	expert.setThAge(Integer.parseInt(thAgeField.getText()));
	        	if (thMale.isSelected()) {
	        		expert.setThSex("男");
				} else {
					expert.setThSex("女");
				}
	        	expert.setThField(thFieldField.getText());
	        	expert.setThProfessionalTitle(thProfessionalTitleField.getText());
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
	   	        	if (iterator.next().getThId() == Integer.parseInt(thIdField.getText())) {
	   	        		if (expert.getThId() == Integer.parseInt(thIdField.getText())) {
	   	        			errorMessage += "";
	   					} else {
	   						errorMessage += "编号重复";
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

	        if (!StringUtils.isNumeric(thAgeField.getText()) || thAgeField.getText().length() > 2) {
	            errorMessage += "年龄格式不对!\n"; 
	        } 
	        
	        if (StringUtils.isBlank(thFieldField.getText())) {
	            errorMessage += "专业不能为空!\n"; 
	        } 
	        
	        if (StringUtils.isBlank(thProfessionalTitleField.getText())) {
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
