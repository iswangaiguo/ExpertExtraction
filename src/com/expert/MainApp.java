package com.expert;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.expert.utils.DBSource;
import com.expert.utils.TableString;
import com.expert.view.MainLayoutController;
import com.expert.view.RootLayoutController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author wag
 *
 */
public class MainApp extends Application{

	private Stage primaryStage;
	
	private BorderPane rootLayout;
	
	@Override
	public void start(Stage stage) throws Exception {
		this.primaryStage = stage;
		this.primaryStage.setTitle("Expert"); 
		initSqliteData();
		initRootLayout();
		showMainLayout();
	}

	private void initSqliteData() {
		try {
			QueryRunner runner = new QueryRunner(DBSource.getDatasource());
			runner.update(TableString.CREATE_EXPERT_TABLE);
			runner.update(TableString.CREATE_PROJECTS_TABLE);
			runner.update(TableString.CREATE_COLLOGE_MAJOR_NAME_TABLE);
			Integer num = runner.query("select count(*) from colloge_major_name", new ScalarHandler<Integer>());
			if (num == 0) {
				runner.update(TableString.INSERT_CMNAME);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void initRootLayout() {
		try {
			primaryStage.setResizable(false);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane)loader.load();
			Scene scene = new Scene(rootLayout);
			RootLayoutController controller = loader.getController();
			primaryStage.setScene(scene);
	        primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void showMainLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/MainOverview.fxml"));
			AnchorPane mainPane = (AnchorPane)loader.load();
			MainLayoutController controller = loader.getController();
			controller.setMainApp(this);
			rootLayout.setCenter(mainPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public Stage getStage() {
		return primaryStage;
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
