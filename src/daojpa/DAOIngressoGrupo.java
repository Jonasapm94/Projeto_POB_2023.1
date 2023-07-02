package daojpa;

import java.util.List;

import com.db4o.query.Query;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import modelo.Ingresso;
import modelo.IngressoGrupo;
import modelo.IngressoIndividual;

public class DAOIngressoGrupo extends DAO<IngressoGrupo> {
	
	public IngressoGrupo read (Object chave){
		// TODO descobrir a chave primária e alterar neste arquivo

		try {
			int codigo = (int) chave;	//casting para o tipo da chave
			TypedQuery<IngressoGrupo> q = manager.createQuery("select i from Ingresso i where i.codigo = :n ",IngressoGrupo.class);
			q.setParameter("n", codigo);
			return q.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}
	
	//Consulta 3: Quantos ingressos-grupo foram vendidos por time
//	public List<IngressoGrupo> listarIngressosGrupoTime(String nomeTime){
//		Query q = manager.query();
//		q.constrain(IngressoGrupo.class);
//		q.descend("jogos").descend("time1").descend("nome").constrain(nomeTime).or(q.descend("jogos").descend("time2").descend("nome").constrain(nomeTime));
//		List<IngressoGrupo> resultado = q.execute();
//		return resultado;
//	}


}
