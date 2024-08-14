package ifmt.cba.persistencia;

import java.time.LocalDate;
import java.util.List;

import ifmt.cba.dto.MovimentoEstoqueDTO;
import ifmt.cba.entity.Produto;
import ifmt.cba.entity.RegistroEstoque;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

public class RegistroEstoqueDAO extends DAO<RegistroEstoque> {

    public RegistroEstoqueDAO(EntityManager entityManager) throws PersistenciaException {
        super(entityManager);
    }

    public RegistroEstoque buscarPorCodigo(int codigo) throws PersistenciaException {

        RegistroEstoque registro = null;

        try {
            registro = this.entityManager.find(RegistroEstoque.class, codigo);
        } catch (Exception ex) {
            throw new PersistenciaException("Erro na selecao por codigo - " + ex.getMessage());
        }
        return registro;
    }

    @SuppressWarnings("unchecked")
    public List<RegistroEstoque> buscarPorMovimento(MovimentoEstoqueDTO movimento) throws PersistenciaException {
        List<RegistroEstoque> listaRegistro = null;
        try {
            Query query = this.entityManager
                    .createQuery("SELECT re FROM RegistroEstoque re WHERE re.movimento = :pMovimento ORDER BY re.data DESC");
            query.setParameter("pMovimento", movimento);
            listaRegistro = query.getResultList();
        } catch (Exception ex) {
            throw new PersistenciaException("Erro na selecao por movimento do registro de estoque - " + ex.getMessage());
        }
        return listaRegistro;
    }

    @SuppressWarnings("unchecked")
    public List<RegistroEstoque> buscarPorMovimentoEData(MovimentoEstoqueDTO movimento, LocalDate data) throws PersistenciaException {
        List<RegistroEstoque> listaRegistro = null;
        try {
            Query query = this.entityManager
                    .createQuery("SELECT re FROM RegistroEstoque re WHERE re.movimento = :pMovimento AND re.data = :pData ORDER BY re.data DESC");
            query.setParameter("pMovimento", movimento);
            query.setParameter("pData", data);
            listaRegistro = query.getResultList();
        } catch (Exception ex) {
            throw new PersistenciaException("Erro na selecao por movimento do registro de estoque - " + ex.getMessage());
        }
        return listaRegistro;
    }

    @SuppressWarnings("unchecked")
    public List<RegistroEstoque> buscarPorProduto(Produto produto) throws PersistenciaException {
        List<RegistroEstoque> listaRegistro = null;
        try {
            Query query = this.entityManager
                    .createQuery("SELECT re FROM RegistroEstoque re WHERE re.produto.codigo = :pProduto ORDER BY re.data DESC");
            query.setParameter("pProduto", produto.getCodigo());
            listaRegistro = query.getResultList();
        } catch (Exception ex) {
            throw new PersistenciaException("Erro na selecao por produto - " + ex.getMessage());
        }
        return listaRegistro;
    }
}
