package dschapplication;

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
    
    private String dschUserPeerDomain;
    private String dschUserPeerURL;
    private String dschUserPeerEventHub;
    
    private String dschWholeSalePeerDomain;
    private String dschWholeSalePeerURL;
    private String dschWholeSalePeerEventHub;
    
    private String dschLocalSalePeerDomain;
    private String dschLocalSalePeerURL;
    private String dschLocalSalePeerEventHub;
    
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
    	    
    	    setCaOrg3URL(props.getProperty("CAOrg3URL"));
    	    setCaOrg3AdminID(props.getProperty("CAOrg3AdminID"));
    	    setCaOrg3AdminPass(props.getProperty("CAOrg3AdminPass"));
    	    
    	    setDschUserPeerDomain(props.getProperty("DSCHUserPeerDomain"));
    	    setDschUserPeerURL(props.getProperty("DSCHUserPeerURL"));
    	    setDschUserPeerEventHub(props.getProperty("DSCHUserPeerEventHub"));
    	    
    	    setDschWholeSalePeerDomain(props.getProperty("DSCHWholeSalePeerDomain"));
    	    setDschWholeSalePeerURL(props.getProperty("DSCHWholeSalePeerURL"));
    	    setDschWholeSalePeerEventHub(props.getProperty("DSCHWholeSalePeerEventHub"));
    	    
    	    setDschLocalSalePeerDomain(props.getProperty("DSCHLocalSalePeerDomain"));
    	    setDschLocalSalePeerURL(props.getProperty("DSCHLocalSalePeerURL"));
    	    setDschLocalSalePeerEventHub(props.getProperty("DSCHLocalSalePeerEventHub"));
    	    
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

	public String getDschUserPeerDomain() {
		return dschUserPeerDomain;
	}

	public void setDschUserPeerDomain(String dschUserPeerDomain) {
		this.dschUserPeerDomain = dschUserPeerDomain;
	}

	public String getDschUserPeerURL() {
		return dschUserPeerURL;
	}

	public void setDschUserPeerURL(String dschUserPeerURL) {
		this.dschUserPeerURL = dschUserPeerURL;
	}

	public String getDschUserPeerEventHub() {
		return dschUserPeerEventHub;
	}

	public void setDschUserPeerEventHub(String dschUserPeerEventHub) {
		this.dschUserPeerEventHub = dschUserPeerEventHub;
	}

	public String getDschWholeSalePeerDomain() {
		return dschWholeSalePeerDomain;
	}

	public void setDschWholeSalePeerDomain(String dschWholeSalePeerDomain) {
		this.dschWholeSalePeerDomain = dschWholeSalePeerDomain;
	}

	public String getDschWholeSalePeerURL() {
		return dschWholeSalePeerURL;
	}

	public void setDschWholeSalePeerURL(String dschWholeSalePeerURL) {
		this.dschWholeSalePeerURL = dschWholeSalePeerURL;
	}

	public String getDschWholeSalePeerEventHub() {
		return dschWholeSalePeerEventHub;
	}

	public void setDschWholeSalePeerEventHub(String dschWholeSalePeerEventHub) {
		this.dschWholeSalePeerEventHub = dschWholeSalePeerEventHub;
	}

	public String getDschLocalSalePeerDomain() {
		return dschLocalSalePeerDomain;
	}

	public void setDschLocalSalePeerDomain(String dschLocalSalePeerDomain) {
		this.dschLocalSalePeerDomain = dschLocalSalePeerDomain;
	}

	public String getDschLocalSalePeerURL() {
		return dschLocalSalePeerURL;
	}

	public void setDschLocalSalePeerURL(String dschLocalSalePeerURL) {
		this.dschLocalSalePeerURL = dschLocalSalePeerURL;
	}

	public String getDschLocalSalePeerEventHub() {
		return dschLocalSalePeerEventHub;
	}

	public void setDschLocalSalePeerEventHub(String dschLocalSalePeerEventHub) {
		this.dschLocalSalePeerEventHub = dschLocalSalePeerEventHub;
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
    
}
