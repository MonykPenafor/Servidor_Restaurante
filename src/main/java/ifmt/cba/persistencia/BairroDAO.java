package ifmt.cba.persistencia;

import java.util.List;

import ifmt.cba.entity.Bairro;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

public class BairroDAO extends DAO<Bairro>{

   public BairroDAO(EntityManager entityManager) throws PersistenciaException {
        super(entityManager);
    }

    public Bairro buscarPorCodigo(int codigo) throws PersistenciaException {
        Bairro Bairro = null;

        try {
            Bairro = this.entityManager.find(Bairro.class, codigo);
        } catch (Exception ex) {
            throw new PersistenciaException("Erro na selecao por codigo - " + ex.getMessage());
        }
        return Bairro;
    }

    @SuppressWarnings("unchecked")
    public List<Bairro> buscarPorParteNome(String nome) throws PersistenciaException {
        List<Bairro> listaBairro;
        try {
            Query query = this.entityManager
                    .createQuery("SELECT b FROM Bairro b WHERE UPPER(b.nome) LIKE :pNome ORDER BY b.nome");
            query.setParameter("pNome", "%" + nome.toUpperCase().trim() + "%");
            listaBairro = query.getResultList();
        } catch (Exception ex) {
            throw new PersistenciaException("Erro na selecao por parte do nome - " + ex.getMessage());
        }
        return listaBairro;
    }
}
