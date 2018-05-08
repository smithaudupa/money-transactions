package moneytransferMain.model;

import java.math.BigDecimal;

public class Transactions {
	
	private BigDecimal amount;
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public int getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(int fromAccount) {
		this.fromAccount = fromAccount;
	}

	public int getToAccount() {
		return toAccount;
	}

	public void setToAccount(int toAccount) {
		this.toAccount = toAccount;
	}

	private int fromAccount;
	private int toAccount;
	
	public Transactions() {
	}

	public Transactions(BigDecimal amount, int fromAccount, int toAccount) {
		this.amount = amount;
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
	}
}
