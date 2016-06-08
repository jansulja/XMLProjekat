package init;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import model.Gradjanin;
import model.Odbornik;
import session.GradjaninDaoLocal;
import session.OdbornikDaoLocal;

public class ContextClass implements ServletContextListener {

	@EJB
	private GradjaninDaoLocal gradjaninDao;
	
	@EJB
	private OdbornikDaoLocal odbornikDao;
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {

		Gradjanin gAdmin = new Gradjanin();
		
		gAdmin.setIme("Gradjanin1");
		gAdmin.setBrojlicne(0123456);
		gAdmin.setBrojTelefona(063321546);
		gAdmin.setDatumrodjenja(new Date(1990, 3, 5));
		gAdmin.setDrzava("Srbija");
		gAdmin.setEmail("g1@g1.com");
		gAdmin.setJMBG(465789746);
		gAdmin.setMestoRodjenja("Novi Sad");
		gAdmin.setOpstinaRodjenja("Novi Sad");
		gAdmin.setPol("M");
		gAdmin.setPrezime("Adminovic");
		
		
		Odbornik o1 = new Odbornik();
		o1.setBrojlicne(11);
		o1.setBrojTelefona(11);
		o1.setDatumrodjenja(new Date());
		o1.setDrzava("Srbija");
		o1.setEmail("o1@o1.com");
		o1.setIme("Zika");
		o1.setJMBG(11);
		o1.setMestoRodjenja("Novi Sad");
		o1.setOdbornickaGrupa("NSS");
		o1.setOpstinaRodjenja("Novi Sad");
		o1.setPlata(1200.00);
		o1.setPol("M");
		o1.setPrezime("Zikic");
		o1.setStranka("UJEBp");
		o1.setZvanje("Dipl. Ing. Pilicarstva");
		
		
		try {
			gAdmin.setPassword(getPasswordHash("g1"));
			o1.setPassword(getPasswordHash("o1"));
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
//		try {
//
//			odbornikDao.persist(o1);
//		} catch (NoSuchFieldException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//		try {
//
//			gradjaninDao.persist(gAdmin);
//		} catch (NoSuchFieldException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		

	}
	
	public static String getPasswordHash(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes("UTF-8"));
		byte byteData[] = md.digest();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
		
	}

}
