package daodb4o;

import java.util.List;

import com.db4o.query.Query;

import modelo.IngressoGrupo;

public class DAOIngressoGrupo extends DAO<IngressoGrupo> {
	
	public IngressoGrupo read (Object chave){
		// TODO descobrir a chave primária e alterar neste arquivo

		String email = (String) chave;	//casting para o tipo da chave
		Query q = manager.query();
		q.constrain(IngressoGrupo.class);
		q.descend("email").constrain(email);
		List<IngressoGrupo> resultados = q.execute();
		if (resultados.size()>0)
			return resultados.get(0);
		else
			return null;
	}

}
