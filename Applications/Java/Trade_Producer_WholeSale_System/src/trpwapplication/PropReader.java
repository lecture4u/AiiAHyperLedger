package trpwapplication;

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
    
    private String caOrg3URL;
    private String caOrg3AdminID;
    private String caOrg3AdminPass;
    
    private String trchProviderPeerDomain;
    private String trchProviderPeerURL;
    private String trchProviderPeerEventHub;
    
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
    		
    		
    		setCaOrg1URL(props.getProperty("CAOrg1URL"));
    	    setCaOrg1AdminID(props.getProperty("CAOrg1AdminID"));
    	    setCaOrg1AdminPass(props.getProperty("CAOrg1AdminPass"));
    	    
    	    setCaOrg2URL(props.getProperty("CAOrg2URL"));
    	    setCaOrg2AdminID(props.getProperty("CAOrg2AdminID"));
    	    setCaOrg2AdminPass(props.getProperty("CAOrg2AdminPass"));
    	    
    	    setCaOrg3URL(props.getProperty("CAOrg3URL"));
    	    setCaOrg3AdminID(props.getProperty("CAOrg3AdminID"));
    	    setCaOrg3AdminPass(props.getProperty("CAOrg3AdminPass"));
    	    
    	    setTrchProviderPeerDomain(props.getProperty("TRCHProviderPeerDomain"));
    	    setTrchProviderPeerURL(props.getProperty("TRCHProviderPeerURL"));
    	    setTrchProviderPeerEventHub(props.getProperty("TRCHProviderPeerEventHub"));
    	    
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

	public String getTrchProviderPeerDomain() {
		return trchProviderPeerDomain;
	}

	public void setTrchProviderPeerDomain(String trchProviderPeerDomain) {
		this.trchProviderPeerDomain = trchProviderPeerDomain;
	}

	public String getTrchProviderPeerURL() {
		return trchProviderPeerURL;
	}

	public void setTrchProviderPeerURL(String trchProviderPeerURL) {
		this.trchProviderPeerURL = trchProviderPeerURL;
	}

	public String getTrchProviderPeerEventHub() {
		return trchProviderPeerEventHub;
	}

	public void setTrchProviderPeerEventHub(String trchProviderPeerEventHub) {
		this.trchProviderPeerEventHub = trchProviderPeerEventHub;
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
