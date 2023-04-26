package daodb4o;

import java.util.List;

import com.db4o.query.Query;

import modelo.Time;

public class DAOTime extends DAO<Time> {
	
	public Time read (Object chave){

		String nome = (String) chave;	//casting para o tipo da chave
		Query q = manager.query();
		q.constrain(Time.class);
		q.descend("nome").constrain(nome);
		List<Time> resultados = q.execute();
		if (resultados.size()>0)
			return resultados.get(0);
		else
			return null;
	}

	public List<Time> listarTimes(){
		Query q = manager.query();
		q.constrain(Time.class);
		return q.execute();

	}

    public List<Time> listarTimesJogandoPorData(String data) {
        Query q = manager.query();
		q.constrain(Time.class);
		q.descend("jogos").descend("data").constrain(data);
		return q.execute();
    }



}
