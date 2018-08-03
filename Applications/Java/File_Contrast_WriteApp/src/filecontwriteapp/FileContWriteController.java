package filecontwriteapp;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;

import hyperledger.fabric.javasdk.CAConnector;
import hyperledger.fabric.javasdk.ChainCodeConnector;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileContWriteController implements Initializable{
	private Stage primaryStage;
	/*-------Hyperledger Fabric Network connection component----------------*/
	@FXML TextField amdinIDfield;
	@FXML TextField adminPWfield;
	@FXML TextField userIDfield;
	@FXML TextField peerDomainField;
	@FXML TextField peerURLField;
	@FXML TextField CAURLField;
	@FXML TextField channelField;
	CAConnector caconnector;
	ChainCodeConnector chainConnector;
	/*-------            File Contrast component           ----------------*/
	@FXML TextField fileNameField;
	@FXML TextField fileHashField;
	@FXML TextArea transactionLogArea;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("File Contrast FileRead Application start");
		CAURLField.setText("http://localhost:7054");
		peerDomainField.setText("peer0.org1.filecont.com");
		peerURLField.setText("grpc://localhost:7051");
		channelField.setText("filecontchannel");
		caconnector = new CAConnector(CAURLField.getText());
		chainConnector = new ChainCodeConnector();
	}
	
	/*-------Hyperledger Fabric Network connection Methods----------------*/
	@FXML
	public void issueAdmin() {
		System.out.println("Issue Admin certificate");
		
		
		caconnector.issuAdmin(amdinIDfield.getText(), adminPWfield.getText());
	}
	@FXML
	public void registerUser() {
		System.out.println("Resister User certificate");
		caconnector.registerUser(userIDfield.getText());
	}
	@FXML
	public void logInUser() {
		System.out.println("Login User ID:"+userIDfield.getText());
		chainConnector.loginUser(userIDfield.getText());
		chainConnector.connectToChannel(peerDomainField.getText(), peerURLField.getText(), channelField.getText());
	}
	/*-------            File Contrast Methods           ----------------*/
	@FXML
	public void fileChoose() {
		System.out.println("Choose File to FileSystem");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open contrast File");
		File file = fileChooser.showOpenDialog(primaryStage);
		fileNameField.setText(file.getName());
	
		try {
			byte[] fileData = Files.readAllBytes(file.toPath());
			System.out.println(fileData);
		
			MessageDigest sh = MessageDigest.getInstance("SHA-256");
			sh.update(fileData);
			byte byteData[] = sh.digest();
			
			StringBuffer sb = new StringBuffer();
			for(int i=0; i<byteData.length; i++) {
				sb.append(Integer.toString((byteData[i]&0xff)+0x100,16).substring(1));
			}
			String SHA = sb.toString();
			fileHashField.setText(SHA);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@FXML
	public void writeFileHash() {
		String transactionLog ="";
		System.out.println("Write File Hash into the blockchain");
		try {
			BlockEvent.TransactionEvent event = chainConnector.invokeBlockChain("filecontchannel","filecont","insertFileHash",new String[] {fileNameField.getText(), fileHashField.getText()})
					.get(1200, TimeUnit.SECONDS);
			if (event.isValid()) {
				transactionLog = transactionLog+"Transaction  tx: " + event.getTransactionID() + "is completed.";
				transactionLog = transactionLog+"Transaction  timestamp: " + event.getTimestamp();
			} else {
				transactionLog = transactionLog+"Transaction  tx: " + event.getTransactionID() + "is invalid.";
				transactionLog = transactionLog+"Transaction  timestamp: " + event.getTimestamp();
			}
		} catch (ProposalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		transactionLogArea.setText(transactionLog);
		transactionLog = "";
	}
	public void setStage(Stage stage) {
		this.primaryStage = stage;
	}
}
