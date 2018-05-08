package moneytransferMain.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import moneytransferMain.model.Account;
import moneytransferMain.dao.AccountDaoImpl;
@Path("/AccountService")
@ApplicationPath("/restService")
public class AccountsService extends Application{
    /**
     * Find all accounts
     * @return
     * @throws Exception
     */
    @GET
    @Path("/all")
    public List<Account> getAllAccounts() throws Exception {
    	AccountDaoImpl account = new AccountDaoImpl();
        return account.getAccounts();
    }

    /**
     * Find by account id
     * @param accountId
     * @return
     * @throws Exception
     */
    @GET
    @Path("/{accountId}")
    public Account getAccount(@PathParam("accountId") int accountId) throws Exception {
    	AccountDaoImpl account = new AccountDaoImpl();
        return account.getAccountById(accountId);
    }
    
    /**
     * Find balance by account Id
     * @param accountId
     * @return
     * @throws Exception
     */
    @GET
    @Path("/{accountId}/balance")
    public BigDecimal getBalance(@PathParam("accountId") int accountId) throws Exception {
    	AccountDaoImpl impl = new AccountDaoImpl();
        final Account account = impl.getAccountById(accountId);

        if(account == null){
            throw new WebApplicationException("Account not found", Response.Status.NOT_FOUND);
        }
        return account.getAmount();
    }
    
    /**
     * Create Account
     * @param account
     * @return
     * @throws Exception
     */
    @PUT
    @Path("/create")
    public List<Account> createAccount(Account account) throws Exception { 
    	AccountDaoImpl impl = new AccountDaoImpl();
    	System.out.println(account);
    	List<Account> accList = new ArrayList<Account>();
    	accList = impl.createAccount(account);
        return accList;
    }

    /**
     * Deposit amount by account Id
     * @param accountId
     * @param amount
     * @return
     * @throws Exception
     */
    @PUT
    @Path("/{accountId}/deposit/{amount}")
    public Account deposit(@PathParam("accountId") int accountId,@PathParam("amount") BigDecimal amount) throws Exception {
    	AccountDaoImpl impl = new AccountDaoImpl();
    	BigDecimal zeroAmount = new BigDecimal(0).setScale(4, RoundingMode.HALF_EVEN);
        if (amount.compareTo(zeroAmount) <=0){
            throw new WebApplicationException("Invalid Deposit amount", Response.Status.BAD_REQUEST);
        }
        impl.updateBalance(accountId,amount.setScale(4, RoundingMode.HALF_EVEN));
        return impl.getAccountById(accountId);
    }

    /**
     * Withdraw amount by account Id
     * @param accountId
     * @param amount
     * @return
     * @throws Exception
     */
    @PUT
    @Path("/{accountId}/withdraw/{amount}")
    public Account withdraw(@PathParam("accountId") int accountId,@PathParam("amount") BigDecimal amount) throws Exception {
    	BigDecimal zeroAmount = new BigDecimal(0).setScale(4, RoundingMode.HALF_EVEN);
    	AccountDaoImpl impl = new AccountDaoImpl();
    	if (amount.compareTo(zeroAmount) <=0){
            throw new WebApplicationException("Invalid Deposit amount", Response.Status.BAD_REQUEST);
        }
        BigDecimal delta = amount.negate();
        impl.updateBalance(accountId,delta.setScale(4, RoundingMode.HALF_EVEN));
        return impl.getAccountById(accountId);
    }
}
