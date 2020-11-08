//Philip Murray and Patrick Barry

package sl.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import sl.view.SLController;

public class SongLib extends Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception { // (1) set fx:controller in fxml (2) cast AnchorPane as it is in fxml
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/sl/view/sl.fxml"));
		
		AnchorPane root = (AnchorPane) loader.load();
		
		SLController listController = loader.getController();
		listController.start(primaryStage);
		
		Scene scene = new Scene(root);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	
	}

}