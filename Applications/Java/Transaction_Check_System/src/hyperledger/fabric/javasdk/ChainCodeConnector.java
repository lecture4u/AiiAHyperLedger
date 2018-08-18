package hyperledger.fabric.javasdk;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.EventHub;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.QueryByChaincodeRequest;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

public class ChainCodeConnector {
	HFClient client;
	CAConnector cac = new CAConnector();
	Channel channel;
	
	String channelName;
	String ordererURL;
	String ordererDomain;
	public ChainCodeConnector() {
		try {
			client = getHfClient();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	public void setChInfo(String channelName, String ordererDomain, String ordererURL) {
		this.channelName = channelName;
		this.ordererDomain = ordererDomain;
		this.ordererURL = ordererURL;
	}
	public void  loginUser(String userID) {
		try {
			client.setUserContext(cac.tryDeserialize(userID));
		} catch (InvalidArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void connectToChannel(String peerURL, String peerAddress, String peerEventHubURL, String eventHubName) {
		try {
			Channel channel = getChannel(client, peerURL, peerAddress,peerEventHubURL, eventHubName);
		} catch (InvalidArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private  HFClient getHfClient() throws Exception {
		// initialize default cryptosuite
		CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
		// setup the client
		HFClient client = HFClient.createNewInstance();
		client.setCryptoSuite(cryptoSuite);
		return client;
	}
	private Channel getChannel(HFClient client, String peerName, String peerAddress, String peerEventHubURL, String eventHubName)
			throws InvalidArgumentException, TransactionException {
		// initialize channel
		// peer name and endpoint in fabcar network
		Peer peer = client.newPeer(peerName, peerAddress);
		// eventhub name and endpoint in fabcar network
		EventHub eventHub = client.newEventHub(eventHubName, peerEventHubURL);
		// orderer name and endpoint in fabcar network
		Orderer orderer = client.newOrderer(ordererDomain, ordererURL);
		// channel name in fabcar network
		Channel channel = client.newChannel(channelName);
		channel.addPeer(peer);
		channel.addEventHub(eventHub);
		channel.addOrderer(orderer);
		channel.initialize();
		return channel;
	}
	public String queryBlockChain(String chaincodeName, String functionName)
			throws ProposalException, InvalidArgumentException {
		String stringResponse="";
		// get channel instance from client
		Channel channel = client.getChannel(channelName);
		// create chaincode request
		QueryByChaincodeRequest qpr = client.newQueryProposalRequest();
		// build cc id providing the chaincode name. Version is omitted here.
		ChaincodeID fabcarCCId = ChaincodeID.newBuilder().setName(chaincodeName).build();
		qpr.setChaincodeID(fabcarCCId);
		// CC function to be called
		qpr.setFcn(functionName);
		//qpr.setArgs(args);
		Collection<ProposalResponse> res = channel.queryByChaincode(qpr);
		// display response
		for (ProposalResponse pres : res) {
			stringResponse = new String(pres.getChaincodeActionResponsePayload());
			System.out.println(stringResponse);
			// log.info(stringResponse);
		}
		return stringResponse;
	}
	public String hasVariablequeryBlockChain(String chaincodeName, String functionName, String[] variable)
			throws ProposalException, InvalidArgumentException {
		String stringResponse="";
		// get channel instance from client
		Channel channel = client.getChannel(channelName);
		// create chaincode request
		QueryByChaincodeRequest qpr = client.newQueryProposalRequest();
		// build cc id providing the chaincode name. Version is omitted here.
		ChaincodeID fabcarCCId = ChaincodeID.newBuilder().setName(chaincodeName).build();
		qpr.setChaincodeID(fabcarCCId);
		// CC function to be called
		qpr.setFcn(functionName);
		qpr.setArgs(variable);
		Collection<ProposalResponse> res = channel.queryByChaincode(qpr);
		// display response
		for (ProposalResponse pres : res) {
			stringResponse = new String(pres.getChaincodeActionResponsePayload());
			System.out.println(stringResponse);
			// log.info(stringResponse);
		}
		return stringResponse;
	}
	public CompletableFuture<BlockEvent.TransactionEvent> invokeBlockChain(String chaincodeName,
			String functionName, String[] args) throws ProposalException, InvalidArgumentException {
		// get channel instance from client
		Channel channel = client.getChannel(channelName);

		// create chaincode request
		TransactionProposalRequest Request = client.newTransactionProposalRequest();

		// build cc id providing the chaincode name. Version is omitted here.
		ChaincodeID CCId = ChaincodeID.newBuilder().setName(chaincodeName).build();
		Request.setChaincodeID(CCId);

		// CC function to be called
		Request.setFcn(functionName);
		Request.setArgs(args);

		Collection<ProposalResponse> responses = channel.sendTransactionProposal(Request);
		List<ProposalResponse> invalid = responses.stream().filter(r -> r.isInvalid()).collect(Collectors.toList());
		if (!invalid.isEmpty()) {
			invalid.forEach(response -> {
				System.out.println(response.getMessage());
			});
			throw new RuntimeException("invalid response(s) found");
		}

		return channel.sendTransaction(responses);

	}
}
