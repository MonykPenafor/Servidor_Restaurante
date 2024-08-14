package ifmt.cba.persistencia;

import ifmt.cba.entity.ItemPedido;
import jakarta.persistence.EntityManager;

public class ItemPedidoDAO extends DAO<ItemPedido>{

    public ItemPedidoDAO(EntityManager entityManager) throws PersistenciaException {
		super(entityManager);
	}

	public ItemPedido buscarPorCodigo(int codigo) throws PersistenciaException {

		ItemPedido itemPedido = null;

		try {
			itemPedido = this.entityManager.find(ItemPedido.class, codigo);
		} catch (Exception ex) {
			throw new PersistenciaException("Erro na selecao por codigo - " + ex.getMessage());
		}
		return itemPedido;
	}
}
