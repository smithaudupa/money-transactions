package mainClient;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;


import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.h2.tools.RunScript;

import moneytransferMain.connection.ConnectionFactory;
import moneytransferMain.model.Account;
import moneytransferMain.model.Transactions;
/**
 * Main class written to test the Account Service class.
 * @author smitha
 *
 */
public class TestClass {
/**
 * Main Method.
 * @param args
 * @throws ClassNotFoundException
 * @throws SQLException
 * @throws IOException
 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		// TODO Auto-generated method stub
			populateTestData();
		Client client = ClientBuilder.newClient();
		//URI to test Service
        WebTarget target = client.target("http://localhost:8081/MoneyTransaction/restService");
        // to get all accounts
        getAllAccounts(target);
        // to get account details by id
        getAccountById(target);
        //resourceWebTarget = target.path("AccountService").path("101");
        // to create an account
        createAccount(target);
     // withdraw amount from account
        withdrawMoney(target);
     // deposit amount to account
        depositAmount(target);
     // transfer Money Betwn Accounts
        transferMoneyBetwnAccounts(target);
}
	/**
	 * Client method to test money transfer between accounts.
	 * @param target
	 */
	private static void transferMoneyBetwnAccounts(WebTarget target) {
	// TODO Auto-generated method stub
		WebTarget resourceWebTarget;
        Invocation.Builder invocationBuilder;
        resourceWebTarget = target.path("transaction");
        invocationBuilder = resourceWebTarget.request(
                MediaType.APPLICATION_JSON);
        BigDecimal deltaAmount = new BigDecimal(100).setScale(4, RoundingMode.HALF_EVEN);
        Transactions transaction = new Transactions(deltaAmount, 101, 222);
        Response response = invocationBuilder.post(Entity.entity(transaction, MediaType.APPLICATION_JSON));
        transaction = response.readEntity(Transactions.class);
        System.out.println("Transfer money Response Status:" + response.getStatus());
        getAllAccounts(target);
}
	/**
	 * Client Method to test service method which gets info of particular Account ID.
	 * @param target
	 */
	private static void getAccountById(WebTarget target) {
		// TODO Auto-generated method stub
		WebTarget resourceWebTarget;
        Invocation.Builder invocationBuilder;
        resourceWebTarget = target.path("AccountService").path("101");
        invocationBuilder = resourceWebTarget.request(
                MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        System.out.println("Get Account By Id Response Status:" + response.getStatus());
        System.out.println("Response" + response.readEntity(String.class));
	}
	/**
	 * Client Method to test service method used to withdraw money.
	 * @param target
	 */
	private static void withdrawMoney(WebTarget target) {
		// TODO Auto-generated method stub
		WebTarget resourceWebTarget;
        Invocation.Builder invocationBuilder;
		resourceWebTarget = target.path("AccountService").path("101").path("withdraw").path("22");
        invocationBuilder = resourceWebTarget.request(
                MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.put(Entity.entity("101,200", MediaType.APPLICATION_JSON));
        System.out.println("Withdraw money Response Status:" + response.getStatus());
        System.out.println("Response" + response.readEntity(String.class));
	}
	/**
	 * Client Method to test deposit money functionality in service method.
	 * @param target
	 */
	private static void depositAmount(WebTarget target) {
		// TODO Auto-generated method stub
		WebTarget resourceWebTarget;
        Invocation.Builder invocationBuilder;
        resourceWebTarget = target.path("AccountService").path("101").path("deposit").path("277");
        invocationBuilder = resourceWebTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.put(Entity.entity("101, 277", MediaType.APPLICATION_JSON));
        System.out.println("Deposit money Response Status:" + response.getStatus());
        System.out.println("Response" + response.readEntity(String.class));
	}
	/**
	 * Client Method to test create method in Account service class.
	 * @param target
	 */
	private static void createAccount(WebTarget target) {
		// TODO Auto-generated method stub
		WebTarget resourceWebTarget;
        Invocation.Builder invocationBuilder;
		resourceWebTarget = target.path("AccountService").path("create");
        invocationBuilder = resourceWebTarget.request(
                MediaType.APPLICATION_JSON);
        BigDecimal balance = new BigDecimal(900).setScale(4, RoundingMode.HALF_EVEN);
        Account acc = new Account(361, balance);
        Response response = invocationBuilder.put(Entity.entity(acc, MediaType.APPLICATION_JSON));
        System.out.println("Create Account Response Status:" + response.getStatus());
        System.out.println("Response" + response.readEntity(String.class));
	}
	/**
	 * Client Method to test service method which gets info of all accounts
	 * @param target
	 */
	private static void getAllAccounts(WebTarget target) {
		// TODO Auto-generated method stub
		WebTarget resourceWebTarget;
        Invocation.Builder invocationBuilder;
		resourceWebTarget = target.path("AccountService").path("all");
        invocationBuilder = resourceWebTarget.request(
                MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        System.out.println("Get All Accounts Response Status:" + response.getStatus());
        System.out.println("Response" + response.readEntity(String.class));	
	}
	/**
	 * Method to populate test data.
	 * @param target
	 */
	public static void populateTestData() throws SQLException, IOException, ClassNotFoundException {
		Connection conn = null;
		try {
			conn = ConnectionFactory.getConnection();
			RunScript.execute(conn, new FileReader("C:/WCSWorkSpace/MoneyTransaction/src/test/demo.sql"));
		} catch (SQLException e) {
			System.err.println("populateTestData(): Error in data population: ");
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				ConnectionFactory.closeQuietly(conn);
			}
		}
	}
}
