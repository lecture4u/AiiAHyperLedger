package trcrapplication;

import java.io.FileInputStream;
import java.util.Properties;

public class PropReader {
	private String propURL;
	
    private String caOrg4URL;
    private String caOrg4AdminID;
    private String caOrg4AdminPass;
    
    private String caOrg2URL;
    private String caOrg2AdminID;
    private String caOrg2AdminPass;
    
    private String caOrg3URL;
    private String caOrg3AdminID;
    private String caOrg3AdminPass;
    
    private String trchConsumerPeerDomain;
    private String trchConsumerPeerURL;
    private String trchConsumerPeerEventHub;
    
    private String trchWholeSalePeerDomain;
    private String trchWholeSalePeerURL;
    private String trchWholeSalePeerEventHub;
    
    private String trchRetailPeerDomain;
    private String trchRetailPeerURL;
    private String trchRetailPeerEventHub;
    
    private String ordererURL;
    private String ordererDomain;
    
    private String channelNAME;
    private String channelNAME2;
    public PropReader(String propURL) {
    	this.propURL = propURL;
    }
    public void propReader() {
    	try {
    		String propFile = propURL;
    		Properties props = new Properties();
    		
    		FileInputStream fis = new FileInputStream(propFile);
    		props.load(new java.io.BufferedInputStream(fis));
    		
    		
    		 setCaOrg4URL(props.getProperty("CAOrg4URL"));
    	    setCaOrg4AdminID(props.getProperty("CAOrg4AdminID"));
    	    setCaOrg4AdminPass(props.getProperty("CAOrg4AdminPass"));
    	    
    	    setCaOrg2URL(props.getProperty("CAOrg2URL"));
    	    setCaOrg2AdminID(props.getProperty("CAOrg2AdminID"));
    	    setCaOrg2AdminPass(props.getProperty("CAOrg2AdminPass"));
    	    
    	    setCaOrg3URL(props.getProperty("CAOrg3URL"));
    	    setCaOrg3AdminID(props.getProperty("CAOrg3AdminID"));
    	    setCaOrg3AdminPass(props.getProperty("CAOrg3AdminPass"));
    	    
    	    setTrchConsumerPeerDomain(props.getProperty("TRCHConsumerPeerDomain"));
    	    setTrchConsumerPeerURL(props.getProperty("TRCHConsumerPeerURL"));
    	    setTrchConsumerPeerEventHub(props.getProperty("TRCHConsumerPeerEventHub"));
    	    
    	    setTrchWholeSalePeerDomain(props.getProperty("TRCHWholeSalePeerDomain"));
    	    setTrchWholeSalePeerURL(props.getProperty("TRCHWholeSalePeerURL"));
    	    setTrchWholeSalePeerEventHub(props.getProperty("TRCHWholeSalePeerEventHub"));
    	    
    	    setTrchRetailPeerDomain(props.getProperty("TRCHRetailPeerDomain"));
    	    setTrchRetailPeerURL(props.getProperty("TRCHRetailPeerURL"));
    	    setTrchRetailPeerEventHub(props.getProperty("TRCHRetailPeerEventHub"));
    	    
    	    setOrdererURL(props.getProperty("OrdererURL"));
    	    setOrdererDomain(props.getProperty("OrdererDomain"));
    	    
    	    setChannelNAME(props.getProperty("CHANNELNAME"));
    	    setChannelNAME2(props.getProperty("CHANNELNAME2"));
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }

	public String getCaOrg4URL() {
		return caOrg4URL;
	}

	public void setCaOrg4URL(String caOrg4URL) {
		this.caOrg4URL = caOrg4URL;
	}

	public String getCaOrg4AdminID() {
		return caOrg4AdminID;
	}

	public void setCaOrg4AdminID(String caOrg4AdminID) {
		this.caOrg4AdminID = caOrg4AdminID;
	}

	public String getCaOrg4AdminPass() {
		return caOrg4AdminPass;
	}

	public void setCaOrg4AdminPass(String caOrg4AdminPass) {
		this.caOrg4AdminPass = caOrg4AdminPass;
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

	public String getCaOrg3URL() {
		return caOrg3URL;
	}

	public void setCaOrg3URL(String caOrg3URL) {
		this.caOrg3URL = caOrg3URL;
	}

	public String getCaOrg3AdminID() {
		return caOrg3AdminID;
	}

	public void setCaOrg3AdminID(String caOrg3AdminID) {
		this.caOrg3AdminID = caOrg3AdminID;
	}

	public String getCaOrg3AdminPass() {
		return caOrg3AdminPass;
	}

	public void setCaOrg3AdminPass(String caOrg3AdminPass) {
		this.caOrg3AdminPass = caOrg3AdminPass;
	}

	public String getTrchConsumerPeerDomain() {
		return trchConsumerPeerDomain;
	}

	public void setTrchConsumerPeerDomain(String trchUserPeerDomain) {
		this.trchConsumerPeerDomain = trchUserPeerDomain;
	}

	public String getTrchConsumerPeerURL() {
		return trchConsumerPeerURL;
	}

	public void setTrchConsumerPeerURL(String trchUserPeerURL) {
		this.trchConsumerPeerURL = trchUserPeerURL;
	}

	public String getTrchConsumerPeerEventHub() {
		return trchConsumerPeerEventHub;
	}

	public void setTrchConsumerPeerEventHub(String trchUserPeerEventHub) {
		this.trchConsumerPeerEventHub = trchUserPeerEventHub;
	}

	public String getTrchWholeSalePeerDomain() {
		return trchWholeSalePeerDomain;
	}

	public void setTrchWholeSalePeerDomain(String trchWholeSalePeerDomain) {
		this.trchWholeSalePeerDomain = trchWholeSalePeerDomain;
	}

	public String getTrchWholeSalePeerURL() {
		return trchWholeSalePeerURL;
	}

	public void setTrchWholeSalePeerURL(String trchWholeSalePeerURL) {
		this.trchWholeSalePeerURL = trchWholeSalePeerURL;
	}

	public String getTrchWholeSalePeerEventHub() {
		return trchWholeSalePeerEventHub;
	}

	public void setTrchWholeSalePeerEventHub(String trchWholeSalePeerEventHub) {
		this.trchWholeSalePeerEventHub = trchWholeSalePeerEventHub;
	}

	public String getTrchRetailPeerDomain() {
		return trchRetailPeerDomain;
	}

	public void setTrchRetailPeerDomain(String trchRetailPeerDomain) {
		this.trchRetailPeerDomain = trchRetailPeerDomain;
	}

	public String getTrchRetailPeerURL() {
		return trchRetailPeerURL;
	}

	public void setTrchRetailPeerURL(String trchRetailPeerURL) {
		this.trchRetailPeerURL = trchRetailPeerURL;
	}

	public String getTrchRetailPeerEventHub() {
		return trchRetailPeerEventHub;
	}

	public void setTrchRetailPeerEventHub(String trchRetailPeerEventHub) {
		this.trchRetailPeerEventHub = trchRetailPeerEventHub;
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
	public String getChannelNAME2() {
		return channelNAME2;
	}
	public void setChannelNAME2(String channelNAME2) {
		this.channelNAME2 = channelNAME2;
	}
    
}
