package model.amandman;

public class AmandmanDodavanje {

	private String amandman;
	private String aktId;



	public AmandmanDodavanje(String data) {


		String[] tokens = data.split("\",\"");

		String[] akt = tokens[1].split("\"");

		this.aktId = akt[2];

		this.amandman = clearXML( tokens[0].split("\":\"")[1]);





	}
	private String clearXML(String string) {
		String s1 = string.replaceAll("xmlns=\\\\", "xmlns=");
		String s2 = s1.replaceAll("http://www.parlament.gov.rs/amandman\\\\", "http://www.parlament.gov.rs/amandman");

		// TODO Auto-generated method stub
		return s2;
	}
	public String getAmandman() {
		return amandman;
	}
	public void setAmandman(String amandman) {
		this.amandman = amandman;
	}
	public String getAktId() {
		return aktId;
	}
	public void setAktId(String aktId) {
		this.aktId = aktId;
	}



}
