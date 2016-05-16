package session;

import javax.ejb.Local;
import javax.ejb.Stateless;

import model.Gradjanin;
import rs.ac.uns.ftn.xws.sessionbeans.common.GenericDaoBean;


@Stateless
@Local(GradjaninDaoLocal.class)
public class GradjaninDaoBean extends GenericDaoBean<Gradjanin, Integer>
		implements GradjaninDaoLocal {
	
	
	
}