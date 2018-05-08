package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import mainClient.TestClass;
import moneytransferMain.model.Account;
import moneytransferMain.service.AccountsService;

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
public class AccountServiceTest extends TestClass{
	@BeforeClass
	public static void setup() throws ClassNotFoundException, SQLException, IOException {
		populateTestData();
	}
	/*
    TC1 Positive Case = AccountService
    Scenario: to get particular user Account info
              return Account info for the given account id
    */
    //@Test
    public void testGetAccountById() throws Exception{
        Account acc = new Account();
        AccountsService accServ = new AccountsService();
        acc = accServ.getAccount(101);
        logJson(acc);
        assertNotNull("Account object is not null ", acc);
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
    TC3 Positive Case = AccountService
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
    Scenario: test case to create new Account details
              return 200 OK
    */
    @Test
    public void testGetAllAccounts() throws Exception {
    	List<Account> accList = new ArrayList<Account>();
        AccountsService accServ = new AccountsService();
        accList = accServ.getAllAccounts();
        logJson(accList);
        assertNotNull("Account object is not null ", accList);
    }
   /* TC4 Positive Case = AccountService
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
    /* TC4 Positive Case = AccountService
    Scenario: test case to get account balance given account ID
              return 200 OK
    */
    //@Test
    public void testGetAccntBalance() throws Exception {
    	AccountsService acc = new AccountsService();
    	int accountId = 222;
    	BigDecimal dbBalnce = new BigDecimal(500).setScale(4, RoundingMode.HALF_EVEN);
    	BigDecimal balance = acc.getAccount(accountId).getAmount();
    	logJson(balance);
    	assertEquals(dbBalnce, balance);
    }
    
    /*TC5  Positive Case = AccountService
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
    /* TC5 Positive Case = AccountService
    Scenario: test case to create new Account details
              return 200 OK
    */
    @Test
    public void testCreateAccount() throws Exception {
    	AccountsService acc = new AccountsService();
    	int accountId = 891;
    	BigDecimal dbBalnce = new BigDecimal(990).setScale(4, RoundingMode.HALF_EVEN);
    	Account account = new Account(accountId, dbBalnce);
    	List<Account> acclist = new ArrayList<Account>();
    	acclist = acc.createAccount(account);
    	logJson(acclist);
    	assertNotNull("Account created Successful", acclist);
    }
    
    /* TC 6 Negative TestCase = AccountService
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
