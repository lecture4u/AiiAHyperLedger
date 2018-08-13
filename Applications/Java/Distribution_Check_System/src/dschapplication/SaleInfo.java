package dschapplication;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SaleInfo {
	private StringProperty assetName;
	private StringProperty marketPrice;
	private StringProperty quantity;
	
	public SaleInfo(String assetName, String marketPrice, String quantity) {
		 this.assetName = new SimpleStringProperty(assetName);
		 this.marketPrice = new SimpleStringProperty(marketPrice);
		 this.quantity = new SimpleStringProperty(quantity);
	}

	public StringProperty getAssetName() {
		return assetName;
	}

	public void setAssetName(StringProperty assetName) {
		this.assetName = assetName;
	}

	public StringProperty getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(StringProperty marketPrice) {
		this.marketPrice = marketPrice;
	}

	public StringProperty getQuantity() {
		return quantity;
	}

	public void setQuantity(StringProperty quantity) {
		this.quantity = quantity;
	}
}
