package services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import model.Gradjanin;
import rs.ac.uns.ftn.xws.util.Authenticate;
import util.AuthenticateOdbornik;
import xml.signature.SignDocument;

@Path("/amandman")
public class AmandmanService {

	private static Logger log = Logger.getLogger(AmandmanService.class);

	@Context
	private ServletContext context;

	@Context
	private HttpServletRequest request;

	@POST
	@Path("/new")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	@Authenticate
	@AuthenticateOdbornik
	public String predloziAmandman(String amandman){

		String signedAmandman = signXmlDocument(amandman);

		Random rand = new Random();

		InputStream stream = new ByteArrayInputStream(signedAmandman.getBytes(StandardCharsets.UTF_8));

		AktService.insertDocument("/amandmani/"+(String.valueOf(rand.nextInt(10000))) + ".xml", stream);

		return "ok";


	}

	private String signXmlDocument(String document) {

		String signedDocument = null;
		Gradjanin currentUser = (Gradjanin) request.getSession().getAttribute("user");
		SignDocument sd = new SignDocument(document,currentUser.getEmail(),currentUser.getEmail());

		try {
			signedDocument = sd.sign();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return signedDocument;
	}



	@Produces(MediaType.TEXT_HTML)
	@Path("/test")
	@GET
	public void test(){



	}


}
