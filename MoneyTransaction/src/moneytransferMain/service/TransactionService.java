package moneytransferMain.service;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import moneytransferMain.dao.AccountDaoImpl;
import moneytransferMain.model.Transactions;
@Path("/transaction")
@ApplicationPath("/restService")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionService extends Application{
	/**
	 * Transfer fund between two accounts.
	 * @param transaction
	 * @return
	 * @throws Exception
	 */
	@POST
	public Response transferFund(Transactions transaction) throws Exception {
		AccountDaoImpl impl = new AccountDaoImpl();
			int updateCount = impl.transferBalance(transaction);
			System.out.println(updateCount);
			if (updateCount == 2) {
				return Response.status(Response.Status.OK).build();
			} else {
				// transaction failed
				throw new WebApplicationException("Transaction failed", Response.Status.BAD_REQUEST);
			}
	}
}
