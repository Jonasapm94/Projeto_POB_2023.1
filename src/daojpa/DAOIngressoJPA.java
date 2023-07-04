package daojpa;

import java.util.List;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import modelo.Ingresso;

public class DAOIngressoJPA extends DAOJPA<Ingresso> {

    @Override
    public Ingresso read(Object chave) {
        try {
            int codigo = (int) chave;
            TypedQuery<Ingresso> q = manager.createQuery("select i from Ingresso i where i.codigo = :cod", Ingresso.class);
            q.setParameter("cod", codigo);
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Ingresso> listarIngressos() {
        try {
            TypedQuery<Ingresso> q = manager.createQuery("select i from Ingresso i", Ingresso.class);
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
    
}
