package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "odbornik")
@PrimaryKeyJoinColumn(name="BOAT_ID")
public class Odbornik extends Gradjanin implements Serializable{
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
	private Integer id;
	private String odbornickaGrupa;
	private String stranka;
	private double plata;
	private String zvanje;
	public Odbornik(Integer id, String ime, String prezime, Integer brojlicne, java.util.Date datumrodjenja, String pol,
			Integer jmbg, String mestoRodjenja, String opstinaRodjenja, String drzava, Integer brojTelefona,
			String email, Integer id2, String odbornickaGrupa, String stranka, double plata, String zvanje) {
		super(id, ime, prezime, brojlicne, datumrodjenja, pol, jmbg, mestoRodjenja, opstinaRodjenja, drzava,
				brojTelefona, email);
		id = id2;
		this.odbornickaGrupa = odbornickaGrupa;
		this.stranka = stranka;
		this.plata = plata;
		this.zvanje = zvanje;
	}
	public Odbornik() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Odbornik(Integer id, String ime, String prezime, Integer brojlicne, java.util.Date datumrodjenja, String pol,
			Integer jmbg, String mestoRodjenja, String opstinaRodjenja, String drzava, Integer brojTelefona,
			String email) {
		super(id, ime, prezime, brojlicne, datumrodjenja, pol, jmbg, mestoRodjenja, opstinaRodjenja, drzava, brojTelefona,
				email);
		// TODO Auto-generated constructor stub
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOdbornickaGrupa() {
		return odbornickaGrupa;
	}
	public void setOdbornickaGrupa(String odbornickaGrupa) {
		this.odbornickaGrupa = odbornickaGrupa;
	}
	public String getStranka() {
		return stranka;
	}
	public void setStranka(String stranka) {
		this.stranka = stranka;
	}
	public double getPlata() {
		return plata;
	}
	public void setPlata(double plata) {
		this.plata = plata;
	}
	public String getZvanje() {
		return zvanje;
	}
	public void setZvanje(String zvanje) {
		this.zvanje = zvanje;
	}
	
	
	
}
