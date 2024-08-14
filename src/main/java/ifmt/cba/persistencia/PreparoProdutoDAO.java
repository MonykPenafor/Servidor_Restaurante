package ifmt.cba.persistencia;

import java.util.List;

import ifmt.cba.entity.PreparoProduto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.Query;

public class PreparoProdutoDAO extends DAO<PreparoProduto> {

	public PreparoProdutoDAO(EntityManager entityManager) throws PersistenciaException {
		super(entityManager);
	}

	public PreparoProduto buscarPorCodigo(int codigo) throws PersistenciaException {

		PreparoProduto preparoProduto = null;

		try {
			preparoProduto = this.entityManager.find(PreparoProduto.class, codigo);
		} catch (Exception ex) {
			throw new PersistenciaException("Erro na selecao por codigo - " + ex.getMessage());
		}
		return preparoProduto;
	}

	@SuppressWarnings("unchecked")
	public List<PreparoProduto> buscarPorParteNome(String nome) throws PersistenciaException {
		List<PreparoProduto> listaPreparoProduto;
		try {
			Query query = this.entityManager
					.createQuery("SELECT pp FROM PreparoProduto pp WHERE UPPER(pp.nome) LIKE :pNome ORDER BY pp.nome");
			query.setParameter("pNome", "%" + nome.toUpperCase().trim() + "%");
			listaPreparoProduto = query.getResultList();
		} catch (Exception ex) {
			throw new PersistenciaException("Erro na selecao por parte do nome - " + ex.getMessage());
		}
		return listaPreparoProduto;
	}

	@SuppressWarnings("unchecked")
	public List<PreparoProduto> buscarPorProduto(int codigoProduto) throws PersistenciaException {
		List<PreparoProduto> listaPreparoProduto;
		try {
			Query query = this.entityManager
					.createQuery("SELECT pp FROM PreparoProduto pp WHERE pp.produto.codigo = :codproduto");
			query.setParameter("codproduto", codigoProduto);
			listaPreparoProduto = query.getResultList();
		} catch (Exception ex) {
			throw new PersistenciaException("Erro na selecao por produto - " + ex.getMessage());
		}
		return listaPreparoProduto;
	}

	@SuppressWarnings("unchecked")
	public List<PreparoProduto> buscarPorTipoPreparo(int codigoTipoPreparo) throws PersistenciaException {
		List<PreparoProduto> listaPreparoProduto;
		try {
			Query query = this.entityManager
					.createQuery("SELECT pp FROM PreparoProduto pp WHERE pp.tipoPreparo.codigo = :codtipo");
			query.setParameter("codtipo", codigoTipoPreparo);
			listaPreparoProduto = query.getResultList();
		} catch (Exception ex) {
			throw new PersistenciaException("Erro na selecao por tipo preparo - " + ex.getMessage());
		}
		return listaPreparoProduto;
	}

	public PreparoProduto buscarPorProdutoETipoPreparo(int codigoProduto, int codigoTipoPreparo) throws PersistenciaException {
		PreparoProduto preparoProduto;
		try {
			Query query = this.entityManager
					.createQuery(
							"SELECT pp FROM PreparoProduto pp WHERE pp.produto.codigo = :codproduto AND pp.tipoPreparo.codigo = :codtipo");
			query.setParameter("codproduto", codigoProduto);
			query.setParameter("codtipo", codigoTipoPreparo);
			preparoProduto = (PreparoProduto) query.getSingleResult();
		} catch (NoResultException | NonUniqueResultException ex) {
			preparoProduto = null;
		} catch (Exception ex) {
			preparoProduto = null;
			throw new PersistenciaException("Erro na selecao por produto e tipo preparo - " + ex.getMessage());
		}
		return preparoProduto;
	}

}
