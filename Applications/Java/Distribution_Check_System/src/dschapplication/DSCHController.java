package dschapplication;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import hyperledger.fabric.javasdk.CAConnector;
import hyperledger.fabric.javasdk.ChainCodeConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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
	@FXML TableColumn<AssetInfo, String> userInfoNameColumn;
	@FXML TableColumn<AssetInfo, String> userInfoValueColumn;
	
	private Stage primaryStage;
	private PropReader propreader;
	private ObservableList<UserAsset> userAssetObservableList;
	private ObservableList<AssetInfo> assetInfoObservableList;
	Map<String ,UserAsset> userMap = new HashMap<String ,UserAsset>();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initTables();
		System.out.println("Distribution Check system start");
		
		String propURL = "./config.properties";
		
		propreader = new PropReader(propURL);
		propreader.propReader();
		caconnector = new CAConnector("http://localhost:7054");
		caconnector.issueAdmin("admin", "adminpw", "Org1");
		
		caconnector.registerUser("user1", "Org1");
		
		org1ChainConnector = new ChainCodeConnector();

		org1ChainConnector.setChInfo("dschchannel", "orderer.dsch.com", "grpc://localhost:7050");
		org1ChainConnector.loginUser("user1");
	
		org1ChainConnector.connectToChannel("peer0.org1.dsch.com", "grpc://localhost:7051", "grpc://localhost:7053", "eventhub01");
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
		
		userIDColumn.setCellFactory(tc->{
			 TableCell<UserAsset, String> cell = new TableCell<UserAsset, String>(){
				 @Override
				 protected void updateItem(String item, boolean empty) {
					 super.updateItem(item, empty);
					 setText(empty ? null : item);
				 }
			 };
			 cell.setOnMouseClicked(e->{
				 if(! cell.isEmpty()) {
					 assetInfoObservableList.clear();
					 String userID = cell.getItem();
					
					 System.out.println("get Item:"+userID);
					 UserAsset userAsset = userMap.get(userID);
					 ArrayList<AssetInfo> userInfo = userAsset.getAssetArray();
					 for(int i=0; i<userInfo.size(); i++) {
						 assetInfoObservableList.add(userInfo.get(i));
					 }
				 }
			 });
			 return cell;
		});
	}
	private void initTables() {
		userAssetObservableList = FXCollections.observableArrayList();
		assetInfoObservableList = FXCollections.observableArrayList();
		userIDColumn.setCellValueFactory(cellData->cellData.getValue().getUserID());
		userInfoNameColumn.setCellValueFactory(cellData->cellData.getValue().getAssetName());
		userInfoValueColumn.setCellValueFactory(cellData->cellData.getValue().getAssetValue());
		userListTableView.setItems(userAssetObservableList);
		userAssetStatusTableView.setItems(assetInfoObservableList);
		
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
    public void queryParticipants() {
    	System.out.println("querying participants");
    	try {
			String queryResult = org1ChainConnector.queryBlockChain("dschchannel", "dschuser", "queryAllUserList");
			
			JsonArray jsonArray = new JsonParser().parse(queryResult).getAsJsonArray();
			int jsonArrayLength = jsonArray.size();
			for(int i=0; i<jsonArrayLength; i++) {
				ArrayList<AssetInfo> assetArray = new ArrayList<AssetInfo>();
			
				JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
			
				JsonPrimitive keyPrimitive = jsonObject.getAsJsonPrimitive("Key");
			
				JsonObject valueObject = jsonObject.getAsJsonObject("Record");
				
			
	
				
				JsonPrimitive userNamePrimitive = valueObject.getAsJsonPrimitive("userName");
				assetArray.add(new AssetInfo("userName", userNamePrimitive.getAsString()));
			
				JsonPrimitive hasMoneyPrimitive = valueObject.getAsJsonPrimitive("hasMoney");
				assetArray.add(new AssetInfo("hasMoney", hasMoneyPrimitive.getAsString()));
				
				JsonPrimitive hasVegetablePrimitive = valueObject.getAsJsonPrimitive("hasVegetable");
				assetArray.add(new AssetInfo("hasVegetable", hasVegetablePrimitive.getAsString()));
				
				JsonPrimitive hasMineralPrimitive = valueObject.getAsJsonPrimitive("hasMineral");
				assetArray.add(new AssetInfo("hasMineral", hasMineralPrimitive.getAsString()));

				
				JsonPrimitive hasMeatPrimitive = valueObject.getAsJsonPrimitive("hasMeat");
				assetArray.add(new AssetInfo("hasMeat", hasMeatPrimitive.getAsString()));
			
				JsonPrimitive hasGrainPrimitive = valueObject.getAsJsonPrimitive("hasGrain");
				assetArray.add(new AssetInfo("hasGrain", hasGrainPrimitive.getAsString()));
			
				JsonPrimitive hasFruitPrimitive = valueObject.getAsJsonPrimitive("hasFruit");
				assetArray.add(new AssetInfo("hasFruit", hasFruitPrimitive.getAsString()));

				UserAsset userAsset = new UserAsset(keyPrimitive.getAsString(), assetArray);
				userAssetObservableList.add(userAsset);
				userMap.put(keyPrimitive.getAsString(), userAsset);
			}
				
		} catch (ProposalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
    		caconnector.issueAdmin(propreader.getCaOrg1AdminID(), propreader.getCaOrg1AdminPass(), userOrgfield.getText());
    		caconnector.registerUser(userIDfield.getText(), userOrgfield.getText());
    	}
    	else if(userOrgfield.getText().equals("Org2")) {
    		caconnector = new CAConnector(propreader.getCaOrg2URL());
    		caconnector.issueAdmin(propreader.getCaOrg2AdminID(), propreader.getCaOrg2AdminPass(), userOrgfield.getText());
    		caconnector.registerUser(userIDfield.getText() , userOrgfield.getText());
    	}
    	else if(userOrgfield.getText().equals("Org3")) {
    		caconnector = new CAConnector(propreader.getCaOrg3URL());
    		caconnector.issueAdmin(propreader.getCaOrg3AdminID(), propreader.getCaOrg3AdminPass(), userOrgfield.getText());
    		caconnector.registerUser(userIDfield.getText(), userOrgfield.getText());
    	}
    	else {
    		System.out.println("This organization not include system");
    	}
    }
}
