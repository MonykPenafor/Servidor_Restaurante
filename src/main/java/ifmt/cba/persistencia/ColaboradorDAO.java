package ifmt.cba.persistencia;

import java.util.List;
import ifmt.cba.entity.Colaborador;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

public class ColaboradorDAO extends DAO<Colaborador> {

    public ColaboradorDAO(EntityManager entityManager) throws PersistenciaException {
        super(entityManager);
    }

    public Colaborador buscarPorCodigo(int codigo) throws PersistenciaException {

        Colaborador colaborador = null;

        try {
            colaborador = this.entityManager.find(Colaborador.class, codigo);
        } catch (Exception ex) {
            throw new PersistenciaException("Erro na selecao por codigo - " + ex.getMessage());
        }
        return colaborador;
    }

    public Colaborador buscarPorCPF(String CPF) throws PersistenciaException {
        Colaborador colaborador = null;
        try {
            Query query = this.entityManager
                    .createQuery("SELECT c FROM Colaborador c WHERE UPPER(c.CPF) = :pCPF");
            query.setParameter("pCPF", CPF.toUpperCase().trim());
            colaborador = (Colaborador) query.getSingleResult();
        } catch (Exception ex) {
            throw new PersistenciaException("Erro na selecao por CPF - " + ex.getMessage());
        }
        return colaborador;
    }

    @SuppressWarnings("unchecked")
    public List<Colaborador> buscarPorParteNome(String nome) throws PersistenciaException {
        List<Colaborador> listaColaborador;
        try {
            Query query = this.entityManager
                    .createQuery("SELECT c FROM Colaborador c WHERE UPPER(c.nome) LIKE :pNome ORDER BY c.nome");
            query.setParameter("pNome", "%" + nome.toUpperCase().trim() + "%");
            listaColaborador = query.getResultList();
        } catch (Exception ex) {
            throw new PersistenciaException("Erro na selecao por parte do nome - " + ex.getMessage());
        }
        return listaColaborador;
    }

}
