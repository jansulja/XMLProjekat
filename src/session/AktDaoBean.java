package session;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import model.Akt;
import rs.ac.uns.ftn.xws.sessionbeans.common.GenericDaoBean;
@Stateless
@Local(AktDaoLocal.class)
public class AktDaoBean extends GenericDaoBean<Akt, Integer> implements AktDaoLocal {

	

}
