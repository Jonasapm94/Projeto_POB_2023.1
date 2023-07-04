package daojpa;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import modelo.IngressoGrupo;

public class DAOIngressoGrupoJPA extends DAOJPA<IngressoGrupo>{

    @Override
    public IngressoGrupo read(Object chave) {
        try {
            int codigo = (int) chave;
            TypedQuery<IngressoGrupo> q = manager.createQuery("select i from IngressoGrupo i where i.codigo = :cod", IngressoGrupo.class);
            q.setParameter("cod", codigo);
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
}
