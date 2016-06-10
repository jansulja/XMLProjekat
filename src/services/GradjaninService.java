package services;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import model.Gradjanin;
import rs.ac.uns.ftn.xws.entities.payments.Invoice;
import rs.ac.uns.ftn.xws.entities.users.User;
import rs.ac.uns.ftn.xws.util.Authenticate;
import rs.ac.uns.ftn.xws.util.ServiceException;
import session.GradjaninDaoLocal;
import session.OdbornikDaoLocal;
import util.AuthenticateOdbornik;

@Path("/gradjanin")
public class GradjaninService {

	private static Logger log = Logger.getLogger(Gradjanin.class);

	// private static Logger log = Logger.getLogger(Gradjanin.class);
	//
	// @EJB
	// private GradjaninDaoLocal gradjaninDao;
	//
	// @GET
	// @Produces(MediaType.APPLICATION_JSON)
	// public List<Gradjanin> findByAll() {
	// List<Gradjanin> retVal = null;
	// try {
	// retVal = gradjaninDao.findAll();
	// } catch (Exception e) {
	// log.error(e.getMessage(), e);
	// }
	// return retVal;
	// }

	@Context
	private HttpServletRequest request;

	@EJB
	OdbornikDaoLocal odbornikDao;

	@GET
    @Path("current")
    @Produces(MediaType.APPLICATION_JSON)
	public Object getCurrentUser(){

		return request.getSession().getAttribute("user");

	}

	@GET
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public Object loginGet(@QueryParam("username") String username, @QueryParam("password") String password) {
    	Gradjanin user = null;
		try {
			user = odbornikDao.login(username, password);

			if(user == null){

				user = gradjaninDao.login(username, password);
			}
        } catch (Exception e) {
        	log.error(e.getMessage(), e);
        }
		if(user==null){
			throw new ServiceException("Wrong username or password", Status.FORBIDDEN);
		}
		log.info("USER: "+user);
    	return user;
    }

	@POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Object login(Gradjanin sentUser) {
    	Gradjanin user = null;
		try {
				user = odbornikDao.login(sentUser.getEmail(), sentUser.getPassword());

				if(user == null){

					user = gradjaninDao.login(sentUser.getEmail(), sentUser.getPassword());
				}


        } catch (Exception e) {
        	log.error(e.getMessage(), e);
        }
		if(user==null){//ako ne uspe prijava posalje se greska 403 - znam sta hoces, ali ti ne dozvoljavam
			throw new ServiceException("Wrong username or password", Status.FORBIDDEN);
		}
    	return user;
    }

	@GET
    @Path("logout")
    @Produces(MediaType.TEXT_HTML)
    public String logout() {
		try {
			gradjaninDao.logout();
        } catch (Exception e) {
        	log.error(e.getMessage(), e);
        }
    	return "ok";
    }



	@EJB
	GradjaninDaoLocal gradjaninDao;

	//Neko gadja ovaj servis....
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public Gradjanin init() {
		return new Gradjanin();
    }

	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public List<Gradjanin> findByAll() {
		List<Gradjanin> retVal = null;
		try {
			retVal = gradjaninDao.findAll();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return retVal;
    }

	@GET
	@Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
	@Authenticate
    public Gradjanin findById(@PathParam("id") String id) {
		Gradjanin retVal = null;
		try {
			retVal = gradjaninDao.findById(Integer.parseInt(id));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return retVal;
    }

	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Gradjanin create(Gradjanin entity) {
		log.info("POST");
    	Gradjanin retVal = null;
		try {
			System.out.println("entity: "+entity);
			retVal = gradjaninDao.persist(entity);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return retVal;
    }


	@PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Gradjanin update(Gradjanin entity) {
    	log.info("PUT");
    	Gradjanin retVal = null;
        try {
        	retVal = gradjaninDao.merge(entity);
        } catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return retVal;
    }

//	@GET
//	@Produces("text/html")
//	public void read(@Context ServletContext context, @Context HttpServletRequest request,
//			@Context HttpServletResponse response) {
//
//		List<Gradjanin> gradjani = new ArrayList<Gradjanin>();
//		gradjani = gradjaninDao.findAll();
//
//		try {
//			request.setAttribute("gradjanin", gradjaninDao.findAll());
//
//			context.getRequestDispatcher("/gradjaninRead.jsp").forward(request, response);
//		} catch (ServletException | IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//
//
//		return;
//
//	}

	@GET
	@Path("/create")
	public Response create() {

		java.net.URI location = null;
		try {
			location = new java.net.URI("../createGradjanin.jsp");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.temporaryRedirect(location).build();

	}

//	@POST
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	@Path("/create")
//	public Response create(@FormParam("ime") String ime, @FormParam("prezime") String prezime,
//			@FormParam("brojLicne") String brojLicne, @FormParam("datumrodjenja") String datumrodjenja,
//			@FormParam("pol") String pol, @FormParam("JMBG") String JMBG,
//			@FormParam("mestoRodjenja") String mestoRodjenja, @FormParam("opstinaRodjenja") String opstinaRodjenja,
//			@FormParam("drzava") String drzava,
//			@FormParam("brojTelefona") String brojTelefona,
//			@FormParam("email") String email) {
//
//
//		Gradjanin gradjanin = new Gradjanin();
//		gradjanin.setIme(ime);
//		gradjanin.setPrezime(prezime);
//		gradjanin.setBrojlicne(Integer.parseInt(brojLicne));
//		gradjanin.setBrojTelefona(Integer.parseInt(brojTelefona));
//
//		SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
//
//			try {
//				gradjanin.setDatumrodjenja(sdf.parse(datumrodjenja));
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		gradjanin.setDrzava(drzava);
//		gradjanin.setJMBG(Integer.parseInt(JMBG));
//		gradjanin.setEmail(email);
//		gradjanin.setPol(pol);
//		gradjanin.setMestoRodjenja(mestoRodjenja);
//		gradjanin.setOpstinaRodjenja(opstinaRodjenja);
//
//		log.info(gradjanin.toString());
//
//		try {
//			gradjaninDao.persist(gradjanin);
//		} catch (NoSuchFieldException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		URI uri = UriBuilder.fromPath("/gradjanin").build();
//	    return Response.seeOther(uri).build();
//
//	}


	@DELETE
    @Path("{id}")
    @Produces(MediaType.TEXT_HTML)
	public String removeItem(@PathParam("id") Integer id) {
    	try {
        	gradjaninDao.remove(id);
        } catch (Exception e) {
        	log.error(e.getMessage(), e);
        }
    	return "ok";
    }

}
