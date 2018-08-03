package hyperledger.fabric.javasdk;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;

public class CAConnector {
	private HFCAClient caClient;
	private AppUser admin;
	private AppUser user;
	public CAConnector(String connectURL) {
		try {
			caClient = getHfCaClient(connectURL, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public CAConnector() {

	}
	public void issuAdmin(String adminID, String adminPW) {
		try {
			admin = getAdmin(caClient,adminID,adminPW);
		} catch (Exception e) {
			System.out.println("admin account issue failed");
			e.printStackTrace();
		}
	}
	public void registerUser(String userID) {
		try {
			user = getUser(caClient,admin,userID);
		} catch (Exception e) {
			System.out.println("admin account issue failed");
			e.printStackTrace();
		}
	}
	private HFCAClient getHfCaClient(String caUrl, Properties caClientProperties) throws Exception {
		CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
		HFCAClient caClient = HFCAClient.createNewInstance(caUrl, caClientProperties);
		caClient.setCryptoSuite(cryptoSuite);
		return caClient;
	}
	private AppUser getAdmin(HFCAClient caClient, String adminID, String adminPW) throws Exception {
		AppUser admin = tryDeserialize("admin");
		if (admin == null) {
			Enrollment adminEnrollment = caClient.enroll(adminID, adminPW);
			admin = new AppUser("admin", "org1", "Org1MSP", adminEnrollment);
			serialize(admin);
		}
		return admin;
	}
	private AppUser getUser(HFCAClient caClient, AppUser registrar, String userId) throws Exception {
		AppUser appUser = tryDeserialize(userId);
		if (appUser == null) {
			RegistrationRequest rr = new RegistrationRequest(userId, "org1");
			String enrollmentSecret = caClient.register(rr, registrar);
			Enrollment enrollment = caClient.enroll(userId, enrollmentSecret);
			appUser = new AppUser(userId, "org1", "Org1MSP", enrollment);
			serialize(appUser);
		}
		return appUser;
	}
	
	private void serialize(AppUser appUser) throws IOException {
		try (ObjectOutputStream oos = new ObjectOutputStream(
				Files.newOutputStream(Paths.get(appUser.getName() + ".jso")))) {
			oos.writeObject(appUser);
		}
	}

	public AppUser tryDeserialize(String name) throws Exception {
		if (Files.exists(Paths.get(name + ".jso"))) {
			return deserialize(name);
		}
		return null;
	}
	public AppUser deserialize(String name) throws Exception {
		try (ObjectInputStream decoder = new ObjectInputStream(Files.newInputStream(Paths.get(name + ".jso")))) {
			return (AppUser) decoder.readObject();
		}
	}
}
