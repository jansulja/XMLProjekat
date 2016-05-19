package interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import model.Gradjanin;
import rs.ac.uns.ftn.xws.util.ServiceException;
import util.AuthenticateGradjanin;

@Interceptor
@AuthenticateGradjanin
public class AuthenticationGradjaninInterceptor {

	public AuthenticationGradjaninInterceptor() {
		super();
	}

	private static Logger log = Logger.getLogger(AuthenticationGradjaninInterceptor.class);

	@Context
	private HttpServletRequest request;

	@AroundInvoke
	public Object intercept(InvocationContext context) throws Exception{
		Gradjanin user = (Gradjanin) request.getSession().getAttribute("user");
		log.info("user: "+user);
		if (user == null) {
			throw new ServiceException("Not logged in", Status.UNAUTHORIZED);
		}	
		
		Object result = context.proceed();
		return result;
	}

	

}
