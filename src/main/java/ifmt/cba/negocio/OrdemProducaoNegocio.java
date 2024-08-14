package ifmt.cba.negocio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.modelmapper.ModelMapper;

import ifmt.cba.dto.EstadoOrdemProducaoDTO;
import ifmt.cba.dto.ItemOrdemProducaoDTO;
import ifmt.cba.dto.OrdemProducaoDTO;
import ifmt.cba.dto.ProdutoDTO;
import ifmt.cba.entity.ItemOrdemProducao;
import ifmt.cba.entity.OrdemProducao;
import ifmt.cba.persistencia.FabricaEntityManager;
import ifmt.cba.persistencia.ItemOrdemProducaoDAO;
import ifmt.cba.persistencia.OrdemProducaoDAO;
import ifmt.cba.persistencia.PersistenciaException;
import ifmt.cba.persistencia.ProdutoDAO;

public class OrdemProducaoNegocio {

	private ModelMapper modelMapper;
	private OrdemProducaoDAO ordemProducaoDAO;
	private ItemOrdemProducaoDAO itemOrdemProducaoDAO;
	private ProdutoNegocio produtoNegocio;

	public OrdemProducaoNegocio(OrdemProducaoDAO ordemProducaoDAO, ItemOrdemProducaoDAO itemOrdemProducaoDAO)
			throws NegocioException {
		try {
			this.ordemProducaoDAO = ordemProducaoDAO;
			this.itemOrdemProducaoDAO = itemOrdemProducaoDAO;
			this.produtoNegocio = new ProdutoNegocio(new ProdutoDAO(FabricaEntityManager.getEntityManagerProducao()));
			this.modelMapper = new ModelMapper();
		} catch (PersistenciaException e) {
			throw new NegocioException("Erro ao iniciar OrdemNegocio");
		}
	}

	public void inserir(OrdemProducaoDTO ordemProducaoDTO) throws NegocioException {

		OrdemProducao ordemProducao = this.toEntity(ordemProducaoDTO);
		String mensagemErros = ordemProducao.validar();

		if (!mensagemErros.isEmpty()) {
			throw new NegocioException(mensagemErros);
		}

		try {
			ordemProducaoDAO.beginTransaction();
			ordemProducaoDAO.incluir(ordemProducao);
			ordemProducaoDAO.commitTransaction();
		} catch (PersistenciaException ex) {
			ordemProducaoDAO.rollbackTransaction();
			throw new NegocioException("Erro ao incluir ordem de producao - " + ex.getMessage());
		}
	}

	public void alterar(OrdemProducaoDTO ordemProducaoDTO) throws NegocioException {

		OrdemProducao ordemProducao = this.toEntity(ordemProducaoDTO);
		String mensagemErros = ordemProducao.validar();
		if (!mensagemErros.isEmpty()) {
			throw new NegocioException(mensagemErros);
		}
		try {
			if (ordemProducaoDAO.buscarPorCodigo(ordemProducao.getCodigo()) == null) {
				throw new NegocioException("Nao existe essa ordem de producao");
			}
			ordemProducaoDAO.beginTransaction();
			ordemProducaoDAO.alterar(ordemProducao);
			ordemProducaoDAO.commitTransaction();
		} catch (PersistenciaException ex) {
			ordemProducaoDAO.rollbackTransaction();
			throw new NegocioException("Erro ao alterar ordem de producao - " + ex.getMessage());
		}
	}

	public void excluir(OrdemProducaoDTO ordemProducaoDTO) throws NegocioException {

		OrdemProducao ordemProducao = this.toEntity(ordemProducaoDTO);
		try {
			if (ordemProducaoDAO.buscarPorCodigo(ordemProducao.getCodigo()) == null) {
				throw new NegocioException("Nao existe essa ordem de producao");
			}
			ordemProducaoDAO.beginTransaction();
			ordemProducaoDAO.excluir(ordemProducao);
			ordemProducaoDAO.commitTransaction();
		} catch (PersistenciaException ex) {
			ordemProducaoDAO.rollbackTransaction();
			throw new NegocioException("Erro ao excluir ordem de producao - " + ex.getMessage());
		}
	}

	public void alterarItemOrdem(ItemOrdemProducaoDTO itemOrdemProducaoDTO) throws NegocioException {

		ItemOrdemProducao itemOrdemProducao = this.toItemOrdemProducaoEntity(itemOrdemProducaoDTO);
		String mensagemErros = itemOrdemProducao.validar();
		if (!mensagemErros.isEmpty()) {
			throw new NegocioException(mensagemErros);
		}
		try {
			if (itemOrdemProducaoDAO.buscarPorCodigo(itemOrdemProducao.getCodigo()) == null) {
				throw new NegocioException("Nao existe esse item de ordem de producao");
			}
			itemOrdemProducaoDAO.beginTransaction();
			itemOrdemProducaoDAO.alterar(itemOrdemProducao);
			itemOrdemProducaoDAO.commitTransaction();
		} catch (PersistenciaException ex) {
			itemOrdemProducaoDAO.rollbackTransaction();
			throw new NegocioException("Erro ao alterar item de ordem de producao - " + ex.getMessage());
		}
	}

	public void excluirItemOrdem(ItemOrdemProducaoDTO itemOrdemProducaoDTO) throws NegocioException {
		try {
			
			ItemOrdemProducao itemOrdemProducao = itemOrdemProducaoDAO.buscarPorCodigo(itemOrdemProducaoDTO.getCodigo());
			if (itemOrdemProducao == null) {
				throw new NegocioException("Nao existe esse item de ordem de producao");
			}

			String mensagemErros = itemOrdemProducao.validar();
			if (!mensagemErros.isEmpty()) {
				throw new NegocioException(mensagemErros);
			}
			
			itemOrdemProducaoDAO.beginTransaction();
			itemOrdemProducaoDAO.excluir(itemOrdemProducao);
			itemOrdemProducaoDAO.commitTransaction();
		} catch (PersistenciaException ex) {
			itemOrdemProducaoDAO.rollbackTransaction();
			throw new NegocioException("Erro ao excluir item de ordem de producao - " + ex.getMessage());
		}
	}

	public OrdemProducaoDTO pesquisaCodigo(int codigo) throws NegocioException {
		try {
			return this.toDTO(ordemProducaoDAO.buscarPorCodigo(codigo));
		} catch (PersistenciaException ex) {
			throw new NegocioException("Erro ao pesquisar ordem de producao pelo codigo - " + ex.getMessage());
		}
	}

	public ItemOrdemProducaoDTO pesquisaItemOrdemCodigo(int codigo) throws NegocioException {
		try {
			return this.toItemOrdemProducaoDTO(itemOrdemProducaoDAO.buscarPorCodigo(codigo));
		} catch (PersistenciaException ex) {
			throw new NegocioException("Erro ao pesquisar item de ordem de producao pelo codigo - " + ex.getMessage());
		}
	}

	public List<OrdemProducaoDTO> pesquisaPorDataProducao(LocalDate dataInicial, LocalDate dataFinal)
			throws NegocioException {
		try {
			return this.toDTOAll(ordemProducaoDAO.buscarPorDataProducao(dataInicial, dataFinal));
		} catch (PersistenciaException ex) {
			throw new NegocioException("Erro ao pesquisar ordem de producao por data de producao - " + ex.getMessage());
		}
	}

	public List<OrdemProducaoDTO> pesquisaPorEstado(EstadoOrdemProducaoDTO estado) throws NegocioException {
		try {
			return this.toDTOAll(ordemProducaoDAO.buscarPorEstado(estado));
		} catch (PersistenciaException ex) {
			throw new NegocioException("Erro ao pesquisar ordem de producao pelo estado - " + ex.getMessage());
		}
	}

	public List<OrdemProducaoDTO> toDTOAll(List<OrdemProducao> listaOrdemProducao) {
		List<OrdemProducaoDTO> listaDTO = new ArrayList<OrdemProducaoDTO>();

		for (OrdemProducao ordemProducao : listaOrdemProducao) {
			listaDTO.add(this.toDTO(ordemProducao));
		}
		return listaDTO;
	}

	public void processarOrdemProducao(OrdemProducaoDTO ordemProducaoDTO) throws NegocioException {

		OrdemProducao ordemProducao = this.toEntity(ordemProducaoDTO);

		if (ordemProducao.getEstado() == EstadoOrdemProducaoDTO.PROCESSADA) {
			throw new NegocioException("Ordem ja processada");
		}

		String mensagemErros = ordemProducao.validar();
		if (!mensagemErros.isEmpty()) {
			throw new NegocioException(mensagemErros);
		}

		ProdutoDTO produto;
		boolean processouItens = true;
		ordemProducaoDAO.beginTransaction();
		Iterator<ItemOrdemProducao> itensOrdem = ordemProducao.getListaItens().iterator();
		while (itensOrdem.hasNext() && processouItens) {
			ItemOrdemProducao itemOrdem = (ItemOrdemProducao) itensOrdem.next();
			produto = produtoNegocio.pesquisaCodigo(itemOrdem.getPreparoProduto().getProduto().getCodigo());
			if (produto.getEstoque() >= itemOrdem.getQuantidadePorcao()) {
				produto.setEstoque(produto.getEstoque() - itemOrdem.getQuantidadePorcao());
				produtoNegocio.alterar(produto);
			} else {
				processouItens = false;
			}
		}
		if (processouItens) {
			ordemProducaoDTO.setEstado(EstadoOrdemProducaoDTO.PROCESSADA);
			this.alterar(ordemProducaoDTO);
			ordemProducaoDAO.commitTransaction();
		} else {
			ordemProducaoDAO.rollbackTransaction();
			throw new NegocioException("Existem produtos sem estoque suficiente para o processamento");
		}
	}

	public OrdemProducaoDTO toDTO(OrdemProducao ordemProducao) {
		return this.modelMapper.map(ordemProducao, OrdemProducaoDTO.class);
	}

	public OrdemProducao toEntity(OrdemProducaoDTO ordemProducaoDTO) {
		return this.modelMapper.map(ordemProducaoDTO, OrdemProducao.class);
	}

	public ItemOrdemProducaoDTO toItemOrdemProducaoDTO(ItemOrdemProducao itemOrdemProducao) {
		return this.modelMapper.map(itemOrdemProducao, ItemOrdemProducaoDTO.class);
	}

	public ItemOrdemProducao toItemOrdemProducaoEntity(ItemOrdemProducaoDTO itemOrdemProducaoDTO) {
		return this.modelMapper.map(itemOrdemProducaoDTO, ItemOrdemProducao.class);
	}
}
