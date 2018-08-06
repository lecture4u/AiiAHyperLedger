package dschapplication;

import java.net.URL;
import java.util.ResourceBundle;

import hyperledger.fabric.javasdk.CAConnector;
import hyperledger.fabric.javasdk.ChainCodeConnector;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DSCHController implements Initializable{
	CAConnector caconnector;
	ChainCodeConnector org1ChainConnector;
	ChainCodeConnector org2ChainConnector;
	ChainCodeConnector org3ChainConnector;
	/*-------User Tab - Hyperledger Fabric Network connection component----------------*/
	@FXML TextField userIDfield;
	@FXML TextField userOrgfield;
	
	/*-------User Tab - Distribution Participants status component----------------*/
	@FXML TextField assetNamefield;
	@FXML TextField assetQuantityfield;
	@FXML TableView userListTableView;
	@FXML TableColumn<UserAsset, String> userIDColumn;
	@FXML TableView userAssetStatusTableView;
	@FXML TableColumn<UserInfo, String> userInfoNameColumn;
	@FXML TableColumn<UserInfo, String> userInfoValueColumn;
	
	private Stage primaryStage;
	private PropReader propreader;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("Distribution Check system start");
		
		String propURL = "./config.properties";
		
		propreader = new PropReader(propURL);
		propreader.propReader();
		
		System.out.println(propreader.getCaOrg1URL());
		System.out.println(propreader.getCaOrg1AdminID());
		System.out.println(propreader.getCaOrg1AdminPass());
		
		System.out.println(propreader.getCaOrg2URL());
		System.out.println(propreader.getCaOrg2AdminID());
		System.out.println(propreader.getCaOrg2AdminPass());
		
		System.out.println(propreader.getCaOrg3URL());
		System.out.println(propreader.getCaOrg3AdminID());
		System.out.println(propreader.getCaOrg3AdminPass());
		
		System.out.println(propreader.getDschUserPeerDomain());
		System.out.println(propreader.getDschUserPeerURL());
		System.out.println(propreader.getDschUserPeerEventHub());
		
		System.out.println(propreader.getDschWholeSalePeerDomain());
		System.out.println(propreader.getDschWholeSalePeerURL());
		System.out.println(propreader.getDschWholeSalePeerEventHub());
		
		System.out.println(propreader.getDschLocalSalePeerDomain());
		System.out.println(propreader.getDschLocalSalePeerURL());
		System.out.println(propreader.getDschLocalSalePeerEventHub());
		
		System.out.println(propreader.getOrdererDomain());
		System.out.println(propreader.getOrdererURL());
		
		System.out.println(propreader.getChannelNAME());
	}
	
	public void setStage(Stage stage) {
		this.primaryStage = stage;
	}
    @FXML
    public void sellingAsset(){
    	System.out.println("selling my asset");
    }
    @FXML 
    public void buyingAsset() {
    	System.out.println("buying localsale asset");
    }
    
    @FXML
    public void loginUser() {
    	System.out.println("User Login");
    	
        
    	if(userOrgfield.getText().equals("Org1")) {
    		org1ChainConnector = new ChainCodeConnector();
    		org1ChainConnector.setChInfo(propreader.getChannelNAME(), propreader.getOrdererURL(), propreader.getOrdererDomain());
    		org1ChainConnector.loginUser(userIDfield.getText());

    		org1ChainConnector.connectToChannel(propreader.getDschUserPeerDomain(), propreader.getDschUserPeerURL(), propreader.getDschUserPeerEventHub(), "event01");
    	}
    	else if(userOrgfield.getText().equals("Org2")) {
    		org2ChainConnector = new ChainCodeConnector();
    		org2ChainConnector.setChInfo(propreader.getChannelNAME(), propreader.getOrdererURL(), propreader.getOrdererDomain());
    		org2ChainConnector.loginUser(userIDfield.getText());

    		org2ChainConnector.connectToChannel(propreader.getDschWholeSalePeerDomain(), propreader.getDschWholeSalePeerURL(), propreader.getDschWholeSalePeerEventHub(), "event02");
    	}
    	else if(userOrgfield.getText().equals("Org3")) {
    		org3ChainConnector = new ChainCodeConnector();
    		org3ChainConnector.setChInfo(propreader.getChannelNAME(), propreader.getOrdererURL(), propreader.getOrdererDomain());
    		org3ChainConnector.loginUser(userIDfield.getText());

    		org1ChainConnector.connectToChannel(propreader.getDschLocalSalePeerDomain(), propreader.getDschLocalSalePeerURL(), propreader.getDschLocalSalePeerEventHub(), "event03");
    	}
    	else {
    		System.out.println("This organization not include system");
    	}
    }
    @FXML
    public void registerUser() {
    	System.out.println("User Resist");
    
    	if(userOrgfield.getText().equals("Org1")) {
    		caconnector = new CAConnector(propreader.getCaOrg1URL());
    		caconnector.issuAdmin(propreader.getCaOrg1AdminID(), propreader.getCaOrg1AdminPass(), userOrgfield.getText());
    		caconnector.registerUser(userIDfield.getText(), userOrgfield.getText());
    	}
    	else if(userOrgfield.getText().equals("Org2")) {
    		caconnector = new CAConnector(propreader.getCaOrg2URL());
    		caconnector.issuAdmin(propreader.getCaOrg2AdminID(), propreader.getCaOrg2AdminPass(), userOrgfield.getText());
    		caconnector.registerUser(userIDfield.getText() , userOrgfield.getText());
    	}
    	else if(userOrgfield.getText().equals("Org3")) {
    		caconnector = new CAConnector(propreader.getCaOrg3URL());
    		caconnector.issuAdmin(propreader.getCaOrg3AdminID(), propreader.getCaOrg3AdminPass(), userOrgfield.getText());
    		caconnector.registerUser(userIDfield.getText(), userOrgfield.getText());
    	}
    	else {
    		System.out.println("This organization not include system");
    	}
    }
}
