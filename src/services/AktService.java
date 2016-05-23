package services;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import model.Akt;
import model.Gradjanin;
import rs.ac.uns.ftn.xws.util.Authenticate;
import session.AktDaoLocal;

@Path("/akt")
public class AktService {

	
	private static Logger log = Logger.getLogger(Gradjanin.class);
	
	@EJB
	AktDaoLocal aktDao;

	@GET 
    @Produces(MediaType.APPLICATION_JSON)
	public List<Akt> findByAll() {
		List<Akt> retVal = null;
		try {
			retVal = aktDao.findAll();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return retVal;
    }
	
}
