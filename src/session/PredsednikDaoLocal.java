package session;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;


import model.Predsednik;
import rs.ac.uns.ftn.xws.sessionbeans.common.GenericDao;

public interface PredsednikDaoLocal extends GenericDao<Predsednik, Integer>{
	public Predsednik login(String username, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException;
	public boolean chechCred(String username, String password);

	public void logout();
}
