package session;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import model.Gradjanin;
import model.Odbornik;
import rs.ac.uns.ftn.xws.sessionbeans.common.GenericDao;

public interface OdbornikDaoLocal extends GenericDao<Odbornik, Integer> {

	public Odbornik login(String username, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException;

	public void logout();
	
}
