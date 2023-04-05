package daodb4o;

import java.util.List;

import com.db4o.query.Query;

import modelo.Jogo;

public class DAOJogo extends DAO<Jogo>{

	public Jogo read (Object chave){
		// TODO descobrir a chave primária e alterar neste arquivo
		
		String id = (String) chave;	//casting para o tipo da chave
		Query q = manager.query();
		q.constrain(Jogo.class);
		q.descend("id").constrain(id);
		List<Jogo> resultados = q.execute();
		if (resultados.size()>0)
			return resultados.get(0);
		else
			return null;
	}

	//--------------------------------------------
	//  consultas
	//--------------------------------------------

	public List<Jogo> listarJogos(){
		Query q = manager.query();
		q.constrain(Jogo.class);
		return q.execute();
	}

	public List<Jogo> listarJogos(String data){
		Query q = manager.query();
		q.constrain(Jogo.class);
		q.descend("data").constrain(data);
		return q.execute();
	}
	
}

