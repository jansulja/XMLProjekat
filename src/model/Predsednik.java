package model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;


@Entity
@Table(name = "predsednik")
@PrimaryKeyJoinColumn(name="GRADJANINID")
public class Predsednik extends Odbornik{

	
	
	
}
