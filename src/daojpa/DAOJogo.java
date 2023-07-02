package daojpa;

import java.util.List;

import java.util.ArrayList;

import com.db4o.query.Query;

import modelo.Jogo;

public class DAOJogo extends DAO<Jogo>{

	public Jogo read (Object chave){

		int id = (Integer) chave;	//casting para o tipo da chave
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
	
	//	Consulta 1: Quais as datas que time x joga
	
	public List<Jogo> dataTimeJoga(String nomeTime){
		Query q = manager.query();
		q.constrain(Jogo.class);
		q.descend("time1").descend("nome").constrain(nomeTime).or(q.descend("time2").descend("nome").constrain(nomeTime));
		List<Jogo> resultado = q.execute();
		return resultado;
	}
}

