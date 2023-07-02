package daojpa;

import java.util.List;
import java.util.ArrayList;

import com.db4o.query.Query;

import modelo.Ingresso;
import modelo.IngressoGrupo;
import modelo.IngressoIndividual;

public class DAOIngresso extends DAO<Ingresso> {
	
	public Ingresso read (Object chave){

		int codigo = (int) chave;	//casting para o tipo da chave
		Query q = manager.query();
		q.constrain(Ingresso.class);
		q.descend("codigo").constrain(codigo);
		List<Ingresso> resultados = q.execute();
		if (resultados.size()>0)
			return resultados.get(0);
		else
			return null;
	}

	public List<Ingresso> listarIngressos(){
		Query q = manager.query();
		q.constrain(Ingresso.class);
		return q.execute();
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
