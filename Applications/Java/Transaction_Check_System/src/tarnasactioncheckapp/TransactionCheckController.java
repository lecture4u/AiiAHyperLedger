package tarnasactioncheckapp;

import java.net.URL;
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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

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


public class TransactionCheckController implements Initializable{
	/*-------BlockChain network Connection components----------------*/
	CAConnector caconnector;
	ChainCodeConnector org1ChainConnector;
	ChainCodeConnector org2ChainConnector;
	/*-------User Tab - Hyperledger Fabric Network connection components----------------*/
	@FXML TextField userIDfield;
	@FXML TextField userOrgfield;
	
	@FXML Label org1ConnectionLabel;
	@FXML Label org2ConnectionLabel;
	/*-------User Tab - Distribution Participants status components----------------*/
	@FXML TextField assetNamefield;
	@FXML TextField assetQuantityfield;
	@FXML TableView userListTableView;
	@FXML TableColumn<UserAsset, String> userIDColumn;
	@FXML TableView userAssetStatusTableView;
	@FXML TableColumn<AssetInfo, String> userInfoNameColumn;
	@FXML TableColumn<AssetInfo, String> userInfoValueColumn;
	
	
	/*-------Whole Sale & Retail components----------------*/
	@FXML TableView retailTableView;
	@FXML TableColumn<SaleInfo, String> retailAssetNameColumn;
	@FXML TableColumn<SaleInfo, String> retailMarketPriceColumn;
	@FXML TableColumn<SaleInfo, String> retailQuantityColumn;
	/*-----------History Tab - components----------------*/
	@FXML TextField userAssetHistoryfield;
	@FXML TextField retailAssetHistoryfield;
	@FXML TextArea histroyArea;
	/*-------basic components----------------*/
	private Stage primaryStage;
	private PropReader propreader;
	private ObservableList<UserAsset> userAssetObservableList;
	private ObservableList<AssetInfo> assetInfoObservableList;
	private ObservableList<SaleInfo> retailObservableList;
	Map<String ,UserAsset> userMap = new HashMap<String ,UserAsset>();
	private String currentUser;
	private String retailMarketPrice;
	private String retailQuantity;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		 System.out.println("Transaction Check System start");
	     String propURL = "./config.properties";
			
		 propreader = new PropReader(propURL);
		 propreader.propReader();
		 initTable();
		 System.out.println("TRCR System init variable check");
			System.out.println(propreader.getCaOrg1AdminID());
			System.out.println(propreader.getCaOrg1AdminPass());
			System.out.println(propreader.getCaOrg1URL());
			
			System.out.println(propreader.getCaOrg2AdminID());
			System.out.println(propreader.getCaOrg2AdminPass());
			System.out.println(propreader.getCaOrg2URL());
	
			System.out.println(propreader.getTrncConsumerPeerDomain());
			System.out.println(propreader.getTrncConsumerPeerEventHub());
			System.out.println(propreader.getTrncConsumerPeerURL());
			
			System.out.println(propreader.getTrncRetailPeerDomain());
			System.out.println(propreader.getTrncRetailPeerEventHub());
			System.out.println(propreader.getTrncRetailPeerURL());
			
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
	
	}
	private void initTable() {
		userAssetObservableList = FXCollections.observableArrayList();
		assetInfoObservableList = FXCollections.observableArrayList();
		retailObservableList = FXCollections.observableArrayList();
		userIDColumn.setCellValueFactory(cellData->cellData.getValue().getUserID());
		userInfoNameColumn.setCellValueFactory(cellData->cellData.getValue().getAssetName());
		userInfoValueColumn.setCellValueFactory(cellData->cellData.getValue().getAssetValue());
		
		retailAssetNameColumn.setCellValueFactory(cellData->cellData.getValue().getAssetName());
		retailMarketPriceColumn.setCellValueFactory(cellData->cellData.getValue().getMarketPrice());
		retailQuantityColumn.setCellValueFactory(cellData->cellData.getValue().getQuantity());
		
		userListTableView.setItems(userAssetObservableList);
		userAssetStatusTableView.setItems(assetInfoObservableList);
		retailTableView.setItems(retailObservableList);
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

		    		org1ChainConnector.connectToChannel(propreader.getTrncConsumerPeerDomain(), propreader.getTrncConsumerPeerURL(), propreader.getTrncConsumerPeerEventHub(), "event01");
		    		org1ConnectionLabel.setText("Connected");
		    	
		    	}
		    	else if(userOrgfield.getText().equals("Org2")) {
		    		org2ChainConnector = new ChainCodeConnector();
		    		org2ChainConnector.setChInfo(propreader.getChannelNAME(), propreader.getOrdererDomain(), propreader.getOrdererURL());
		    		org2ChainConnector.loginUser(userIDfield.getText());

		    		org2ChainConnector.connectToChannel(propreader.getTrncRetailPeerDomain(), propreader.getTrncRetailPeerURL(), propreader.getTrncRetailPeerEventHub(), "event02");
		    		org2ConnectionLabel.setText("Connected");
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
					String queryResult = org1ChainConnector.queryBlockChain("trncconsumer", "queryAllUserList");
					
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
		    public void buyingAsset() {
		    	System.out.println("Buying retail asset");
		    	String dealName = "BuyingDeal";
		       dealName = dealName + getCurrentTime("DD HH:mm:ss");
		     	String dealType = "Buying";
		     	String dealTransaction = "";
		     
		    	
		    	
		    	queryretailAssetForKey(assetNamefield.getText());
		    	
		    	UserAsset currentUserInfo = userMap.get(currentUser);
		    	ArrayList<AssetInfo> currentAssetInfo = currentUserInfo.getAssetArray();
		       String currentUserAsset = "";
		       String currentUserMoney = currentAssetInfo.get(1).getAssetValue().get();
		      
		       for(int i=0; i<currentAssetInfo.size(); i++) {
		        	if(currentAssetInfo.get(i).getAssetName().get().equals("has"+assetNamefield.getText())) {
		        		
		        		currentUserAsset =  currentAssetInfo.get(i).getAssetValue().get();
		        	}
		        }
		        
		       System.out.println("Buying User:"+currentUser+"'s Asset:"+assetNamefield.getText()+", "+currentUserAsset);
		       dealTransaction = "Buying User:"+currentUser+"'s Asset:"+assetNamefield.getText()+", "+currentUserAsset+"\n";
		       System.out.println("To retail MarketPrice:"+retailMarketPrice+" ,Quantity:"+retailQuantity);
		       dealTransaction =dealTransaction + "Buying User:"+currentUser+"'s Asset:"+assetNamefield.getText()+", "+currentUserAsset+"\n";
		       try {
		           int userAssetQuantityInt = Integer.parseInt(currentUserAsset);
		           int assetQuantityInt = Integer.parseInt(assetQuantityfield.getText());
		           int resultUserAssetQuantityInt = userAssetQuantityInt + assetQuantityInt;
		           System.out.println("Then Result be User's Asset Quantity:"+currentUserAsset+"+"+assetQuantityfield.getText()+"="+resultUserAssetQuantityInt);
		           dealTransaction =dealTransaction + "Then Result be User's Asset Quantity:"+currentUserAsset+"+"+assetQuantityfield.getText()+"="+resultUserAssetQuantityInt+"\n";
		           
		           int currentUserMoneyInt = Integer.parseInt(currentUserMoney);
		           int retailMarketPriceInt = Integer.parseInt(retailMarketPrice);
		           int resultmarketPrice = retailMarketPriceInt* assetQuantityInt;
		           resultUserAssetQuantityInt = currentUserMoneyInt - resultmarketPrice;
		           System.out.println("               User's Money         :"+currentUserMoneyInt+"-"+retailMarketPrice+"*"+assetQuantityInt+"="+resultUserAssetQuantityInt);
		           dealTransaction =dealTransaction + "               User's Money         :"+currentUserMoneyInt+"-"+retailMarketPrice+"*"+assetQuantityInt+"="+resultUserAssetQuantityInt+"\n";
		           
		           BlockEvent.TransactionEvent event = org1ChainConnector.invokeBlockChain("trncconsumer","buyingAsset",new String[] {currentUser, assetNamefield.getText(),assetQuantityfield.getText(),resultmarketPrice+""})
							.get(1200, TimeUnit.SECONDS);
					if (event.isValid()) {		
						dealTransaction =dealTransaction + "Transaction  tx: " + event.getTransactionID() + "is completed.";		        
						dealTransaction =dealTransaction + "Transaction  timestamp: " + event.getTimestamp();
					} else {			
						dealTransaction =dealTransaction + "Transaction  tx: " + event.getTransactionID() + "is invalid.";				
						dealTransaction =dealTransaction + "Transaction  timestamp: " + event.getTimestamp();
					}
					
		           int retailAssetQuantityInt = Integer.parseInt(retailQuantity);
		          
		           resultUserAssetQuantityInt = retailAssetQuantityInt - assetQuantityInt;
		           System.out.println("               Wholesale's Asset Quantity:"+retailQuantity+"-"+assetQuantityfield.getText()+"="+resultUserAssetQuantityInt);
		           dealTransaction =dealTransaction + "               Wholesale's Asset Quantity:"+retailQuantity+"-"+assetQuantityfield.getText()+"="+resultUserAssetQuantityInt;
		          event = org2ChainConnector.invokeBlockChain("trncretail","decreaseQuantity",new String[] {assetNamefield.getText(),assetQuantityfield.getText()})
							.get(1200, TimeUnit.SECONDS);
					if (event.isValid()) {				
						dealTransaction =dealTransaction + "Transaction  tx: " + event.getTransactionID() + "is completed.";			
						dealTransaction =dealTransaction + "Transaction  timestamp: " + event.getTimestamp();
					} else {			
						dealTransaction =dealTransaction + "Transaction  tx: " + event.getTransactionID() + "is invalid.";					
						dealTransaction =dealTransaction + "Transaction  timestamp: " + event.getTimestamp();
					}
					
					
					Thread.sleep(1000);
					
		           queryParticipants();
		           queryRetailInfo();
		           System.out.println(dealTransaction);
		          // queryWholeSaleAssetForKey(assetNamefield.getText());
		          // distributionAsset();
		       }catch(Exception e) {
		    	   e.printStackTrace();
		       }
		    }
		    /*History methods*/
		    @FXML
		    public void queryUserHistory() {
		    	System.out.println("Query Consumer Transaction History");
		    	String historyText = "";
		    	histroyArea.setText("");
		    	String[] variables = new String[]{userAssetHistoryfield.getText()};
		    	try {
					String queryResult = org1ChainConnector.hasVariablequeryBlockChain("trncconsumer", "userInfoHistory",variables);
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
		    public void queryRetailrHistory() {
		    	System.out.println("Query Retail Transaction History");
		    	String historyText = "";
		    	histroyArea.setText("");
		    	String[] variables = new String[]{retailAssetHistoryfield.getText()};
		    	try {
					String queryResult = org2ChainConnector.hasVariablequeryBlockChain("trncretail", "assetInfoHistory",variables);
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
           private void queryRetailInfo() {
		    	
		    	retailObservableList.clear();
		    	try {
					String queryResult = org2ChainConnector.queryBlockChain("trncretail", "queryAllAssetList");
					
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
		    private void queryretailAssetForKey(String assetName) {
		    	System.out.println("Query Retail Asset:"+assetName);
		    	try {
					String queryResult = org2ChainConnector.hasVariablequeryBlockChain("trncretail", "queryAssetWithKey", new String[] {assetName});
					
					
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
		    private String getCurrentTime(String timeFormat) {
		    	return new SimpleDateFormat(timeFormat).format(System.currentTimeMillis());
		    }
}
