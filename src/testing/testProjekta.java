package testing;
import java.io.File;
import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import model.Akt;
import model.Clan;
import model.Podtacka;
import model.Stav;
import model.Tacka;


 
/** 
 * komentar
 * Primer demonstrira "unmarshalling" tj. konverziju iz XML fajla
 * u objektni model i nazad.
 * 
 */
public class testProjekta {
	
	public void test() throws Exception {
		try {
			System.out.println(" JAXB unmarshalling/marshalling.\n");
			
//			Podtacka podtacka = createPodTacka(BigInteger.valueOf(5), "TekstPodtacke");
//			Tacka tacka = createTacka(BigInteger.valueOf(4), "TekstTacke",  podtacka);
//			Stav stav = createStav("Tekst Stava", tacka);
//			Clan clan = createClan("Tekst Clana", stav);
//			
			
			// Definiöe se JAXB kontekst (putanja do paketa sa JAXB bean-ovima)
			JAXBContext context = JAXBContext.newInstance("model");
			
			// Unmarshaller je objekat zaduûen za konverziju iz XML-a u objektni model
			Unmarshaller unmarshaller = context.createUnmarshaller();

			Akt akt = (Akt) unmarshaller.unmarshal(new File("./data/instance1.xml"));
			//akt.getDeo().get(0).getGlava().get(0).getClan().add(clan);
			// Izmena nad objektnim modelom dodavanjem novog odseka
			
			
			// Marshaller je objekat zaduûen za konverziju iz objektnog u XML model
			Marshaller marshaller = context.createMarshaller();
			
			// Podeöavanje marshaller-a
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
			// Umesto System.out-a, moûe se koristiti FileOutputStream
			marshaller.marshal(akt, System.out);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	

	}
	
	private Podtacka createPodTacka(BigInteger id, String tekst) {

		// Eksplicitno instanciranje OdsekType klase
		Podtacka podtacka = new Podtacka();
		podtacka.setTekst(tekst);
		podtacka.setRedniBroj(id);

		// Generi≈°u se studenti

		
		return podtacka;
	}
	
	private Tacka createTacka(BigInteger id, String tekst, Podtacka podtacka) {

		// Instanciranje Studenti klase posredstvom ObjectFactory-a
		Tacka tacka = new Tacka();
		tacka.setRedniBroj(id);
		tacka.setTekst(tekst);
		tacka.getPodtacka().add(podtacka);
		
		// Generi≈°e se novi student

		
		return tacka;
	}
	
	private Stav createStav(String tekst, Tacka tacka) {
		
		Stav stav = new Stav();
		stav.setTekst(tekst);
		stav.getTacka().add(tacka);
		
		return stav;
	}
	private Clan createClan(String tekst, Stav stav) {
		
		Clan clan = new Clan();
		clan.setNaziv("NEki naziv");
		clan.setRedniBroj(BigInteger.valueOf(5));
		clan.getStav().add(stav);
		
		return clan;
	}

	

	
    public static void main( String[] args ) throws Exception {
    	testProjekta test = new testProjekta();
    	test.test();
    }
}
