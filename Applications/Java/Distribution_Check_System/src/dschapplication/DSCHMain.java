package dschapplication;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;


public class DSCHMain extends Application {
	FXMLLoader loader;
	@Override
	public void start(Stage primaryStage) {
	
		
		loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("DSCH.fxml"));
		Parent root;
		
		
		try {
			root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setTitle("Hyperledger Blockchain File Contrast Read Application");
			DSCHController controller = loader.getController();
		
			controller.setStage(primaryStage);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
