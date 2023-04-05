package daodb4o;

import java.util.List;

import com.db4o.query.Query;

import modelo.IngressoIndividual;

public class DAOIngressoIndividual extends DAO<IngressoIndividual> {
	
	public IngressoIndividual read (Object chave){
		// TODO descobrir a chave primária e alterar neste arquivo

		String email = (String) chave;	//casting para o tipo da chave
		Query q = manager.query();
		q.constrain(IngressoIndividual.class);
		q.descend("email").constrain(email);
		List<IngressoIndividual> resultados = q.execute();
		if (resultados.size()>0)
			return resultados.get(0);
		else
			return null;
	}

}
