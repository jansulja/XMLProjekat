package model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "gradjanin")
@DiscriminatorValue("P")
public class Predsednik extends Odbornik{

	
	
	
}
