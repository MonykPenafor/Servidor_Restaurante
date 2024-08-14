package ifmt.cba.persistencia;

import ifmt.cba.entity.ItemOrdemProducao;
import jakarta.persistence.EntityManager;

public class ItemOrdemProducaoDAO extends DAO<ItemOrdemProducao>{

    public ItemOrdemProducaoDAO(EntityManager entityManager) throws PersistenciaException {
		super(entityManager);
	}

	public ItemOrdemProducao buscarPorCodigo(int codigo) throws PersistenciaException {

		ItemOrdemProducao itemOrdemProducao = null;

		try {
			itemOrdemProducao = this.entityManager.find(ItemOrdemProducao.class, codigo);
		} catch (Exception ex) {
			throw new PersistenciaException("Erro na selecao por codigo - " + ex.getMessage());
		}
		return itemOrdemProducao;
	}
}
