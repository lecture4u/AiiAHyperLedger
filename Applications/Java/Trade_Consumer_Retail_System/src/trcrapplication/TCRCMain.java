package trcrapplication;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class TCRCMain extends Application {
	FXMLLoader loader;
	@Override
	public void start(Stage primaryStage) {
		loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("TRCR.fxml"));
		Parent root;
		
		
		try {
			root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setTitle("Hyperledger Blockchain Trade Check - Consumer & Retail Application");
			TRCRController controller = loader.getController();
		
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
