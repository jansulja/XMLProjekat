package rs.ac.uns.ftn.xws.services.users;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import rs.ac.uns.ftn.xws.sessionbeans.payments.InvoiceDaoLocal;
import rs.ac.uns.ftn.xws.sessionbeans.users.UserDaoLocal;

@Path("/hello")
public class Hello {

	@EJB
	private UserDaoLocal userDao;
	
	@GET
	public String get(){
		
		return userDao.findAll().get(0).getUsername();
		
	}
	
}
