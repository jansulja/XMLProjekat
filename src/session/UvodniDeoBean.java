package session;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import model.UvodniDeo;
import rs.ac.uns.ftn.xws.sessionbeans.common.GenericDaoBean;

@Stateless
@Local(UvodniDeoLocal.class)
public class UvodniDeoBean extends GenericDaoBean<UvodniDeo, Integer> implements UvodniDeoLocal {

	
}
