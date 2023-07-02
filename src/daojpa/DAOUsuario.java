/**********************************
 * IFPB - SI
 * POB - Persistencia de Objetos
 * Prof. Fausto Ayres
 **********************************/

package daojpa;

import java.util.List;

import com.db4o.query.Query;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import modelo.Jogo;
import modelo.Usuario;

public class DAOUsuario extends DAO<Usuario>{

	public Usuario read (Object chave){
		try {
			 String email = (String) chave;	//casting para o tipo da chave
		     TypedQuery<Usuario> q = manager.createQuery("select u from Usuario u where u.id = :e", Usuario.class);
		     q.setParameter("e", email);
		     Usuario usuario = q.getSingleResult();
		     return usuario;
		 }	catch (NoResultException e) {
			 return null;      
	    }
	}

	//--------------------------------------------
	//  consultas
	//--------------------------------------------

	public List<Usuario> listarUsuarios(){
		TypedQuery<Usuario> q = manager.createQuery("select u from Usuario u", Usuario.class);
		return q.getResultList();
	}
	
}

