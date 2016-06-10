package services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import org.w3c.dom.Document;

import model.Odbornik;
import rs.ac.uns.ftn.xws.util.Authenticate;
import util.AuthenticateOdbornik;
import xml.encryption.DecryptKEK;
import xml.encryption.EncryptKEK;

@Path("/amandman")
public class AmandmanService {

	@POST
	@Path("/new")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	@Authenticate
	@AuthenticateOdbornik
	public String predloziAkt(String amandman){

		Random rand = new Random();

		InputStream stream = new ByteArrayInputStream(amandman.getBytes(StandardCharsets.UTF_8));

		AktService.insertDocument("/amandmani/"+(String.valueOf(rand.nextInt(10000))) + ".xml", stream);

		return "ok";


	}



}
