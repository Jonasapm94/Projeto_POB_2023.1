package daojpa;

import java.util.List;
import java.util.ArrayList;

import com.db4o.query.Query;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import modelo.Ingresso;
import modelo.IngressoGrupo;
import modelo.IngressoIndividual;

public class DAOIngresso extends DAO<Ingresso> {
	
	public Ingresso read (Object chave){
		
		try {
			int codigo = (int) chave;	//casting para o tipo da chave
			TypedQuery<Ingresso> q = manager.createQuery("select i from Ingresso i where i.codigo = :n ",Ingresso.class);
			q.setParameter("n", codigo);
			return q.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}

	public List<Ingresso> listarIngressos(){
		TypedQuery<Ingresso> q = manager.createQuery("select i from Ingresso i", Ingresso.class);
		List<Ingresso> ingressos = q.getResultList();
		return ingressos;
	}
	
	//Consulta 5: Quais os códigos dos ingressos de todos os jogos de um time?
	
	public List<Ingresso> ingressosTime(String nomeTime){
		List<Ingresso> resultado = new ArrayList<Ingresso>();
		Query q = manager.query();
		q.constrain(IngressoIndividual.class);
		q.descend("jogo").descend("time1").descend("nome").constrain(nomeTime).or(q.descend("jogo").descend("time2").descend("nome").constrain(nomeTime));
		resultado.addAll(q.execute());
		q = manager.query();
		q.constrain(IngressoGrupo.class);
		q.descend("jogos").descend("time1").descend("nome").constrain(nomeTime).or(q.descend("jogos").descend("time2").descend("nome").constrain(nomeTime));
		resultado.addAll(q.execute());
		return resultado;
	}

}
