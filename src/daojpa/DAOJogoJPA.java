package daojpa;

import java.util.List;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import modelo.Jogo;

public class DAOJogoJPA extends DAOJPA<Jogo>{

    @Override
    public Jogo read(Object chave) {
        try {
            int id = (int) chave;
            TypedQuery<Jogo> q = manager.createQuery("select j from Jogo j where j.id = :id",Jogo.class);
            q.setParameter("id",id);
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Jogo> listarJogos(){
        try {
            TypedQuery<Jogo> q = manager.createQuery("select j from Jogo j", Jogo.class);
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Jogo> listarJogos(String data){
        try {
            TypedQuery<Jogo> q = manager.createQuery("select j from Jogo j where j.data = :dt", Jogo.class);
            q.setParameter("dt", data);
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
    
}
