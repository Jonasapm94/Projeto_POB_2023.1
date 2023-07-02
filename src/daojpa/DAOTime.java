package daojpa;

import java.util.List;

import com.db4o.query.Query;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import modelo.Ingresso;
import modelo.Time;

public class DAOTime extends DAO<Time> {
	
	public Time read (Object chave){
		
		try {
			String nome = (String) chave;	//casting para o tipo da chave
			TypedQuery<Time> q = manager.createQuery("select t from Time t where t.nome = :n ",Time.class);
			q.setParameter("n", nome);
			return q.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}

	public List<Time> listarTimes(){
		TypedQuery<Time> q = manager.createQuery("select t from Time t",Time.class);
		return q.getResultList();

	}

	//Consulta 4: Quais os Times que jogam em uma determinada Data?

//    public List<Time> listarTimesJogandoPorData(String data) {
//    	TypedQuery<Time> q = manager.createQuery("select t from Time t where t.nome = :n ",Time.class);
//		q.setParameter("n", data);
//		return q.getResultList();
//    }
}
