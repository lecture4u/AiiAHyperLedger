package tarnasactioncheckapp;

import java.io.FileInputStream;
import java.util.Properties;

public class PropReader {
	private String propURL;
	
    private String caOrg1URL;
    private String caOrg1AdminID;
    private String caOrg1AdminPass;
    
    private String caOrg2URL;
    private String caOrg2AdminID;
    private String caOrg2AdminPass;
    
    
    private String trncConsumerPeerDomain;
    private String trncConsumerPeerURL;
    private String trncConsumerPeerEventHub;
    
    private String trncRetailPeerDomain;
    private String trncRetailPeerURL;
    private String trncRetailPeerEventHub;
    
    private String ordererURL;
    private String ordererDomain;
    
    private String channelNAME;
    public PropReader(String propURL) {
    	this.propURL = propURL;
    }
    public void propReader() {
    	try {
    		String propFile = propURL;
    		Properties props = new Properties();
    		
    		FileInputStream fis = new FileInputStream(propFile);
    		props.load(new java.io.BufferedInputStream(fis));
    		
    		
    		setCaOrg1URL(props.getProperty("CAOrg1URL"));
    	    setCaOrg1AdminID(props.getProperty("CAOrg1AdminID"));
    	    setCaOrg1AdminPass(props.getProperty("CAOrg1AdminPass"));
    	    
    	    setCaOrg2URL(props.getProperty("CAOrg2URL"));
    	    setCaOrg2AdminID(props.getProperty("CAOrg2AdminID"));
    	    setCaOrg2AdminPass(props.getProperty("CAOrg2AdminPass"));
    	     
    	    setTrncConsumerPeerDomain(props.getProperty("TRNCConsumerPeerDomain"));
    	    setTrncConsumerPeerURL(props.getProperty("TRNCConsumerPeerURL"));
    	    setTrncConsumerPeerEventHub(props.getProperty("TRNCConsumerPeerEventHub"));
    	     	    
    	    setTrncRetailPeerDomain(props.getProperty("TRNCRetailPeerDomain"));
    	    setTrncRetailPeerURL(props.getProperty("TRNCRetailPeerURL"));
    	    setTrncRetailPeerEventHub(props.getProperty("TRNCRetailPeerEventHub"));
    	    
    	    setOrdererURL(props.getProperty("OrdererURL"));
    	    setOrdererDomain(props.getProperty("OrdererDomain"));
    	    
    	    setChannelNAME(props.getProperty("CHANNELNAME"));
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }

	public String getCaOrg1URL() {
		return caOrg1URL;
	}

	public void setCaOrg1URL(String caOrg1URL) {
		this.caOrg1URL = caOrg1URL;
	}

	public String getCaOrg1AdminID() {
		return caOrg1AdminID;
	}

	public void setCaOrg1AdminID(String caOrg1AdminID) {
		this.caOrg1AdminID = caOrg1AdminID;
	}

	public String getCaOrg1AdminPass() {
		return caOrg1AdminPass;
	}

	public void setCaOrg1AdminPass(String caOrg1AdminPass) {
		this.caOrg1AdminPass = caOrg1AdminPass;
	}

	public String getCaOrg2URL() {
		return caOrg2URL;
	}

	public void setCaOrg2URL(String caOrg2URL) {
		this.caOrg2URL = caOrg2URL;
	}

	public String getCaOrg2AdminID() {
		return caOrg2AdminID;
	}

	public void setCaOrg2AdminID(String caOrg2AdminID) {
		this.caOrg2AdminID = caOrg2AdminID;
	}

	public String getCaOrg2AdminPass() {
		return caOrg2AdminPass;
	}

	public void setCaOrg2AdminPass(String caOrg2AdminPass) {
		this.caOrg2AdminPass = caOrg2AdminPass;
	}

	public String getOrdererURL() {
		return ordererURL;
	}

	public void setOrdererURL(String ordererURL) {
		this.ordererURL = ordererURL;
	}

	public String getOrdererDomain() {
		return ordererDomain;
	}

	public void setOrdererDomain(String ordererDomain) {
		this.ordererDomain = ordererDomain;
	}
	public String getChannelNAME() {
		return channelNAME;
	}
	public void setChannelNAME(String channelNAME) {
		this.channelNAME = channelNAME;
	}
	public String getTrncConsumerPeerDomain() {
		return trncConsumerPeerDomain;
	}
	public void setTrncConsumerPeerDomain(String trncConsumerPeerDomain) {
		this.trncConsumerPeerDomain = trncConsumerPeerDomain;
	}
	public String getTrncConsumerPeerURL() {
		return trncConsumerPeerURL;
	}
	public void setTrncConsumerPeerURL(String trncConsumerPeerURL) {
		this.trncConsumerPeerURL = trncConsumerPeerURL;
	}
	public String getTrncConsumerPeerEventHub() {
		return trncConsumerPeerEventHub;
	}
	public void setTrncConsumerPeerEventHub(String trncConsumerPeerEventHub) {
		this.trncConsumerPeerEventHub = trncConsumerPeerEventHub;
	}
	public String getTrncRetailPeerDomain() {
		return trncRetailPeerDomain;
	}
	public void setTrncRetailPeerDomain(String trncRetailPeerDomain) {
		this.trncRetailPeerDomain = trncRetailPeerDomain;
	}
	public String getTrncRetailPeerURL() {
		return trncRetailPeerURL;
	}
	public void setTrncRetailPeerURL(String trncRetailPeerURL) {
		this.trncRetailPeerURL = trncRetailPeerURL;
	}
	public String getTrncRetailPeerEventHub() {
		return trncRetailPeerEventHub;
	}
	public void setTrncRetailPeerEventHub(String trncRetailPeerEventHub) {
		this.trncRetailPeerEventHub = trncRetailPeerEventHub;
	}
    
}
