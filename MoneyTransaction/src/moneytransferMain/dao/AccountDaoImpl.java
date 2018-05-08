package moneytransferMain.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import moneytransferMain.connection.ConnectionFactory;
import moneytransferMain.model.Account;
import moneytransferMain.model.Transactions;

public class AccountDaoImpl implements AccountDao {

		private final static String GET_ACCOUNNT_BY_ID = "SELECT * FROM Account WHERE AccountId = ? ";
		private final static String LOCK_ACCOUNT = "SELECT * FROM Account WHERE AccountId = ? FOR UPDATE";
		private final static String SQL_CREATE_ACC = "INSERT INTO Account (AccountId, BalanceAmount) VALUES (?, ?)";
		private final static String SQL_UPDATE_ACC_BALANCE = "UPDATE Account SET BalanceAmount = ? WHERE AccountId = ? ";
		private final static String SQL_GET_ALL_ACC = "SELECT * FROM Account";
	    public static final BigDecimal zeroAmount = new BigDecimal(0).setScale(4, RoundingMode.HALF_EVEN);
		
		/**
		 * Get all accounts.
		 * @throws Exception 
		 */
		public List<Account> getAccounts() throws Exception {
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			List<Account> allAccounts = new ArrayList<Account>();
			try {
				conn = ConnectionFactory.getConnection();
				stmt = conn.prepareStatement(SQL_GET_ALL_ACC);
				rs = stmt.executeQuery();
				while (rs.next()) {
					Account acc = new Account(rs.getInt("AccountId"),
							rs.getBigDecimal("BalanceAmount"));
					allAccounts.add(acc);
				}
				return allAccounts;
			} catch (SQLException e) {
				throw new Exception("getAccountById(): Unable to read data", e);
			} finally {
				ConnectionFactory.closeQuietly(conn);
			}
		}
		
		/**
		 * Get account by id
		 * @throws Exception 
		 */
		public Account getAccountById(int accountId) throws Exception {
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			Account acc = null;
			try {
				conn = ConnectionFactory.getConnection();
				stmt = conn.prepareStatement(GET_ACCOUNNT_BY_ID);
				stmt.setInt(1, accountId);
				rs = stmt.executeQuery();
				if (rs.next()) {
					acc = new Account(rs.getInt("AccountId"),
							rs.getBigDecimal("BalanceAmount"));
				}
				return acc;
			} catch (SQLException e) {
				throw new Exception("getAccountById(): Error in reading data", e);
			} finally {
				ConnectionFactory.closeQuietly(conn);
			}

		}
		
		/**
		 * Create account
		 * @throws Exception 
		 */
		public List<Account> createAccount(Account account) throws Exception {
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			int updatedRows = 0;
			List<Account> accList = new ArrayList<Account>();
			Account acc = null;
			try {
				conn = ConnectionFactory.getConnection();
				stmt = conn.prepareStatement(SQL_CREATE_ACC);
				stmt.setInt(1, account.getAccountId());
				stmt.setBigDecimal(2, account.getAmount());
				updatedRows = stmt.executeUpdate();
				if (updatedRows == 0) {
					System.err.println("no rows created.");
					throw new Exception("Account Cannot be created");
				}
				stmt = conn.prepareStatement(SQL_GET_ALL_ACC);
			      rs = stmt.executeQuery();
			      while(rs.next()){
			    	  acc = new Account(rs.getInt("AccountId"),
										rs.getBigDecimal("BalanceAmount"));
			    	  accList.add(acc);
			      }
				return accList;
			} catch (SQLException | ClassNotFoundException e) {
				System.err.println("Error Inserting rows  " + acc);
				throw new Exception("Error creating account details " + acc, e);
			} finally {
				ConnectionFactory.closeQuietly(conn);
			}
		}
		
		/**
		 * Update balance
		 * @throws Exception 
		 */
		public Account updateBalance(int accountId, BigDecimal balance) throws Exception {
			Connection conn = null;
			PreparedStatement updateStmt = null;
			PreparedStatement stmtLocked = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			Account targetAccount = null;
			try {
				conn = ConnectionFactory.getConnection();
				conn.setAutoCommit(false);
				stmtLocked = conn.prepareStatement(LOCK_ACCOUNT);
				stmtLocked.setInt(1, accountId);
				rs = stmtLocked.executeQuery();
				if (rs.next()) {
					targetAccount = new Account(rs.getInt("AccountId"),
							rs.getBigDecimal("BalanceAmount"));
				}
				System.out.println(rs.getInt(1));
				System.out.println(rs.getBigDecimal(2));
				if (targetAccount == null) {
					throw new Exception("updateAccountBalance(): lock failed: " + accountId);
				}
				BigDecimal balanceAmt = targetAccount.getAmount().add(balance);
				System.out.println(balanceAmt);
				if (balanceAmt.compareTo(zeroAmount) < 0) {
					throw new Exception("Not enough money to transfer" + accountId);
				}
				updateStmt = conn.prepareStatement(SQL_UPDATE_ACC_BALANCE);
				updateStmt.setBigDecimal(1, balanceAmt);
				updateStmt.setInt(2, accountId);
				System.out.println(accountId);
				System.out.println(balanceAmt);
				updateStmt.executeUpdate();
				conn.commit();
				stmt = conn.prepareStatement(SQL_GET_ALL_ACC);
			      rs = stmt.executeQuery();
			      while(rs.next()){
			    	  targetAccount = new Account(rs.getInt("AccountId"),
										rs.getBigDecimal("BalanceAmount"));
			      }
				return targetAccount;
			} catch (SQLException se) {
				System.err.println("updateAccountBalance(): User Transaction Failed" + accountId);
				try {
					if (conn != null)
						conn.rollback();
				} catch (SQLException re) {
					throw new Exception("Fail to rollback transaction", re);
				}
			} finally {
				ConnectionFactory.closeQuietly(conn);
			}
			return targetAccount;
		}

		/**
		 * Transfer balance between two accounts.
		 * @throws Exception 
		 */
		public int transferBalance(Transactions transaction) throws Exception {
			int result = 0;
			Connection conn = null;
			PreparedStatement stmtLocked = null;
			PreparedStatement updateStmt = null;
			ResultSet rs = null;
			Account fromAccount = null;
			Account toAccount = null;

			try {
				conn = ConnectionFactory.getConnection();
				conn.setAutoCommit(false);
				// lock the credit account and debit account:
				stmtLocked = conn.prepareStatement(LOCK_ACCOUNT);
				stmtLocked.setInt(1, transaction.getFromAccount());
				rs = stmtLocked.executeQuery();
				if (rs.next()) {
					fromAccount = new Account(rs.getInt("AccountId"),
							rs.getBigDecimal("BalanceAmount"));
				}
				stmtLocked = conn.prepareStatement(LOCK_ACCOUNT);
				stmtLocked.setInt(1, transaction.getToAccount());
				rs = stmtLocked.executeQuery();
				if (rs.next()) {
					toAccount = new Account(rs.getInt("AccountId"),
							rs.getBigDecimal("BalanceAmount"));
				}

				if (fromAccount == null || toAccount == null) {
					throw new Exception("Unable to lock both accounts");
				}

				// check the amount for transfer
				BigDecimal fromAccountLeftOver = fromAccount.getAmount().subtract(transaction.getAmount());
				if (fromAccountLeftOver.compareTo(zeroAmount) < 0) {
					throw new Exception("Not enough Fund to transfer ");
				}
				updateStmt = conn.prepareStatement(SQL_UPDATE_ACC_BALANCE);
				updateStmt.setBigDecimal(1, fromAccountLeftOver);
				updateStmt.setInt(2, transaction.getFromAccount());
				updateStmt.addBatch();
				updateStmt.setBigDecimal(1, toAccount.getAmount().add(transaction.getAmount()));
				updateStmt.setInt(2, transaction.getToAccount());
				updateStmt.addBatch();
				int[] rowsUpdated = updateStmt.executeBatch();
				result = rowsUpdated[0] + rowsUpdated[1];
				// If no error, commit the transaction
				conn.commit();
			} catch (SQLException se) {
				System.err.println("transferAccountBalance(): Transaction Failed" + transaction);
				try {
					if (conn != null)
						conn.rollback();
				} catch (SQLException re) {
					throw new Exception("Fail to rollback", re);
				}
			} finally {
				ConnectionFactory.closeQuietly(conn);
			}
			return result;
		}
}
