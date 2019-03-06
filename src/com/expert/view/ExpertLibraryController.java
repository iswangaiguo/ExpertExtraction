package com.expert.view;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.filechooser.FileSystemView;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;

import com.expert.model.Expert;
import com.expert.model.ExpertCopy;
import com.expert.utils.DBSource;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
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
	private TableColumn<Expert, String> thPhoneColumn;

	@FXML
	private TableColumn<Expert, String> thFieldColumn;

	@FXML
	private TableColumn<Expert, String> thProfessionalTitleColumn;

	@FXML
	private ProgressBar progressBar;

	private MainLayoutController controller;

	private QueryRunner queryRunner = new QueryRunner(DBSource.getDatasource());

	
	@FXML
	private void initialize() {
		progressBar.setVisible(false);
		thIdColumn.setCellValueFactory(cellData -> cellData.getValue().thIdProperty());
		thNameColumn.setCellValueFactory(cellData -> cellData.getValue().thNameProperty());
		thSexColumn.setCellValueFactory(cellData -> cellData.getValue().thSexProperty());
		thPhoneColumn.setCellValueFactory(cellData -> cellData.getValue().thPhoneProperty());
		thFieldColumn.setCellValueFactory(cellData -> cellData.getValue().thFieldProperty());
		thProfessionalTitleColumn.setCellValueFactory(cellData -> cellData.getValue().thProfessionalTitleProperty());
	}

	@FXML
	private void handleDefineMajor() {
		controller.showDefineMajor();
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
						tempExpert.getThPhone(), tempExpert.getThField(), tempExpert.getThProfessionalTitle() };
				String sql = "insert into expert (th_id, th_name, th_sex, th_phone, th_field, th_professional_title) values (?,?,?,?,?,?)";
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
				Object[] params = { selectedPerson.getThName(), selectedPerson.getThSex(), selectedPerson.getThPhone(),
						selectedPerson.getThField(), selectedPerson.getThProfessionalTitle(),
						selectedPerson.getThId() };
				String sql = "update expert set th_name = ?,th_sex = ?,th_phone = ?,th_field = ?,th_professional_title = ? where th_id = ?";
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
		fileChooser
				.setInitialDirectory(new File(FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath()));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Excel", "*.xls"));
		File file = fileChooser.showOpenDialog(new Stage());
		if (file == null) {
			return;
		}
		progressBar.setVisible(true);
		Task<Double> task = new Task<Double>() {
			@Override
			protected Double call() throws Exception {
				HSSFWorkbook workbook = null;
				int insertNum = 0;
				int updateNum = 0;
				try {
					POIFSFileSystem fileSystem = new POIFSFileSystem(file);
					workbook = new HSSFWorkbook(fileSystem);
					HSSFSheet sheet = workbook.getSheet("Sheet1");
					int lastRowIndex = sheet.getLastRowNum();
					ExpertCopy expertCopy;
					for (int i = 1; i <= lastRowIndex; i++) {
						HSSFRow row = sheet.getRow(i);
						expertCopy = new ExpertCopy();
						if (row == null || row.getCell(0) == null || row.getCell(0).toString().equals("")
								|| row.getCell(0).getCellTypeEnum() == CellType.BLANK) {
							continue;
						} else {
							expertCopy = gnerateExpertCopy(row.getCell(0), row.getCell(1), row.getCell(2),
									row.getCell(3), row.getCell(4), row.getCell(5));
						}
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
									expertCopy.getTh_phone(), expertCopy.getTh_field(),
									expertCopy.getTh_professional_title(), expertCopy.getTh_id() };
							sql = "update expert set th_name = ?,th_sex = ?,th_phone = ?,th_field = ?,th_professional_title = ? where th_id = ?";
							queryRunner.update(sql, params);
							updateNum++;
						} else {
							Object[] params = { expertCopy.getTh_id(), expertCopy.getTh_name(), expertCopy.getTh_sex(),
									expertCopy.getTh_phone(), expertCopy.getTh_field(), expertCopy.getTh_professional_title()};	
							sql = "insert into expert (th_id, th_name, th_sex, th_phone, th_field, th_professional_title) values (?,?,?,?,?,?)";
							queryRunner.update(sql, params);
							expertData.add(expert);
							insertNum++;
						}
						updateProgress(i*1.0, lastRowIndex*1.0);
					}
//					bathInsert(expertCopies);
					updateMessage("共插入" + insertNum + "条数据\n" + "共更新" + updateNum + "条数据");
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
				return 1D;
			}
			
			@Override
			protected void succeeded() {
				super.succeeded();
				progressBar.setVisible(false);
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("插入情况");
				alert.setHeaderText(null);
				alert.setContentText(getMessage());
				alert.showAndWait();
			}
			
//			private void bathInsert(List<ExpertCopy> expertCopies) {
//				try {
//					String insertsql = "insert into expert (th_id, th_name, th_sex, th_phone, th_field, th_professional_title) values (?,?,?,?,?,?)";
//					int expertSize = expertCopies.size();
//					Object[][] params = new Object[expertSize][];
//					for (int i = 0; i < expertSize; i++) {
//						updateProgress(i*1.0, expertSize*1.0);
//						params[i] = new Object[] { expertCopies.get(i).getTh_id(), expertCopies.get(i).getTh_name(),
//								expertCopies.get(i).getTh_sex(), expertCopies.get(i).getTh_phone(),
//								expertCopies.get(i).getTh_field(), expertCopies.get(i).getTh_professional_title() };
//					}
//					queryRunner.batch(insertsql, params);
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
			
		};
		progressBar.progressProperty().bind(task.progressProperty());
		new Thread(task).start();

	}



	public ExpertCopy gnerateExpertCopy(HSSFCell... expertCell) {
		expertCell[0].setCellType(CellType.STRING);
		ExpertCopy expertCopy = new ExpertCopy();
		String[] cellValue = new String[6];
		for (int i = 0; i < expertCell.length; i++) {
			if (expertCell[i] == null || expertCell.toString().equals("")
					|| expertCell[i].getCellTypeEnum() == CellType.BLANK) {
				cellValue[i] = "未设置";
				if (i == 3) {
					continue;
				}
			} else {
				if (i == 3) {
					expertCell[i].setCellType(CellType.STRING);
				}
				cellValue[i] = expertCell[i].getStringCellValue();
			}
		}
		expertCopy.setTh_id(cellValue[0]);
		expertCopy.setTh_name(cellValue[1]);
		expertCopy.setTh_sex(cellValue[2]);
		expertCopy.setTh_phone(cellValue[3]);
		expertCopy.setTh_professional_title(cellValue[4]);
		expertCopy.setTh_field(cellValue[5]);
		return expertCopy;

	}

	public void setData(MainLayoutController controller) {
		this.controller = controller;
		expertTable.setItems(controller.getExpertData());
	}

}
