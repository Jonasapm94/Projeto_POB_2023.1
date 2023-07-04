package daojpa;

import java.util.List;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import modelo.Usuario;

public class DAOUsuarioJPA extends DAOJPA<Usuario> {

	public Usuario read(Object chave) {
		try {
			String email = (String) chave;
			
			TypedQuery<Usuario> q = manager.createQuery("select u from Usuario u where u.email = :email",Usuario.class);
			q.setParameter("email", email);
			return q.getSingleResult();
		}catch (NoResultException e){
			return null;
		}
	}

	public List<Usuario> listarUsuarios() {
		try {
			TypedQuery<Usuario> q = manager.createQuery("select u from Usuario", Usuario.class);
			return q.getResultList();
		} catch (NoResultException e) {
			// TODO: handle exception
			return null;
		}
	}

	
}
