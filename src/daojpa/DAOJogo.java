package daojpa;

import java.util.List;

import java.util.ArrayList;

import com.db4o.query.Query;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import modelo.Jogo;

public class DAOJogo extends DAO<Jogo>{

	public Jogo read (Object chave){

		 try {
			 int id = (Integer) chave;	//casting para o tipo da chave
		     TypedQuery<Jogo> q = manager.createQuery("select j from Jogo j where j.id = :i", Jogo.class);
		     q.setParameter("i", id);
		     Jogo jogo = q.getSingleResult();
		     return jogo;
		 }	catch (NoResultException e) {
			 return null;      
	    }
	}

	//--------------------------------------------
	//  consultas
	//--------------------------------------------

	public List<Jogo> listarJogos(){
		TypedQuery<Jogo> q = manager.createQuery("select j from Jogo j", Jogo.class);
		return q.getResultList();
	}
//
//	public List<Jogo> listarJogos(String data){
//		Query q = manager.query();
//		q.constrain(Jogo.class);
//		q.descend("data").constrain(data);
//		return q.execute();
//	}
	
	//	Consulta 1: Quais as datas que um time x joga?
	
//	public List<Jogo> dataTimeJoga(String nomeTime){
//		Query q = manager.query();
//		q.constrain(Jogo.class);
//		q.descend("time1").descend("nome").constrain(nomeTime).or(q.descend("time2").descend("nome").constrain(nomeTime));
//		List<Jogo> resultado = q.execute();
//		return resultado;
//	}
}

