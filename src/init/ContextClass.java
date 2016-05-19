package init;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import model.Gradjanin;
import session.GradjaninDaoLocal;

public class ContextClass implements ServletContextListener {

	@EJB
	private GradjaninDaoLocal gradjaninDao;
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {

		Gradjanin gAdmin = new Gradjanin();
		
		gAdmin.setIme("Admin");
		gAdmin.setBrojlicne(0123456);
		gAdmin.setBrojTelefona(063321546);
		gAdmin.setDatumrodjenja(new Date(1990, 3, 5));
		gAdmin.setDrzava("Srbija");
		gAdmin.setEmail("admin@admin.com");
		gAdmin.setJMBG(465789746);
		gAdmin.setMestoRodjenja("Novi Sad");
		gAdmin.setOpstinaRodjenja("Novi Sad");
		gAdmin.setPol("M");
		gAdmin.setPrezime("Adminovic");
		try {
			gAdmin.setPassword(getPasswordHash("admin"));
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
//		try {
//			//gradjaninDao.persist(gAdmin);
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
