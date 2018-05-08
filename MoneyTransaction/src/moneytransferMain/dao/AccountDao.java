package moneytransferMain.dao;

import java.math.BigDecimal;
import java.util.List;

import moneytransferMain.model.Account;
import moneytransferMain.model.Transactions;

public interface AccountDao {

    List<Account> getAccounts() throws Exception;
    List<Account> createAccount(Account account) throws Exception;
    Account updateBalance(int accountId, BigDecimal amountBal) throws Exception;
    int transferBalance(Transactions transactions) throws Exception;
}