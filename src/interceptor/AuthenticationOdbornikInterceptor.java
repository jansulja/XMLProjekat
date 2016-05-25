package interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import model.Odbornik;
import rs.ac.uns.ftn.xws.util.ServiceException;
import util.AuthenticateOdbornik;

@Interceptor
@AuthenticateOdbornik
public class AuthenticationOdbornikInterceptor {

	public AuthenticationOdbornikInterceptor() {
		super();
	}

	private static Logger log = Logger.getLogger(AuthenticationOdbornikInterceptor.class);

	@Context
	private HttpServletRequest request;

	@AroundInvoke
	public Object intercept(InvocationContext context) throws Exception{
		Object user = request.getSession().getAttribute("user");
		log.info("user: "+user);
		if (user == null) {
			throw new ServiceException("Not logged in", Status.UNAUTHORIZED);
		}else if(!(user instanceof Odbornik)){
			throw new ServiceException("Not allowed", Status.UNAUTHORIZED);
		}	
		
		Object result = context.proceed();
		return result;
	}

	

}
