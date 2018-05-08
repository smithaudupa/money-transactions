package test;
import static org.junit.Assert.*;

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
import moneytransferMain.model.Account;
import moneytransferMain.model.Transactions;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test class to test Account Service functionality implementation.
 * @author smitha
 *
 */
public class TestMainClassTest extends TestClass{
	@BeforeClass
	public static void setup() throws ClassNotFoundException, SQLException, IOException {
		populateTestData();
	}
    /*
    TC1 Positive Case = AccountService
    Scenario: test case to get particular user Account info
      return Account info for the given account id
    */
    //@Test
    public void testGetAccountByIdClent() throws Exception {
    	Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8081/MoneyTransaction/restService");
        WebTarget desiredTarget = target.path("AccountService").path("222");
        Invocation.Builder invocationBuilder;
        invocationBuilder = desiredTarget.request(
                MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        int statusCode = response.getStatus();
        System.out.println(statusCode);
        assertTrue(statusCode == 200);
        //check the content
        String accountInfo = response.readEntity(String.class);
        assertNotNull("AccountId not null", accountInfo);
    }
    /*
    TC2 Positive Case = AccountService
    Scenario: test case to get all user accounts
              return 200 OK
    */
    //@Test
    public void testGetAllAccountsClient() throws Exception {
    	Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8081/MoneyTransaction/restService");
        WebTarget desiredTarget = target.path("AccountService").path("all");
        Invocation.Builder invocationBuilder;
        invocationBuilder = desiredTarget.request(
                MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        int statusCode = response.getStatus();
        System.out.println(statusCode);
        assertTrue(statusCode == 200);
        //check the content
        String accountList = response.readEntity(String.class);
        System.out.println(accountList);;
        assertNotNull("Account object not null", accountList);
    }
   /* TC3 Positive Case = AccountService
    Scenario: test case to get account balance given account ID
              return 200 OK
    */
    //@Test
    public void testGetAccountBalance() throws Exception {
    	Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8081/MoneyTransaction/restService");
        WebTarget desiredTarget = target.path("AccountService").path("101");
        Invocation.Builder invocationBuilder;
        invocationBuilder = desiredTarget.request(
                MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        int statusCode = response.getStatus();
        System.out.println(statusCode);
        assertTrue(statusCode == 200);
        //check the content, assert user test2 have balance 100
        String accountInfo = response.readEntity(String.class);
        assertNotNull("Balance not null", accountInfo);
    }
    
    /*TC4  Positive Case = AccountService
    Scenario: test case to create new account
              return 200 OK
    */
    //@Test
    public void testCreateAccountClient() throws Exception {
    	Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8081/MoneyTransaction/restService");
        WebTarget desiredTarget = target.path("AccountService").path("create");
        Invocation.Builder invocationBuilder;
        invocationBuilder = desiredTarget.request(
                MediaType.APPLICATION_JSON);
        BigDecimal balance = new BigDecimal(343).setScale(4, RoundingMode.HALF_EVEN);
        Account acc = new Account(343, balance);
        Response response = invocationBuilder.put(Entity.entity(acc, MediaType.APPLICATION_JSON));
        int statusCode = response.getStatus();
        System.out.println(statusCode);
        assertTrue(statusCode == 200);
        String jsonString = response.readEntity(String.class);
        System.out.println(jsonString);
        assertNotNull("Account created Successful", jsonString);
    }
    
    /* TC 5 Negative TestCase = AccountService
    Scenario: test case to create user account which already exists.
              return 500 INTERNAL SERVER ERROR
     */    
    //@Test
    public void testCreateExistingAccountClient() throws Exception {
    	Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8081/MoneyTransaction/restService");
        WebTarget desiredTarget = target.path("AccountService").path("create");
        Invocation.Builder invocationBuilder;
        invocationBuilder = desiredTarget.request(
                MediaType.APPLICATION_JSON);
        BigDecimal balance = new BigDecimal(101).setScale(4, RoundingMode.HALF_EVEN);
        Account acc = new Account(101, balance);
        Response response = invocationBuilder.put(Entity.entity(acc, MediaType.APPLICATION_JSON));
        int statusCode = response.getStatus();
        System.out.println(statusCode);
        assertTrue(statusCode == 500);
    }
    /* TC 6 Positive TestCase = Transaction
    Scenario: test case to transfer funds between accounts
              return 500 INTERNAL SERVER ERROR
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
        BigDecimal deltaAmount = new BigDecimal(100).setScale(4, RoundingMode.HALF_EVEN);
        Transactions transaction = new Transactions(deltaAmount, 123, 234);
        Response response = invocationBuilder.post(Entity.entity(transaction, MediaType.APPLICATION_JSON));
        int statusCode = response.getStatus();
        System.out.println(statusCode);
        assertTrue(statusCode == 200);
    }
/**
 * Method to log data in json format.
 * @param object
 */
    public static void logJson(final Object object) {
        System.gc();
        ObjectMapper obj = new ObjectMapper();
        try {
                String jSon = obj.writeValueAsString(object);
               System.out.println(jSon);
        } catch (JsonGenerationException e) {
                System.err.println("JsonGenerationException" + e);
        } catch (JsonMappingException e) {
        	System.err.println("JsonMappingException" + e);
        } catch (IOException e) {
        	System.err.println("IOException" + e);
        }
    }
}
