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
import model.Odbornik;
import model.Predsednik;
import rs.ac.uns.ftn.xws.sessionbeans.common.GenericDaoBean;

@Stateless
@Local(PredsednikDaoLocal.class)

public class PredsednikDaoBean extends GenericDaoBean<Predsednik,Integer> implements PredsednikDaoLocal{
	@Context
	private HttpServletRequest request;

	private static Logger log = Logger.getLogger(PredsednikDaoBean.class);

	
	
	@Override
	public Predsednik login(String username, String password)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {

		password = ContextClass.getPasswordHash(password);

		Query q = em.createQuery(
				"select distinct u from " + "Odbornik u where u.email = :username " + "and u.password = :password");
		q.setParameter("username", username);
		q.setParameter("password", password);

		@SuppressWarnings("unchecked")
		List<Predsednik> users = q.getResultList();
		if (users.size() == 1) {
			request.getSession().setAttribute("user", users.get(0));
			log.info("Ulogovan predsednik: " + users.get(0).toString());
			return users.get(0);
		} else
			return null;
	}

	@Override
	public void logout() {
		// TODO Auto-generated method stub
		request.getSession().invalidate();
	}
	@Override
	public boolean chechCred(String username, String password) {
		Query q = em.createQuery(
				"select distinct u from " + "Odbornik u where u.email = :username " + "and u.password = :password");
		q.setParameter("username", username);
		q.setParameter("password", password);
		@SuppressWarnings("unchecked")
		List<Predsednik> users = q.getResultList();
		if (users.size() == 1) {
			return true;
		} else
		return false;
	}
}
