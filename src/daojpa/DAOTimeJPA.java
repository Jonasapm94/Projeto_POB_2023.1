package daojpa;

import java.util.List;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import modelo.Time;

public class DAOTimeJPA extends DAOJPA<Time>{

    @Override
    public Time read(Object chave) {
        try {
            String nome = (String) chave;
            TypedQuery<Time> q = manager.createQuery("select t from Time t where t.nome = :nomeTime",Time.class);
            q.setParameter("nomeTime", nome);
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Time> listarTimes(){
        try {
            TypedQuery<Time> q = manager.createQuery("select t from Time t", Time.class);
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
    
}
