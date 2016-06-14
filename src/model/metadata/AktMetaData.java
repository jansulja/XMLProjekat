package model.metadata;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


public class AktMetaData {


	private String naziv;
	private String id;
	private String status;
	private String datumPredlaganja;
	private String odbornik;

	public AktMetaData(String naziv, String id, String status, String datumPredlaganja, String odbornik) {
		super();
		this.naziv = naziv;
		this.id = id;
		this.status = status;
		this.datumPredlaganja = datumPredlaganja;
		this.odbornik = odbornik;
	}



	public AktMetaData() {
		super();
		// TODO Auto-generated constructor stub
	}



	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDatumPredlaganja() {
		return datumPredlaganja;
	}

	public void setDatumPredlaganja(String datumPredlaganja) {
		this.datumPredlaganja = datumPredlaganja;
	}

	public String getOdbornik() {
		return odbornik;
	}

	public void setOdbornik(String odbornik) {
		this.odbornik = odbornik;
	}



	@Override
	public String toString() {
		return " {\"naziv\":\"" + naziv + "\", \"id\":\"" + id + "\", \"status\":\"" + status + "\", \"datumPredlaganja\":\""
				+ datumPredlaganja + "\", \"odbornik\":\"" + odbornik + "\"}";
	}




}
