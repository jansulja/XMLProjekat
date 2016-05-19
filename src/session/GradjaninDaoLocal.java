package session;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import model.Gradjanin;
import rs.ac.uns.ftn.xws.entities.users.User;
import rs.ac.uns.ftn.xws.sessionbeans.common.GenericDao;

public interface GradjaninDaoLocal extends GenericDao<Gradjanin,Integer> {

	public Gradjanin login(String username, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException;

	public void logout();
	
}
