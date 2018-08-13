package dschapplication;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class DealInfo {
	private StringProperty dealName;
	private StringProperty dealType;
	private StringProperty dealSender;
	private StringProperty dealReceive;
	private StringProperty dealAsset;
	private String dealTransaction;
	
	public DealInfo(String dealName, String dealType, String dealSender, String dealReceive,String dealAsset){
		this.dealName = new SimpleStringProperty(dealName);
		this.dealType = new SimpleStringProperty(dealType);
		this.dealSender = new SimpleStringProperty(dealSender);
		this.dealReceive = new SimpleStringProperty(dealReceive);
		this.dealAsset = new SimpleStringProperty(dealAsset);
	}
    public void setDealtransaction(String dealTransaction) {
    	this.dealTransaction = dealTransaction;
    }
    public String getDealtransaction() {
    	return dealTransaction;
    }
	public StringProperty getDealType() {
		return dealType;
	}

	public void setDealType(StringProperty dealType) {
		this.dealType = dealType;
	}

	public StringProperty getDealSender() {
		return dealSender;
	}

	public void setDealSender(StringProperty dealSender) {
		this.dealSender = dealSender;
	}

	public StringProperty getDealReceive() {
		return dealReceive;
	}

	public void setDealReceive(StringProperty dealReceive) {
		this.dealReceive = dealReceive;
	}

	public StringProperty getDealAsset() {
		return dealAsset;
	}

	public void setDealAsset(StringProperty dealAsset) {
		this.dealAsset = dealAsset;
	}

	public String getDealTransaction() {
		return dealTransaction;
	}

	public void setDealTransaction(String dealTransaction) {
		this.dealTransaction = dealTransaction;
	}
	public StringProperty getDealName() {
		return dealName;
	}
	public void setDealName(StringProperty dealName) {
		this.dealName = dealName;
	}
		
}