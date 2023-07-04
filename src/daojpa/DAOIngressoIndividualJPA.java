package daojpa;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import modelo.IngressoIndividual;

public class DAOIngressoIndividualJPA extends DAOJPA<IngressoIndividual> {

    @Override
    public IngressoIndividual read(Object chave) {
        try {
            int codigo = (int) chave;
            TypedQuery<IngressoIndividual> q = manager.createQuery("select i from IngressoIndividual i where i.codigo = :cod ", IngressoIndividual.class);
            q.setParameter("cod", codigo);
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
}
