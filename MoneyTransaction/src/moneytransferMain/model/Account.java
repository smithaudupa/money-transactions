package moneytransferMain.model;

import java.math.BigDecimal;

public class Account {
	
public Account() {
		super();
	}
public Account(int accntId, BigDecimal amnt) {
		super();
		this.accountId = accntId;
		this.amount = amnt;
	}
private int accountId;
private BigDecimal amount;
public int getAccountId() {
	return accountId;
}
public void setAccountId(int accountId) {
	this.accountId = accountId;
}
public BigDecimal getAmount() {
	return amount;
}
public void setAmount(BigDecimal amount) {
	this.amount = amount;
}

public BigDecimal withdrawal(BigDecimal amount) {
	return amount;
}
public BigDecimal deposit(BigDecimal amount) {
	return amount;
}

}
