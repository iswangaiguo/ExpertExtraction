package com.expert.view;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.filechooser.FileSystemView;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;

import com.expert.model.Expert;
import com.expert.model.ExpertCopy;
import com.expert.utils.DBSource;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ExpertLibraryController {

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
				Object[] params = {selectedPerson.getThName(), selectedPerson.getThSex(),
						selectedPerson.getThAge(), selectedPerson.getThField(), selectedPerson.getThProfessionalTitle(),
						selectedPerson.getThId() };
				String sql = "update expert set th_name = ?,th_sex = ?,th_age = ?,th_field = ?,th_professional_title = ? where th_id = ?";
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

	@FXML
	private void importxls() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("导入excel");
		fileChooser.setInitialDirectory(new File(FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath()));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Excel", "*.xls"));
		File file = fileChooser.showOpenDialog(new Stage());
		if (file == null) {
			return;
		}
		HSSFWorkbook workbook = null;
		try {
			POIFSFileSystem fileSystem = new POIFSFileSystem(file);
			workbook = new HSSFWorkbook(fileSystem);
			HSSFSheet sheet = workbook.getSheet("Sheet1");
			int lastRowIndex = sheet.getLastRowNum();
			int insertNum = 0;
			int updateNum = 0;
			for (int i = 1; i <= lastRowIndex; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row == null)
					break;
				ExpertCopy expertCopy = new ExpertCopy();
				if (row.getCell(0) == null) {
					continue;
				}
				row.getCell(0).setCellType(CellType.STRING);
				expertCopy.setTh_id(row.getCell(0).getStringCellValue());
				expertCopy.setTh_name(row.getCell(1).getStringCellValue());
				expertCopy.setTh_sex(row.getCell(2).getStringCellValue());
				row.getCell(3).setCellType(CellType.NUMERIC);
				expertCopy.setTh_age((int) row.getCell(3).getNumericCellValue());
				expertCopy.setTh_professional_title(row.getCell(4).getStringCellValue());
				expertCopy.setTh_field(row.getCell(5).getStringCellValue());
				Expert expert = new Expert(expertCopy);
				String sql = "";
				ObservableList<Expert> expertData = controller.getExpertData();
				boolean flag = false;
				int index = 0;
				for (Expert item : expertData) {
					if (item.getThId().equals(expert.getThId())) {
						flag = true;
						break;
					}
					index++;
				}
				if (flag) {
					expertData.set(index, expert);
					Object[] params = { expertCopy.getTh_name(), expertCopy.getTh_sex(),
							expertCopy.getTh_age(), expertCopy.getTh_field(), expertCopy.getTh_professional_title(),expertCopy.getTh_id()};
					sql = "update expert set th_name = ?,th_sex = ?,th_age = ?,th_field = ?,th_professional_title = ? where th_id = ?";
					queryRunner.update(sql, params);
					updateNum++;
				} else {
					Object[] params = { expertCopy.getTh_id(), expertCopy.getTh_name(), expertCopy.getTh_sex(),
							expertCopy.getTh_age(), expertCopy.getTh_field(), expertCopy.getTh_professional_title()};	
					sql = "insert into expert (th_id, th_name, th_sex, th_age, th_field, th_professional_title) values (?,?,?,?,?,?)";
					queryRunner.update(sql, params);
					expertData.add(expert);
					insertNum++;
				}
			}
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("插入情况");
			alert.setHeaderText(null);
			alert.setContentText("共插入" + insertNum + "条数据\n"
					+ "共更新" + updateNum + "条数据");
			alert.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public void setData(MainLayoutController controller) {
		this.controller = controller;
		expertTable.setItems(controller.getExpertData());
	}

}
