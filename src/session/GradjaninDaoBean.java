package session;

import java.util.List;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import init.ContextClass;
import model.Gradjanin;
import rs.ac.uns.ftn.xws.sessionbeans.common.GenericDaoBean;

@Stateless
@Local(GradjaninDaoLocal.class)
public class GradjaninDaoBean extends GenericDaoBean<Gradjanin, Integer> implements GradjaninDaoLocal {

	@Context
	private HttpServletRequest request;

	@Override
	public Gradjanin login(String username, String password)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {

		password = ContextClass.getPasswordHash(password);
		
		Query q = em.createQuery(
				"select distinct u from " + "Gradjanin u where u.email = :username " + "and u.password = :password");
		q.setParameter("username", username);
		q.setParameter("password", password);

		@SuppressWarnings("unchecked")
		List<Gradjanin> users = q.getResultList();
		if (users.size() == 1) {
			request.getSession().setAttribute("user", users.get(0));
			return users.get(0);
		} else
			return null;
	}

	@Override
	public void logout() {
		// TODO Auto-generated method stub
		request.getSession().invalidate();
	}

}