package ifmt.cba.persistencia;

import java.time.LocalDate;
import java.util.List;

import ifmt.cba.dto.EstadoPedidoDTO;
import ifmt.cba.entity.Cliente;
import ifmt.cba.entity.Pedido;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

public class PedidoDAO extends DAO<Pedido> {

	public PedidoDAO(EntityManager entityManager) throws PersistenciaException {
		super(entityManager);
	}

	public Pedido buscarPorCodigo(int codigo) throws PersistenciaException {

		Pedido pedido = null;

		try {
			pedido = this.entityManager.find(Pedido.class, codigo);
		} catch (Exception ex) {
			throw new PersistenciaException("Erro na selecao por codigo - " + ex.getMessage());
		}
		return pedido;
	}

	@SuppressWarnings("unchecked")
	public List<Pedido> buscarPorEstado(EstadoPedidoDTO estado) throws PersistenciaException {
		List<Pedido> listaPedido = null;
		try {
			Query query = this.entityManager
					.createQuery("SELECT p FROM Pedido p WHERE p.estado = :pEstado ORDER BY p.dataPedido DESC");
			query.setParameter("pEstado", estado);
			listaPedido = query.getResultList();
		} catch (Exception ex) {
			throw new PersistenciaException("Erro na selecao por estado do pedido - " + ex.getMessage());
		}
		return listaPedido;
	}

	@SuppressWarnings("unchecked")
	public List<Pedido> buscarPorEstadoEData(EstadoPedidoDTO estado, LocalDate data) throws PersistenciaException {
		List<Pedido> listaPedido = null;
		try {
			Query query = this.entityManager
					.createQuery("SELECT p FROM Pedido p WHERE p.estado = :pEstado AND p.dataPedido = :pData ORDER BY p.dataPedido DESC");
			query.setParameter("pEstado", estado);
			query.setParameter("pData", data);
			listaPedido = query.getResultList();
		} catch (Exception ex) {
			throw new PersistenciaException("Erro na selecao por estado e data do pedido - " + ex.getMessage());
		}
		return listaPedido;
	}

	@SuppressWarnings("unchecked")
	public List<Pedido> buscarPorCliente(Cliente cliente) throws PersistenciaException {
		List<Pedido> listaPedido = null;
		try {
			Query query = this.entityManager
					.createQuery("SELECT p FROM Pedido p WHERE p.cliente.codigo = :pCliente");
			query.setParameter("pCliente", cliente.getCodigo());
			listaPedido = query.getResultList();
		} catch (Exception ex) {
			throw new PersistenciaException("Erro na selecao por cliente - " + ex.getMessage());
		}
		return listaPedido;
	}

	@SuppressWarnings("unchecked")
	public List<Pedido> buscarPorDataPedido(LocalDate dataInicial, LocalDate dataFinal) throws PersistenciaException {
		List<Pedido> listaPedido = null;
		try {
			Query query = this.entityManager
					.createQuery(
							"SELECT p FROM Pedido p WHERE p.dataPedido >= :pDataInicial AND p.dataPedido <= :pDataFinal");
			query.setParameter("pDataInicial", dataInicial);
			query.setParameter("pDataFinal", dataFinal);
			listaPedido = query.getResultList();
		} catch (Exception ex) {
			throw new PersistenciaException("Erro na selecao por data de pedido - " + ex.getMessage());
		}
		return listaPedido;
	}

}
