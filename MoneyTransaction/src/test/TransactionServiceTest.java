package test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import mainClient.TestClass;
import moneytransferMain.model.Transactions;

import org.junit.BeforeClass;
import org.junit.Test;

public class TransactionServiceTest extends TestClass {
	
	@BeforeClass
	public static void setup() throws ClassNotFoundException, SQLException, IOException {
		populateTestData();
	}
	 /* TC 1 Positive TestCase = Transaction
    Scenario: test case to transfer funds between accounts
              return 200
     */    
    @Test
    public void testMoneyTransferClient() throws Exception {
    	Client client = ClientBuilder.newClient();
    	WebTarget resourceWebTarget;
        Invocation.Builder invocationBuilder;
        WebTarget target = client.target("http://localhost:8081/MoneyTransaction/restService");
        resourceWebTarget = target.path("transaction");
        invocationBuilder = resourceWebTarget.request(
                MediaType.APPLICATION_JSON);
        BigDecimal deltaAmount = new BigDecimal(20).setScale(4, RoundingMode.HALF_EVEN);
        Transactions transaction = new Transactions(deltaAmount, 123, 234);
        Response response = invocationBuilder.post(Entity.entity(transaction, MediaType.APPLICATION_JSON));
        int statusCode = response.getStatus();
        System.out.println(statusCode);
        assertTrue(statusCode == 200);
    }
    /* TC 2 Negative TestCase = Transaction
    Scenario: test case to transfer funds between accounts
     			(trying to transfer more than the account balance)
              return 500 INTERNAL SERVER ERROR
     */    
    @Test
    public void testMoneyTransferNegativeCase() throws Exception {
    	Client client = ClientBuilder.newClient();
    	WebTarget resourceWebTarget;
        Invocation.Builder invocationBuilder;
        WebTarget target = client.target("http://localhost:8081/MoneyTransaction/restService");
        resourceWebTarget = target.path("transaction");
        invocationBuilder = resourceWebTarget.request(
                MediaType.APPLICATION_JSON);
        BigDecimal deltaAmount = new BigDecimal(600).setScale(4, RoundingMode.HALF_EVEN);
        Transactions transaction = new Transactions(deltaAmount, 123, 234);
        Response response = invocationBuilder.post(Entity.entity(transaction, MediaType.APPLICATION_JSON));
        int statusCode = response.getStatus();
        System.out.println(statusCode);
        assertTrue(statusCode == 500);
    }
    /* TC 2 Negative TestCase = Transaction
    Scenario: test case to transfer funds between accounts
     			transfer amount from invalid account
              return 500 INTERNAL SERVER ERROR
     */    
    @Test
    public void testMoneyTransferFrmWrongAcct() throws Exception {
    	Client client = ClientBuilder.newClient();
    	WebTarget resourceWebTarget;
        Invocation.Builder invocationBuilder;
        WebTarget target = client.target("http://localhost:8081/MoneyTransaction/restService");
        resourceWebTarget = target.path("transaction");
        invocationBuilder = resourceWebTarget.request(
                MediaType.APPLICATION_JSON);
        BigDecimal deltaAmount = new BigDecimal(0).setScale(4, RoundingMode.HALF_EVEN);
        Transactions transaction = new Transactions(deltaAmount, 99, 234);
        Response response = invocationBuilder.post(Entity.entity(transaction, MediaType.APPLICATION_JSON));
        int statusCode = response.getStatus();
        System.out.println(statusCode);
        assertTrue(statusCode == 500);
    }
    /* TC3 Negative TestCase = Transaction
    Scenario: test case to transfer funds between accounts
     			transfer amount from invalid account
              return 500 INTERNAL SERVER ERROR
     */    
    @Test
    public void testMoneyTransferToInvalidAcc() throws Exception {
    	Client client = ClientBuilder.newClient();
    	WebTarget resourceWebTarget;
        Invocation.Builder invocationBuilder;
        WebTarget target = client.target("http://localhost:8081/MoneyTransaction/restService");
        resourceWebTarget = target.path("transaction");
        invocationBuilder = resourceWebTarget.request(
                MediaType.APPLICATION_JSON);
        BigDecimal deltaAmount = new BigDecimal(0).setScale(4, RoundingMode.HALF_EVEN);
        Transactions transaction = new Transactions(deltaAmount, 222, 99);
        Response response = invocationBuilder.post(Entity.entity(transaction, MediaType.APPLICATION_JSON));
        int statusCode = response.getStatus();
        System.out.println(statusCode);
        assertTrue(statusCode == 500);
    }
}
