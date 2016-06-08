package xml.crl;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CRLException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Date;

import java.security.InvalidKeyException;

import java.security.SignatureException;


import javax.security.auth.x500.X500Principal;


import org.bouncycastle.x509.X509V2CRLGenerator;



public class CheckCRL {
	private static final String KEY_STORE_FILE = "C:/Users/Windows7/git/XMLProjekat/data/sgns.jks";
	private static X509CRL crl ;
	
	public static boolean checkInCrl(BigInteger serialNumber) {
		if (crl.getRevokedCertificate(serialNumber) != null){
			return true;
		}
		return false;
		
		
	}
	
	private void loadCRL (String path) {
		
	}
	
	private void saveCRL (String path){
		
	}
	
	public void addCertificate (X509Certificate cert, int reason) {
		X509V2CRLGenerator   crlGen = new X509V2CRLGenerator();
		Date                 now = new Date();
		X509Certificate      caCrlCert;
		PrivateKey           caCrlPrivateKey ;

		
		
		
		PrivateKey privateKey = readPrivateKey ("sgns", "sgns", "sgns");
		Certificate certificate =  readCertificate("sgns", "sgns") ;
		caCrlCert = (X509Certificate)certificate;
		
	    crlGen.setIssuerDN(new X500Principal("sgns"));
	    crlGen.setThisUpdate(now);
	    crlGen.setNextUpdate(new Date(now.getTime() + 50));
	    crlGen.setSignatureAlgorithm("SHA1withRSA");
	    try {
			crlGen.addCRL(crl);
		} catch (CRLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    crlGen.addCRLEntry(cert.getSerialNumber(), now, reason);
	    try {
			crl = crlGen.generate(privateKey);
		} catch (InvalidKeyException | CRLException | IllegalStateException | NoSuchAlgorithmException
				| SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    
		
	}
	
	
	/**
	 * Ucitava sertifikat is KS fajla
	 * alias primer
	 */
	private Certificate readCertificate(String alias, String kspassword) {
		try {
			//kreiramo instancu KeyStore
			KeyStore ks = KeyStore.getInstance("JKS", "SUN");
			//ucitavamo podatke
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(KEY_STORE_FILE));
			ks.load(in, kspassword.toCharArray());
			
			if(ks.isKeyEntry(alias)) {
				Certificate cert = ks.getCertificate(alias);
				return cert;
				
			}
			else
				return null;
			
		} catch (KeyStoreException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			return null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (CertificateException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	/**
	 * Ucitava privatni kljuc is KS fajla
	 * alias primer
	 */
	private PrivateKey readPrivateKey(String alias, String kspassword, String apassword) {
		try {
			//kreiramo instancu KeyStore
			KeyStore ks = KeyStore.getInstance("JKS", "SUN");
			//ucitavamo podatke
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(KEY_STORE_FILE));
			ks.load(in, kspassword.toCharArray());
			
			if(ks.isKeyEntry(alias)) {
				PrivateKey pk = (PrivateKey) ks.getKey(alias, apassword.toCharArray());
				
				return pk;
			}
			else
				return null;
			
		} catch (KeyStoreException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			return null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (CertificateException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	
	
	private void init () throws CertificateException, CRLException, IOException, InvalidKeyException, IllegalStateException, NoSuchAlgorithmException, SignatureException {
		
		X509V2CRLGenerator   crlGen = new X509V2CRLGenerator();
		Date                 now = new Date();
		X509Certificate      caCrlCert;
		PrivateKey           caCrlPrivateKey ;

		
		
		
		PrivateKey privateKey = readPrivateKey ("sgns", "sgns", "sgns");
		Certificate certificate =  readCertificate("sgns", "sgns") ;
		caCrlCert = (X509Certificate)certificate;
		
	    crlGen.setIssuerDN(caCrlCert.getIssuerX500Principal());
	    crlGen.setThisUpdate(now);
	    crlGen.setNextUpdate(new Date(now.getTime() + 50));
	    crlGen.setSignatureAlgorithm("SHA1withRSA");
	    crl = crlGen.generate(privateKey);
	    System.out.println(crl);


		 
	}

	
	
	public static void main(String[] args) {
		CheckCRL sign = new CheckCRL();
		try {
			try {
				sign.init();
			} catch (InvalidKeyException | IllegalStateException | NoSuchAlgorithmException | SignatureException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (CertificateException | CRLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
