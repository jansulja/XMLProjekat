package model;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "gradjanin")
@DiscriminatorValue("O")
public class Odbornik extends Gradjanin implements Serializable{
	
	private String odbornickaGrupa;
	private String stranka;
	private double plata;
	private String zvanje;
	
	
	
	public Odbornik() {
		super();
		// TODO Auto-generated constructor stub
	}
	
//	public Integer getId() {
//		return id;
//	}
//	public void setId(Integer id) {
//		this.id = id;
//	}
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
