package trpwapplication;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;


import hyperledger.fabric.javasdk.CAConnector;
import hyperledger.fabric.javasdk.ChainCodeConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class TRPWSampleController implements Initializable{
	/*-------BlockChain network Connection components----------------*/
	CAConnector caconnector;
	ChainCodeConnector org1ChainConnector;
	ChainCodeConnector org2ChainConnector;
	ChainCodeConnector org3ChainConnector;
	/*-------User Tab - Hyperledger Fabric Network connection components----------------*/
	@FXML TextField userIDfield;
	@FXML TextField userOrgfield;
	
	@FXML Label org1ConnectionLabel;
	@FXML Label org2ConnectionLabel;
	@FXML Label org3ConnectionLabel;
	/*-------User Tab - Distribution Participants status components----------------*/
	@FXML TextField assetNamefield;
	@FXML TextField assetQuantityfield;
	@FXML TableView userListTableView;
	@FXML TableColumn<UserAsset, String> userIDColumn;
	@FXML TableView userAssetStatusTableView;
	@FXML TableColumn<AssetInfo, String> userInfoNameColumn;
	@FXML TableColumn<AssetInfo, String> userInfoValueColumn;
	
	/*-------Whole Sale & Retail components----------------*/
	@FXML TableView wholeSaleTableView;
	@FXML TableColumn<SaleInfo, String> wholesaleAssetNameColumn;
	@FXML TableColumn<SaleInfo, String> wholesaleMarketPriceColumn;
	@FXML TableColumn<SaleInfo, String> wholesaleQuantityColumn;
	
	@FXML TableView retailTableView;
	@FXML TableColumn<SaleInfo, String> retailAssetNameColumn;
	@FXML TableColumn<SaleInfo, String> retailMarketPriceColumn;
	@FXML TableColumn<SaleInfo, String> retailQuantityColumn;
	/*-------User Tab - New user Registance components----------------*/
	@FXML TextField initUserIDfield;
	@FXML TextField initUserNamefield;
	@FXML TextField initMoneyfield;
	@FXML TextField initVegetablefield;
	@FXML TextField initMineralfield;
	@FXML TextField initMeatfield;
	@FXML TextField initGrainfield;
	@FXML TextField initFruitfield;
	/*-----------History Tab - components----------------*/
	@FXML TextField userAssetHistoryfield;
	@FXML TextField wholeSaleAssetHistoryfield;
	@FXML TextArea histroyArea;
	/*-----------WholeSale<->Retail trade components----------------*/
	@FXML TextField wholeSaleAssetNameField;
	@FXML TextField wholeSaleAssetQuantityField;
	/*-----------BlockInfo components----------------*/
	@FXML TextArea blockInfoArea;
	@FXML TextField blockNumberField;
	/*-----------Distribution Tab - components----------------*/
	@FXML TableView dealListTableView;
	@FXML TableColumn<DealInfo, String> dealNameColumn;
	@FXML TableColumn<DealInfo, String> dealTypeColumn;
	@FXML TableColumn<DealInfo, String> dealSenderColumn;
	@FXML TableColumn<DealInfo, String> dealReceiveColumn;
	@FXML TableColumn<DealInfo, String> dealAssetColumn;
	
	@FXML TextArea dealTransactionArea;
	/*-------basic components----------------*/
	private Stage primaryStage;
	private PropReader propreader;
	private ObservableList<UserAsset> userAssetObservableList;
	private ObservableList<AssetInfo> assetInfoObservableList;
	
	private ObservableList<SaleInfo> wholeSaleObservableList;
	private ObservableList<SaleInfo> retailObservableList;
	
	private ObservableList<DealInfo> dealInfoObservableList;
	Map<String ,UserAsset> userMap = new HashMap<String ,UserAsset>();
	Map<String ,DealInfo> dealMap = new HashMap<String ,DealInfo>();
	/*----------basic component - asset selling/buying--------------------------------*/
	private String currentUser;
	private String currentDeal;
	private String wholeSaleMarketPrice;
	private String wholeSaleQuantity;
	private String retailMarketPrice;
	private String retailQuantity;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		  System.out.println("Trade Check - Provider&WholeSale system start");
			initTable();
			testDealInfoParsing();
			String propURL = "./config.properties";
			
			propreader = new PropReader(propURL);
			propreader.propReader();
			
			System.out.println("TRCR System init variable check");
			System.out.println(propreader.getCaOrg1AdminID());
			System.out.println(propreader.getCaOrg1AdminPass());
			System.out.println(propreader.getCaOrg1URL());
			
			System.out.println(propreader.getCaOrg2AdminID());
			System.out.println(propreader.getCaOrg2AdminPass());
			System.out.println(propreader.getCaOrg2URL());
			
			System.out.println(propreader.getCaOrg3AdminID());
			System.out.println(propreader.getCaOrg3AdminPass());
			System.out.println(propreader.getCaOrg3URL());
			
		
			System.out.println(propreader.getTrchProviderPeerDomain());
			System.out.println(propreader.getTrchProviderPeerEventHub());
			System.out.println(propreader.getTrchProviderPeerURL());
			
			System.out.println(propreader.getTrchWholeSalePeerDomain());
			System.out.println(propreader.getTrchWholeSalePeerEventHub());
			System.out.println(propreader.getTrchWholeSalePeerURL());
			
			System.out.println(propreader.getTrchRetailPeerDomain());
			System.out.println(propreader.getTrchRetailPeerEventHub());
			System.out.println(propreader.getTrchRetailPeerURL());
			
			System.out.println(propreader.getOrdererDomain());
			System.out.println(propreader.getOrdererURL());
			
			System.out.println(propreader.getChannelNAME());
			System.out.println(propreader.getChannelNAME2());
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
						 currentUser = cell.getItem();
					
						 UserAsset userAsset = userMap.get(currentUser);
						 ArrayList<AssetInfo> userInfo = userAsset.getAssetArray();
						 for(int i=0; i<userInfo.size(); i++) {
							 assetInfoObservableList.add(userInfo.get(i));
						 }
					 }
				 });
				 return cell;
			});
			dealNameColumn.setCellFactory(tc->{
				 TableCell<DealInfo, String> cell = new TableCell<DealInfo, String>(){
					 @Override
					 protected void updateItem(String item, boolean empty) {
						 super.updateItem(item, empty);
						 setText(empty ? null : item);
					 }
				 };
				 cell.setOnMouseClicked(e->{
					 if(! cell.isEmpty()) {
						 
						 dealTransactionArea.clear();
						 
						 currentDeal = cell.getItem();
						 DealInfo currentDealInfo = dealMap.get(currentDeal);
						 String dealTransactionInfo = currentDealInfo.getDealtransaction();
										
						 dealTransactionArea.setText(dealTransactionInfo);
					 }
				 });
				 return cell;
			});
	}
	private void initTable() {
		userAssetObservableList = FXCollections.observableArrayList();
		assetInfoObservableList = FXCollections.observableArrayList();
		wholeSaleObservableList = FXCollections.observableArrayList();
		retailObservableList = FXCollections.observableArrayList();
		dealInfoObservableList = FXCollections.observableArrayList();
		
		userIDColumn.setCellValueFactory(cellData->cellData.getValue().getUserID());
		userInfoNameColumn.setCellValueFactory(cellData->cellData.getValue().getAssetName());
		userInfoValueColumn.setCellValueFactory(cellData->cellData.getValue().getAssetValue());
		
		wholesaleAssetNameColumn.setCellValueFactory(cellData->cellData.getValue().getAssetName());
		wholesaleMarketPriceColumn.setCellValueFactory(cellData->cellData.getValue().getMarketPrice());
		wholesaleQuantityColumn.setCellValueFactory(cellData->cellData.getValue().getQuantity());
			
		retailAssetNameColumn.setCellValueFactory(cellData->cellData.getValue().getAssetName());
		retailMarketPriceColumn.setCellValueFactory(cellData->cellData.getValue().getMarketPrice());
		retailQuantityColumn.setCellValueFactory(cellData->cellData.getValue().getQuantity());
		
		dealNameColumn.setCellValueFactory(cellData->cellData.getValue().getDealName());
		dealTypeColumn.setCellValueFactory(cellData->cellData.getValue().getDealType());
		dealSenderColumn.setCellValueFactory(cellData->cellData.getValue().getDealSender());
		dealReceiveColumn.setCellValueFactory(cellData->cellData.getValue().getDealReceive());
		dealAssetColumn.setCellValueFactory(cellData->cellData.getValue().getDealAsset());
		
		userListTableView.setItems(userAssetObservableList);
		userAssetStatusTableView.setItems(assetInfoObservableList);
		wholeSaleTableView.setItems(wholeSaleObservableList);
		retailTableView.setItems(retailObservableList);
		dealListTableView.setItems(dealInfoObservableList);
	}
	public void setStage(Stage stage) {
		this.primaryStage = stage;
	}
	/*-------------------User Tab methods - Connected BlockChain network---------------*/
	   @FXML
		    public void loginUser() {
		    	System.out.println("User Login");
		    	
		        
		    	if(userOrgfield.getText().equals("Org1")) {
		    		org1ChainConnector = new ChainCodeConnector();
		    		org1ChainConnector.setChInfo(propreader.getChannelNAME(), propreader.getOrdererDomain(), propreader.getOrdererURL());
		    		org1ChainConnector.loginUser(userIDfield.getText());

		    		org1ChainConnector.connectToChannel(propreader.getTrchProviderPeerDomain(), propreader.getTrchProviderPeerURL(), propreader.getTrchProviderPeerEventHub(), "event01");
		    		org1ConnectionLabel.setText("Connected");
		    	
		    	}
		    	else if(userOrgfield.getText().equals("Org2")) {
		    		org2ChainConnector = new ChainCodeConnector();
		    		org2ChainConnector.setChInfo(propreader.getChannelNAME(), propreader.getOrdererDomain(), propreader.getOrdererURL());
		    		org2ChainConnector.loginUser(userIDfield.getText());

		    		org2ChainConnector.connectToChannel(propreader.getTrchWholeSalePeerDomain(), propreader.getTrchWholeSalePeerURL(), propreader.getTrchWholeSalePeerEventHub(), "event02");
		    		org2ConnectionLabel.setText("Connected");
		    		queryWholeSaleInfo();
		    	}
		    	else if(userOrgfield.getText().equals("Org3")) {
		    		org3ChainConnector = new ChainCodeConnector();
		    		org3ChainConnector.setChInfo(propreader.getChannelNAME2(), propreader.getOrdererDomain(), propreader.getOrdererURL());
		    		org3ChainConnector.loginUser(userIDfield.getText());

		    		org3ChainConnector.connectToChannel(propreader.getTrchRetailPeerDomain(), propreader.getTrchRetailPeerURL(), propreader.getTrchRetailPeerEventHub(), "event03");
		    		org3ConnectionLabel.setText("Connected");
		    		queryRetailInfo();
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
		    		caconnector.registerUser(userIDfield.getText() , userOrgfield.getText());
		    	}
		    	else {
		    		System.out.println("This organization not include system");
		    	}
		    }
		    @FXML
		    public void queryParticipants() {
		    	System.out.println("querying participants");
		    	userAssetObservableList.clear();
		    	userMap.clear();
		    	try {
					String queryResult = org1ChainConnector.queryBlockChain("trchproducer", "queryAllUserList");
					
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
		    public void insertNewUserAccount() throws NoSuchAlgorithmException{

		    	System.out.println("Insert user account to blockchain network");
		    
		    	
		    	
		  
		    	String[] userInitInformation = new String[] {initUserIDfield.getText(),initUserNamefield.getText(),  initMoneyfield.getText(), initVegetablefield.getText(), initMineralfield.getText(), initMeatfield.getText(), initGrainfield.getText(), 
		    			initFruitfield.getText()};
		    	System.out.println("Insert user Informations");
		    	for(int i=0; i<userInitInformation.length; i++) {
		    		System.out.println(i+": "+userInitInformation[i]);
		    	}
		    	
		    	try {
					org1ChainConnector.invokeBlockChain("trchproducer", "registUserAsset", userInitInformation);
				} catch (ProposalException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    /*-------------------Teade Mathods----------------*/
		    @FXML
		    public void sellingAsset(){
		    	String dealName = "SellingDeal";
		       dealName = dealName + getCurrentTime("DD HH:mm:ss");
		    	String dealType = "Selling";
		    	String dealTransaction = "";
		    	DealInfo dealInfo = new DealInfo(dealName, dealType, currentUser, "wholeSale", assetNamefield.getText());
		    	
		       queryWholeSaleAssetForKey(assetNamefield.getText());
		    	UserAsset currentUserInfo = userMap.get(currentUser);
		    	ArrayList<AssetInfo> currentAssetInfo = currentUserInfo.getAssetArray();
		       String currentUserAsset = "";
		        String currentUserMoney = currentAssetInfo.get(1).getAssetValue().get();
		      
		       for(int i=0; i<currentAssetInfo.size(); i++) {
		        	if(currentAssetInfo.get(i).getAssetName().get().equals("has"+assetNamefield.getText())) {
		        		
		        		currentUserAsset =  currentAssetInfo.get(i).getAssetValue().get();
		        	}
		        }
		       dealTransaction = "Selling User:"+currentUser+"'s Asset:"+assetNamefield.getText()+", "+currentUserAsset+"\n";
		       dealTransaction = dealTransaction + "To wholeSale MarketPrice:"+wholeSaleMarketPrice+" ,Quantity:"+wholeSaleQuantity+"\n";
		       
		       try {
		           int userAssetQuantityInt = Integer.parseInt(currentUserAsset);
		           int assetQuantityInt = Integer.parseInt(assetQuantityfield.getText());
		           int resultUserAssetQuantityInt = userAssetQuantityInt - assetQuantityInt;
		           //System.out.println("Then Result be User's Asset Quantity:"+currentUserAsset+"-"+assetQuantityfield.getText()+"="+resultUserAssetQuantityInt);
		           dealTransaction = dealTransaction +"Then Result be User's Asset Box Quantity:"+currentUserAsset+"-"+assetQuantityfield.getText()+"="+resultUserAssetQuantityInt+"\n";
		           
		           int currentUserMoneyInt = Integer.parseInt(currentUserMoney);
		           int wholeSaleMarketPriceInt = Integer.parseInt(wholeSaleMarketPrice);
		           int resultAssetPrice = wholeSaleMarketPriceInt* assetQuantityInt;
		           resultUserAssetQuantityInt = currentUserMoneyInt+ resultAssetPrice;
		           //System.out.println("               User's Money         :"+currentUserMoneyInt+"+"+wholeSaleMarketPrice+"*("+assetQuantityInt+"/1oo)="+resultUserAssetQuantityInt);
		           dealTransaction = dealTransaction +"               User's Money         :"+currentUserMoneyInt+"+"+wholeSaleMarketPrice+"*"+assetQuantityInt+"="+resultUserAssetQuantityInt+"\n";
		           
		           BlockEvent.TransactionEvent event = org1ChainConnector.invokeBlockChain("trchproducer","sellingAsset",new String[] {currentUser, assetNamefield.getText(),assetQuantityfield.getText(),resultAssetPrice+""})
							.get(1200, TimeUnit.SECONDS);
					if (event.isValid()) {
						dealTransaction = dealTransaction +"Transaction  tx: " + event.getTransactionID() + "is completed."+"\n";
						dealTransaction = dealTransaction +"Transaction  timestamp: " + event.getTimestamp()+"\n";
					} else {
						dealTransaction = dealTransaction +"Transaction  tx: " + event.getTransactionID() + "is invalid."+"\n";		
						dealTransaction = dealTransaction +"Transaction  timestamp: " + event.getTimestamp()+"\n";
					}
					
		           int wholeSaleAssetQuantityInt = Integer.parseInt(wholeSaleQuantity);
		           int wholesaleIncreaseAset = assetQuantityInt;
		           resultUserAssetQuantityInt = wholeSaleAssetQuantityInt + wholesaleIncreaseAset;
		          // System.out.println("               Wholesale's Asset Quantity:"+wholeSaleQuantity+"+"+assetQuantityfield.getText()+"/100="+resultUserAssetQuantityInt);
		           dealTransaction = dealTransaction +"               Wholesale's Asset Quantity:"+wholeSaleQuantity+"+"+assetQuantityfield.getText()+"="+resultUserAssetQuantityInt+"\n";
		           event = org2ChainConnector.invokeBlockChain("trchwholesale","increaseQuantity",new String[] {assetNamefield.getText(),wholesaleIncreaseAset+""})
							.get(1200, TimeUnit.SECONDS);
					if (event.isValid()) {
						dealTransaction = dealTransaction +"Transaction  tx: " + event.getTransactionID() + "is completed."+"\n";
						dealTransaction = dealTransaction +"Transaction  timestamp: " + event.getTimestamp()+"\n";
					} else {
					//	transactionLog = transactionLog+"Transaction  tx: " + event.getTransactionID() + "is invalid.";
						dealTransaction = dealTransaction +"Transaction  tx: " + event.getTransactionID() + "is invalid."+"\n";
						
						dealTransaction = dealTransaction +"Transaction  timestamp: " + event.getTimestamp()+"\n";
					
					}
					Thread.sleep(1000);
		           queryParticipants();
		           queryWholeSaleInfo();
		           dealInfo.setDealtransaction(dealTransaction);
		         
		           dealInfoObservableList.add(dealInfo);
		           dealMap.put(dealName, dealInfo);
		           dealInfoListSaveToJson();
		    	   // queryretailAssetForKey(assetNamefield.getText());
		          // distributionAsset();
		       }catch(Exception e) {
		    	   e.printStackTrace();
		       }
		    }
		    @FXML
		    public void transportWholesaleToRetail(){
		    	System.out.println("Transport WholeSale Asset to Retail");
		    	String dealName = "TransferDeal";
		        dealName = dealName + getCurrentTime("DD HH:mm:ss");
		      	String dealType = "Transfer";
		      	String dealTransaction = "";
		      	DealInfo dealInfo = new DealInfo(dealName, dealType, "wholeSale", "retail", wholeSaleAssetNameField.getText());
		      	
		     	queryWholeSaleAssetForKey(wholeSaleAssetNameField.getText());
		     	queryretailAssetForKey(wholeSaleAssetNameField.getText());
		    	
		     	int transportWholeSaleAsset = Integer.parseInt(wholeSaleAssetQuantityField.getText());
		     	
		     	dealTransaction = "Transfer from WholeSale Market "+transportWholeSaleAsset+" Box AssetName:"+wholeSaleAssetNameField.getText() +"\n";
		     	int transportRetailAssset = transportWholeSaleAsset * 100;
		     	try {
		     		
		     	
		     	    BlockEvent.TransactionEvent event = org2ChainConnector.invokeBlockChain("trchwholesale","decreaseQuantity",new String[] {wholeSaleAssetNameField.getText(),transportWholeSaleAsset+""})
		 					    .get(1200, TimeUnit.SECONDS);
		 			if (event.isValid()) {
		 				//transactionLog = transactionLog+"Transaction  tx: " + event.getTransactionID() + "is completed.";
		 				    System.out.println("Transaction  tx: " + event.getTransactionID() + "is completed.");
		 					dealTransaction = dealTransaction + "Transaction  tx: " + event.getTransactionID() + "is completed." +"\n";
		 			//	transactionLog = transactionLog+"Transaction  timestamp: " + event.getTimestamp();
		 				    System.out.println("Transaction  timestamp: " + event.getTimestamp());
		 				    dealTransaction = dealTransaction + "Transaction  timestamp: " + event.getTimestamp() +"\n";
		 		   } else {
		 			     //	transactionLog = transactionLog+"Transaction  tx: " + event.getTransactionID() + "is invalid.";
		 				    System.out.println("Transaction  tx: " + event.getTransactionID() + "is invalid.");
		 				    dealTransaction = dealTransaction + "Transaction  tx: " + event.getTransactionID() + "is invalid." +"\n";
		 				    //transactionLog = transactionLog+"Transaction  timestamp: " + event.getTimestamp();
		 				    System.out.println("Transaction  timestamp: " + event.getTimestamp());
		 				    dealTransaction = dealTransaction + "Transaction  timestamp: " + event.getTimestamp() +"\n";
		 		    }
		 			dealTransaction = dealTransaction + "Transfer to Retail Market "+transportRetailAssset+" AssetName:"+wholeSaleAssetNameField.getText() +"\n";
		 			event = org3ChainConnector.invokeBlockChain("trchretail","increaseQuantity",new String[] {wholeSaleAssetNameField.getText(),transportRetailAssset+""})
		 						.get(1200, TimeUnit.SECONDS);
		 			if (event.isValid()) {
		 					//transactionLog = transactionLog+"Transaction  tx: " + event.getTransactionID() + "is completed.";
		 					System.out.println("Transaction  tx: " + event.getTransactionID() + "is completed.");
		 					dealTransaction = dealTransaction + "Transaction  tx: " + event.getTransactionID() + "is completed." +"\n";
		 				//	transactionLog = transactionLog+"Transaction  timestamp: " + event.getTimestamp();
		 					System.out.println("Transaction  timestamp: " + event.getTimestamp());
		 					dealTransaction = dealTransaction + "Transaction  timestamp: " + event.getTimestamp() +"\n";
		 			} else {
		 				//	transactionLog = transactionLog+"Transaction  tx: " + event.getTransactionID() + "is invalid.";
		 					System.out.println("Transaction  tx: " + event.getTransactionID() + "is invalid.");
		 					dealTransaction = dealTransaction + "Transaction  tx: " + event.getTransactionID() + "is invalid." +"\n";
		 					//transactionLog = transactionLog+"Transaction  timestamp: " + event.getTimestamp();
		 					System.out.println("Transaction  timestamp: " + event.getTimestamp());
		 					dealTransaction = dealTransaction + "Transaction  timestamp: " + event.getTimestamp() +"\n";
		 			}
		 			
		 			Thread.sleep(1000);
		 			queryWholeSaleInfo();
		 			queryRetailInfo();
		 		      
		 			dealInfo.setDealtransaction(dealTransaction);
		 	       dealInfoObservableList.add(dealInfo);
		          dealMap.put(dealName, dealInfo);
		          dealInfoListSaveToJson();
		 	
		 
		 			
		     	}catch(Exception e) {
		     		e.printStackTrace();
		     	}
		    }
		    /*History methods*/
		    @FXML
		    public void queryUserHistory() {
		    	System.out.println("Query Provider Transaction History");
		    	String historyText = "";
		    	histroyArea.setText("");
		    	String[] variables = new String[]{userAssetHistoryfield.getText()};
		    	try {
					String queryResult = org1ChainConnector.hasVariablequeryBlockChain("trchproducer", "userInfoHistory",variables);
					JsonArray jsonArray = new JsonParser().parse(queryResult).getAsJsonArray();
					
					int jsonArrayLength = jsonArray.size();
					for(int i=0; i<jsonArrayLength; i++) {
					     if(i % 2 == 0) {
					    	 historyText = historyText+jsonArray.get(i)+"\n";
					     }
					     else {
					    	 String value = jsonArray.get(i).toString().substring(1, jsonArray.get(i).toString().length()-1);
					    	 value = value.replace("\\", "");
					    	// historyText = historyText+"    "+value+"\n";
					    	
					    	 JsonObject jsonObject =  new JsonParser().parse(value).getAsJsonObject();
					    	 
					    		JsonPrimitive userNamePrimitive = jsonObject.getAsJsonPrimitive("userName");
					    		historyText =historyText+"     UserName: "+userNamePrimitive.getAsString()+"\n";
								JsonPrimitive hasMoneyPrimitive = jsonObject.getAsJsonPrimitive("hasMoney");
								historyText =historyText+"     Money: "+hasMoneyPrimitive.getAsString()+"\n";
								JsonPrimitive hasVegetablePrimitive = jsonObject.getAsJsonPrimitive("hasVegetable");
								historyText =historyText+"     Vegetable: "+hasVegetablePrimitive.getAsString()+"\n";
								JsonPrimitive hasMineralPrimitive = jsonObject.getAsJsonPrimitive("hasMineral");
								historyText =historyText+"     Mineral: "+hasMineralPrimitive.getAsString()+"\n";
								JsonPrimitive hasMeatPrimitive = jsonObject.getAsJsonPrimitive("hasMeat");
								historyText =historyText+"     Meat: "+hasMeatPrimitive.getAsString()+"\n";
								JsonPrimitive hasGrainPrimitive = jsonObject.getAsJsonPrimitive("hasGrain");
								historyText =historyText+"     Grain: "+hasGrainPrimitive.getAsString()+"\n";
								JsonPrimitive hasFruitPrimitive = jsonObject.getAsJsonPrimitive("hasFruit");
								historyText =historyText+"     Fruit: "+hasFruitPrimitive.getAsString()+"\n";							
					     }
					}
				
					histroyArea.setText(historyText);
				} catch (ProposalException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    @FXML
		    public void queryWholeSaleHistory() {
		    	System.out.println("Query WholeSale Transaction History");
		    	String historyText = "";
		    	histroyArea.setText("");
		    	String[] variables = new String[]{wholeSaleAssetHistoryfield.getText()};
		    	try {
					String queryResult = org2ChainConnector.hasVariablequeryBlockChain("trchwholesale", "assetInfoHistory",variables);
		          JsonArray jsonArray = new JsonParser().parse(queryResult).getAsJsonArray();
					
					int jsonArrayLength = jsonArray.size();
					for(int i=0; i<jsonArrayLength; i++) {
					     if(i % 2 == 0) {
					    	 historyText = historyText+jsonArray.get(i)+"\n";
					     }
					     else {
					    	 String value = jsonArray.get(i).toString().substring(1, jsonArray.get(i).toString().length()-1);
					    	 value = value.replace("\\", "");
					    	// historyText = historyText+"    "+value+"\n";
					    	
					    	 JsonObject jsonObject =  new JsonParser().parse(value).getAsJsonObject();
					    	 
					    		JsonPrimitive userNamePrimitive = jsonObject.getAsJsonPrimitive("marketPrice");
					    		historyText =historyText+"     MarketPrice: "+userNamePrimitive.getAsString()+"\n";
								JsonPrimitive hasMoneyPrimitive = jsonObject.getAsJsonPrimitive("quantity");
								historyText =historyText+"     Quantity: "+hasMoneyPrimitive.getAsString()+"\n";
								
					     }
					}
					histroyArea.setText(historyText);
				} catch (ProposalException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    @FXML
		    public void queryBlockChain() {
		    	System.out.println("Query Block Information using Blocknumber");
		    //	String historyText = "";
		    	blockInfoArea.setText("");
		    	String[] variables = new String[]{propreader.getChannelNAME(), blockNumberField.getText()};
		    	try {
					String queryResult = org1ChainConnector.hasVariablequeryBlockChain("qscc", "GetBlockByNumber",variables);
					
					
					blockInfoArea.setText(queryResult);
				} catch (ProposalException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    /*-------------------TradeCheck prviate  methods----------------*/
		    private void queryRetailInfo() {
		    	
		    	retailObservableList.clear();
		    	try {
					String queryResult = org3ChainConnector.queryBlockChain("trchretail", "queryAllAssetList");
					
					JsonArray jsonArray = new JsonParser().parse(queryResult).getAsJsonArray();
					int jsonArrayLength = jsonArray.size();
					for(int i=0; i<jsonArrayLength; i++) {
						ArrayList<AssetInfo> assetArray = new ArrayList<AssetInfo>();
					
						JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
					
						JsonPrimitive keyPrimitive = jsonObject.getAsJsonPrimitive("Key");
					
						JsonObject valueObject = jsonObject.getAsJsonObject("Record");
						
						
						JsonPrimitive marketPricePrimitive = valueObject.getAsJsonPrimitive("marketPrice");
					
					
						JsonPrimitive quantityPrimitive = valueObject.getAsJsonPrimitive("quantity");

						
						SaleInfo saleInfo = new SaleInfo(keyPrimitive.getAsString(), marketPricePrimitive.getAsString(), quantityPrimitive.getAsString());
						retailObservableList.add(saleInfo);
					
					}
						
				} catch (ProposalException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    private void queryWholeSaleInfo() {
		    	wholeSaleObservableList.clear();
		    	try {
					String queryResult = org2ChainConnector.queryBlockChain("trchwholesale", "queryAllAssetList");
					
					JsonArray jsonArray = new JsonParser().parse(queryResult).getAsJsonArray();
					int jsonArrayLength = jsonArray.size();
					for(int i=0; i<jsonArrayLength; i++) {
						ArrayList<AssetInfo> assetArray = new ArrayList<AssetInfo>();
					
						JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
					
						JsonPrimitive keyPrimitive = jsonObject.getAsJsonPrimitive("Key");
					
						JsonObject valueObject = jsonObject.getAsJsonObject("Record");
						
						
						JsonPrimitive marketPricePrimitive = valueObject.getAsJsonPrimitive("marketPrice");
					
					
						JsonPrimitive quantityPrimitive = valueObject.getAsJsonPrimitive("quantity");

						
						SaleInfo saleInfo = new SaleInfo(keyPrimitive.getAsString(), marketPricePrimitive.getAsString(), quantityPrimitive.getAsString());
						wholeSaleObservableList.add(saleInfo);
					
					}
						
				} catch (ProposalException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	
		    }
		    private void queryretailAssetForKey(String assetName) {
		    	System.out.println("Query Retail Asset:"+assetName);
		    	try {
					String queryResult = org3ChainConnector.hasVariablequeryBlockChain("trchretail", "queryAssetWithKey", new String[] {assetName});
					
					
					JsonObject jsonObject = new JsonParser().parse(queryResult).getAsJsonObject();
					JsonPrimitive marketPricePrimitive = jsonObject.getAsJsonPrimitive("marketPrice");
				
				    JsonPrimitive quantityPrimitive = jsonObject.getAsJsonPrimitive("quantity");
				    retailMarketPrice = marketPricePrimitive.getAsString(); 
					 retailQuantity   =  quantityPrimitive.getAsString();
				
				} catch (ProposalException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    private void queryWholeSaleAssetForKey(String assetName) {
		    	System.out.println("Query Whole Sale Asset:"+assetName);
		    	try {
					String queryResult = org2ChainConnector.hasVariablequeryBlockChain("trchwholesale", "queryAssetWithKey", new String[] {assetName});
					
					
					JsonObject jsonObject = new JsonParser().parse(queryResult).getAsJsonObject();
					JsonPrimitive marketPricePrimitive = jsonObject.getAsJsonPrimitive("marketPrice");
				  
				    JsonPrimitive quantityPrimitive = jsonObject.getAsJsonPrimitive("quantity");
				    wholeSaleMarketPrice = marketPricePrimitive.getAsString(); 
					 wholeSaleQuantity   =  quantityPrimitive.getAsString();
				
				} catch (ProposalException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    private void testDealInfoParsing() {
		    	try {
					JsonArray jsonArray = new JsonParser().parse(new FileReader("./test.json")).getAsJsonArray();
					System.out.println(jsonArray.toString());
					
					int jsonArrayLength = jsonArray.size();
					
					for(int i=0; i<jsonArrayLength; i++) {
				
						JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
					
						JsonPrimitive keyPrimitive = jsonObject.getAsJsonPrimitive("Key");
					
						JsonObject valueObject = jsonObject.getAsJsonObject("Record");
						
						
						JsonPrimitive categoryPrimitive = valueObject.getAsJsonPrimitive("category");
						
						JsonPrimitive sendPrimitive = valueObject.getAsJsonPrimitive("send");
						
						JsonPrimitive receivePrimitive = valueObject.getAsJsonPrimitive("receive");
					
						JsonPrimitive assetPrimitive = valueObject.getAsJsonPrimitive("asset");
					
						JsonPrimitive transactionPrimitive = valueObject.getAsJsonPrimitive("transaction");
					    DealInfo dealInfo = new DealInfo(keyPrimitive.getAsString(), categoryPrimitive.getAsString(), sendPrimitive.getAsString(), receivePrimitive.getAsString(), assetPrimitive.getAsString());
					    dealInfo.setDealtransaction(transactionPrimitive.getAsString());
					    dealInfoObservableList.add(dealInfo);
						//userAssetObservableList.add(userAsset);
						dealMap.put(keyPrimitive.getAsString(), dealInfo);
						
					}
				} catch (JsonIOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonSyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    private void dealInfoListSaveToJson() {
		    	JsonArray jsonArray = new JsonArray();
				
		    	for(int i=0; i<dealInfoObservableList.size(); i++) {
		    		JsonObject jsonObject = new JsonObject();
		    		DealInfo dealInfo = dealInfoObservableList.get(i);
		    		JsonPrimitive keyPrimitive = new JsonPrimitive(dealInfo.getDealName().get());
		    		jsonObject.add("Key", keyPrimitive);
		    		
		    		JsonObject valueObject = new JsonObject();
		    		
		    		JsonPrimitive categoryPrimitive = new JsonPrimitive(dealInfo.getDealType().get());
					
					JsonPrimitive sendPrimitive = new JsonPrimitive(dealInfo.getDealSender().get());
					
					JsonPrimitive receivePrimitive = new JsonPrimitive(dealInfo.getDealReceive().get());
				
					JsonPrimitive assetPrimitive = new JsonPrimitive(dealInfo.getDealAsset().get());
				
					JsonPrimitive transactionPrimitive = new JsonPrimitive(dealInfo.getDealTransaction());
					
					valueObject.add("category", categoryPrimitive);
					valueObject.add("send", sendPrimitive);
					valueObject.add("receive", receivePrimitive);
					valueObject.add("asset", assetPrimitive);
					valueObject.add("transaction", transactionPrimitive);
					jsonObject.add("Record", valueObject);
					
					jsonArray.add(jsonObject);
		    	}
		    	System.out.println(jsonArray);
		    	String jsonArrayText = jsonArray.toString();
		    	try {
		    		FileWriter fw = new FileWriter("test.json");
		    		fw.write(jsonArrayText);
		    		fw.close();
		    	}catch(IOException e) {
		    		e.printStackTrace();
		    	}
		    }
		    private String getCurrentTime(String timeFormat) {
		    	return new SimpleDateFormat(timeFormat).format(System.currentTimeMillis());
		    }
}
