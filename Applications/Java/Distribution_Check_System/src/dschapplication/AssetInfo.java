package dschapplication;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AssetInfo {
	private StringProperty assetName;
	private StringProperty assetValue;
   public AssetInfo(String assetName, String assetValue) {
	   this.assetName = new SimpleStringProperty(assetName);
	   this.assetValue = new SimpleStringProperty(assetValue);
   }
   public StringProperty getAssetName() {
	   return assetName;
   }
   public void setAssetName(StringProperty assetName) {
	   this.assetName = assetName;
   }
   public StringProperty getAssetValue() {
	   return assetValue;
   }
   public void setAssetValue(StringProperty assetValue) {
	   this.assetValue = assetValue;
   }
}
