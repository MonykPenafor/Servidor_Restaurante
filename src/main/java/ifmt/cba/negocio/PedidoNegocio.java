package ifmt.cba.negocio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import ifmt.cba.dto.ClienteDTO;
import ifmt.cba.dto.EstadoPedidoDTO;
import ifmt.cba.dto.ItemPedidoDTO;
import ifmt.cba.dto.PedidoDTO;
import ifmt.cba.entity.Cliente;
import ifmt.cba.entity.ItemPedido;
import ifmt.cba.entity.Pedido;
import ifmt.cba.persistencia.ClienteDAO;
import ifmt.cba.persistencia.ItemPedidoDAO;
import ifmt.cba.persistencia.PedidoDAO;
import ifmt.cba.persistencia.PersistenciaException;

public class PedidoNegocio {

	private ModelMapper modelMapper;
	private PedidoDAO pedidoDAO;
	private ItemPedidoDAO itemPedidoDAO;
	private ClienteDAO clienteDAO;

	public PedidoNegocio(PedidoDAO pedidoDAO,ItemPedidoDAO itemPedidoDAO, ClienteDAO clienteDAO) {
		this.pedidoDAO = pedidoDAO;
		this.itemPedidoDAO = itemPedidoDAO;
		this.clienteDAO = clienteDAO;
		this.modelMapper = new ModelMapper();
	}

	public void inserir(PedidoDTO pedidoDTO) throws NegocioException {

		Pedido pedido = this.toEntity(pedidoDTO);
		String mensagemErros = pedido.validar();

		if (!mensagemErros.isEmpty()) {
			throw new NegocioException(mensagemErros);
		}

		try {
			pedido.setEstado(EstadoPedidoDTO.REGISTRADO);
			pedidoDAO.beginTransaction();
			pedidoDAO.incluir(pedido);
			pedidoDAO.commitTransaction();
		} catch (PersistenciaException ex) {
			pedidoDAO.rollbackTransaction();
			throw new NegocioException("Erro ao incluir pedido - " + ex.getMessage());
		}
	}

	public void alterar(PedidoDTO pedidoDTO) throws NegocioException {

		Pedido pedido = this.toEntity(pedidoDTO);
		String mensagemErros = pedido.validar();
		if (!mensagemErros.isEmpty()) {
			throw new NegocioException(mensagemErros);
		}
		try {
			if (pedidoDAO.buscarPorCodigo(pedido.getCodigo()) == null) {
				throw new NegocioException("Nao existe esse pedido");
			}
			pedidoDAO.beginTransaction();
			pedidoDAO.alterar(pedido);
			pedidoDAO.commitTransaction();
		} catch (PersistenciaException ex) {
			pedidoDAO.rollbackTransaction();
			throw new NegocioException("Erro ao alterar pedido - " + ex.getMessage());
		}
	}

	public void excluir(PedidoDTO pedidoDTO) throws NegocioException {

		Pedido pedido = this.toEntity(pedidoDTO);
		try {
			if (pedidoDAO.buscarPorCodigo(pedido.getCodigo()) == null) {
				throw new NegocioException("Nao existe esse pedido");
			}
			pedidoDAO.beginTransaction();
			pedidoDAO.excluir(pedido);
			pedidoDAO.commitTransaction();
		} catch (PersistenciaException ex) {
			pedidoDAO.rollbackTransaction();
			throw new NegocioException("Erro ao excluir pedido - " + ex.getMessage());
		}
	}

	public void excluirItemPedido(ItemPedidoDTO itemPedidoDTO) throws NegocioException {

		try {
			ItemPedido itemPedido = itemPedidoDAO.buscarPorCodigo(itemPedidoDTO.getCodigo());
			if (itemPedido == null) {
				throw new NegocioException("Nao existe esse item de pedido");
			}
			itemPedidoDAO.beginTransaction();
			itemPedidoDAO.excluir(itemPedido);
			itemPedidoDAO.commitTransaction();
		} catch (PersistenciaException ex) {
			itemPedidoDAO.rollbackTransaction();
			throw new NegocioException("Erro ao excluir item de pedido - " + ex.getMessage());
		}
	}

	public void alterarItemPedido(ItemPedidoDTO itemPedidoDTO) throws NegocioException {

		ItemPedido itemPedido = this.toItemPedidoEntity(itemPedidoDTO);
		String mensagemErros = itemPedido.validar();
		if (!mensagemErros.isEmpty()) {
			throw new NegocioException(mensagemErros);
		}
		try {
			if (itemPedidoDAO.buscarPorCodigo(itemPedido.getCodigo()) == null) {
				throw new NegocioException("Nao existe esse item de pedido");
			}
			itemPedidoDAO.beginTransaction();
			itemPedidoDAO.alterar(itemPedido);
			itemPedidoDAO.commitTransaction();
		} catch (PersistenciaException ex) {
			itemPedidoDAO.rollbackTransaction();
			throw new NegocioException("Erro ao excluir item de pedido - " + ex.getMessage());
		}
	}

	public PedidoDTO pesquisaCodigo(int codigo) throws NegocioException {
		try {
			Pedido pedido = pedidoDAO.buscarPorCodigo(codigo);
			if (pedido != null) {
				return this.toDTO(pedido);
			} else {
				return null;
			}
		} catch (PersistenciaException ex) {
			throw new NegocioException("Erro ao pesquisar pedido pelo codigo - " + ex.getMessage());
		}
	}


	public List<PedidoDTO> pesquisaPorDataProducao(LocalDate dataInicial, LocalDate dataFinal) throws NegocioException {
		try {
			return this.toDTOAll(pedidoDAO.buscarPorDataPedido(dataInicial, dataFinal));
		} catch (PersistenciaException ex) {
			throw new NegocioException("Erro ao pesquisar pedido por data - " + ex.getMessage());
		}
	}

	public List<PedidoDTO> pesquisaPorEstado(EstadoPedidoDTO estado) throws NegocioException {
		try {
			return this.toDTOAll(pedidoDAO.buscarPorEstado(estado));
		} catch (PersistenciaException ex) {
			throw new NegocioException("Erro ao pesquisar pedido pelo estado - " + ex.getMessage());
		}
	}

	public List<PedidoDTO> pesquisaPorEstadoEData(EstadoPedidoDTO estado, LocalDate data) throws NegocioException {
		try {
			return this.toDTOAll(pedidoDAO.buscarPorEstadoEData(estado, data));
		} catch (PersistenciaException ex) {
			throw new NegocioException("Erro ao pesquisar pedido pelo estado e data - " + ex.getMessage());
		}
	}

	public List<PedidoDTO> pesquisaPorCliente(ClienteDTO clienteDTO) throws NegocioException {

		try {
			Cliente cliente = this.clienteDAO.buscarPorCodigo(clienteDTO.getCodigo());
			if (cliente == null) {
				throw new NegocioException("Cliente nao existe");
			}
			return this.toDTOAll(pedidoDAO.buscarPorCliente(cliente));
		} catch (PersistenciaException ex) {
			throw new NegocioException("Erro ao pesquisar pedido pelo cliente - " + ex.getMessage());
		}
	}

	public void mudarPedidoParaProducao(PedidoDTO pedidoDTO) throws NegocioException {

		if (pedidoDTO.getEstado().equals(EstadoPedidoDTO.REGISTRADO)) {
			pedidoDTO.setEstado(EstadoPedidoDTO.PRODUCAO);
			this.alterar(pedidoDTO);
		} else {
			throw new NegocioException(
					"Pedido esta no estado: " + pedidoDTO.getEstado() + ", nao pode mudar para Producao");
		}
	}

	public void mudarPedidoParaPronto(PedidoDTO pedidoDTO) throws NegocioException {

		if (pedidoDTO.getEstado().equals(EstadoPedidoDTO.PRODUCAO)) {
			pedidoDTO.setEstado(EstadoPedidoDTO.PRONTO);
			this.alterar(pedidoDTO);
		} else {
			throw new NegocioException(
					"Pedido esta no estado: " + pedidoDTO.getEstado() + ", nao pode mudar para Pronto");
		}
	}

	public void mudarPedidoParaEntrega(PedidoDTO pedidoDTO) throws NegocioException {

		if (pedidoDTO.getEstado().equals(EstadoPedidoDTO.PRONTO)) {
			pedidoDTO.setEstado(EstadoPedidoDTO.ENTREGA);
			this.alterar(pedidoDTO);
		} else {
			throw new NegocioException(
					"Pedido esta no estado: " + pedidoDTO.getEstado() + ", nao pode mudar para Entrega");
		}
	}

	public void mudarPedidoParaConcluido(PedidoDTO pedidoDTO) throws NegocioException {

		if (pedidoDTO.getEstado().equals(EstadoPedidoDTO.ENTREGA)) {
			pedidoDTO.setEstado(EstadoPedidoDTO.CONCLUIDO);
			this.alterar(pedidoDTO);
		} else {
			throw new NegocioException(
					"Pedido esta no estado: " + pedidoDTO.getEstado() + ", nao pode mudar para Concluido");
		}
	}

	public List<PedidoDTO> toDTOAll(List<Pedido> listaPedido) {
		List<PedidoDTO> listaDTO = new ArrayList<PedidoDTO>();

		for (Pedido pedido : listaPedido) {
			listaDTO.add(this.toDTO(pedido));
		}
		return listaDTO;
	}

	public PedidoDTO toDTO(Pedido pedido) {
		return this.modelMapper.map(pedido, PedidoDTO.class);
	}

	public Pedido toEntity(PedidoDTO pedidoDTO) {
		return this.modelMapper.map(pedidoDTO, Pedido.class);
	}

	public ItemPedidoDTO toItemPeditoDTO(ItemPedido itemPedido) {
		return this.modelMapper.map(itemPedido, ItemPedidoDTO.class);
	}

	public ItemPedido toItemPedidoEntity(ItemPedidoDTO itemPedidoDTO) {
		return this.modelMapper.map(itemPedidoDTO, ItemPedido.class);
	}
}
