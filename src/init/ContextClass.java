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
import model.Predsednik;
import session.GradjaninDaoLocal;
import session.OdbornikDaoLocal;
import session.PredsednikDaoLocal;

public class ContextClass implements ServletContextListener {




	@EJB
	private GradjaninDaoLocal gradjaninDao;

	@EJB
	private OdbornikDaoLocal odbornikDao;

	@EJB
	private PredsednikDaoLocal predsednikDao;

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

		Predsednik p1 = new Predsednik();
		p1.setBrojlicne(11);
		p1.setBrojTelefona(11);
		p1.setDatumrodjenja(new Date());
		p1.setDrzava("Srbija");
		p1.setEmail("p1@p1.com");
		p1.setIme("Predsednik 1");
		p1.setJMBG(11);
		p1.setMestoRodjenja("Novi Sad");
		p1.setOdbornickaGrupa("NSS");
		p1.setOpstinaRodjenja("Novi Sad");
		p1.setPlata(1200.00);
		p1.setPol("M");
		p1.setPrezime("Zikic");
		p1.setStranka("UJEBp");
		p1.setZvanje("Dipl. Ing. Pilicarstva");


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

		Odbornik o2 = new Odbornik();
		o2.setBrojlicne(12);
		o2.setBrojTelefona(12);
		o2.setDatumrodjenja(new Date());
		o2.setDrzava("Srbija");
		o2.setEmail("o2@o2.com");
		o2.setIme("Pera");
		o2.setJMBG(11);
		o2.setMestoRodjenja("Novi Sad");
		o2.setOdbornickaGrupa("NSS");
		o2.setOpstinaRodjenja("Novi Sad");
		o2.setPlata(900.00);
		o2.setPol("M");
		o2.setPrezime("Peric");
		o2.setStranka("Stranka 2");
		o2.setZvanje("Zvanje 1");

		try {
			gAdmin.setPassword(getPasswordHash("g1"));
			o1.setPassword(getPasswordHash("o1"));
			o2.setPassword(getPasswordHash("o2"));
			p1.setPassword(getPasswordHash("p1"));
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

//		try {
//
//			predsednikDao.persist(p1);
//		} catch (NoSuchFieldException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

//		try {
//
//			odbornikDao.persist(o2);
//		} catch (NoSuchFieldException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

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
