package session;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import model.Ovlascenje;
import rs.ac.uns.ftn.xws.sessionbeans.common.GenericDaoBean;

@Stateless
@Local(OvlascenjeDaoLocal.class)
public class OvlascenjeDaoBean extends GenericDaoBean<Ovlascenje, Integer> implements OvlascenjeDaoLocal {

	

}
