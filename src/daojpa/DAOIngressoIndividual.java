package daojpa;

import java.util.List;

import com.db4o.query.Query;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import modelo.Ingresso;
import modelo.IngressoIndividual;

public class DAOIngressoIndividual extends DAO<IngressoIndividual> {
	
	public IngressoIndividual read (Object chave){
		// TODO descobrir a chave primária e alterar neste arquivo
		try {
			int codigo = (int) chave;	//casting para o tipo da chave
			TypedQuery<IngressoIndividual> q = manager.createQuery("select i from Ingresso i where i.codigo = :n ",IngressoIndividual.class);
			q.setParameter("n", codigo);
			return q.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}
	
	// Consulta 2: Quantos ingressos individuais um determinado time vendeu
//	public List<IngressoIndividual> listarIngressosIndividuaisTime(String nomeTime){
//		Query q = manager.query();
//		q.constrain(IngressoIndividual.class);
//		q.descend("jogo").descend("time1").descend("nome").constrain(nomeTime).or(q.descend("jogo").descend("time2").descend("nome").constrain(nomeTime));
//		List<IngressoIndividual> resultado = q.execute();
//		return resultado;
//	}

}
