package trpwapplication;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserAsset {
	private StringProperty userID;
	private ArrayList<AssetInfo> assetArray;
	
	public UserAsset(String userID, ArrayList<AssetInfo> assetArray) {
		this.userID = new SimpleStringProperty(userID);
		this.assetArray = assetArray;

	}

	public StringProperty getUserID() {
		return userID;
	}

	public void setUserID(StringProperty userID) {
		this.userID = userID;
	}
	public ArrayList<AssetInfo> getAssetArray(){
		return assetArray;
	}
}
