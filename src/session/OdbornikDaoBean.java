package session;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;

import init.ContextClass;
import model.Gradjanin;
import model.Odbornik;
import rs.ac.uns.ftn.xws.sessionbeans.common.GenericDaoBean;

@Stateless
@Local(OdbornikDaoLocal.class)
public class OdbornikDaoBean extends GenericDaoBean<Odbornik, Integer> implements OdbornikDaoLocal {

	@Context
	private HttpServletRequest request;

	private static Logger log = Logger.getLogger(OdbornikDaoBean.class);
	
	@Override
	public Odbornik login(String username, String password)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {
		
		password = ContextClass.getPasswordHash(password);
		
		Query q = em.createQuery(
				"select distinct u from " + "Odbornik u where u.email = :username " + "and u.password = :password");
		q.setParameter("username", username);
		q.setParameter("password", password);

		@SuppressWarnings("unchecked")
		List<Odbornik> users = q.getResultList();
		if (users.size() == 1) {
			request.getSession().setAttribute("user", users.get(0));
			log.info("Ulogovan odbornik: " + users.get(0).toString());
			return users.get(0);
		} else
			return null;
	}

	@Override
	public void logout() {
		request.getSession().invalidate();
		
	}

	

}
