package ifmt.cba.negocio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import ifmt.cba.dto.MovimentoEstoqueDTO;
import ifmt.cba.dto.ProdutoDTO;
import ifmt.cba.dto.RegistroEstoqueDTO;
import ifmt.cba.entity.Produto;
import ifmt.cba.entity.RegistroEstoque;
import ifmt.cba.persistencia.PersistenciaException;
import ifmt.cba.persistencia.ProdutoDAO;
import ifmt.cba.persistencia.RegistroEstoqueDAO;

public class RegistroEstoqueNegocio {

    private ModelMapper modelMapper;
	private RegistroEstoqueDAO registroDAO;
	private ProdutoNegocio produtoNegocio;
	
	public RegistroEstoqueNegocio(RegistroEstoqueDAO registroDAO, ProdutoDAO produtoDAO) {
		this.registroDAO = registroDAO;
		this.produtoNegocio = new ProdutoNegocio(produtoDAO);
		this.modelMapper = new ModelMapper();
	}

	public void inserir(RegistroEstoqueDTO registroEstoqueDTO) throws NegocioException {

		RegistroEstoque registroEstoque = this.toEntity(registroEstoqueDTO);
		String mensagemErros = registroEstoque.validar();

		if (!mensagemErros.isEmpty()) {
			throw new NegocioException(mensagemErros);
		}

		try {
			registroDAO.beginTransaction();
			ProdutoDTO produtoDTO = produtoNegocio.pesquisaCodigo(registroEstoque.getProduto().getCodigo());
			if(registroEstoque.getMovimento() == MovimentoEstoqueDTO.COMPRA){
				produtoDTO.setEstoque(produtoDTO.getEstoque() + registroEstoque.getQuantidade());
			}else{
				produtoDTO.setEstoque(produtoDTO.getEstoque() - registroEstoque.getQuantidade());
			}
			produtoNegocio.alterar(produtoDTO);
			registroDAO.incluir(registroEstoque);
			registroDAO.commitTransaction();
		} catch (PersistenciaException ex) {
			registroDAO.rollbackTransaction();
			throw new NegocioException("Erro ao incluir registro de estoque - " + ex.getMessage());
		}
	}

	public void excluir(RegistroEstoqueDTO registroEstoqueDTO) throws NegocioException {

		RegistroEstoque registroEstoque = this.toEntity(registroEstoqueDTO);
		try {
			if (registroDAO.buscarPorCodigo(registroEstoque.getCodigo()) == null) {
				throw new NegocioException("Nao existe esse registro de estoque");
			}
			registroDAO.beginTransaction();
			ProdutoDTO produtoDTO = produtoNegocio.pesquisaCodigo(registroEstoque.getProduto().getCodigo());
			if(registroEstoque.getMovimento() == MovimentoEstoqueDTO.COMPRA){
				produtoDTO.setEstoque(produtoDTO.getEstoque() - registroEstoque.getQuantidade());
			}else{
				produtoDTO.setEstoque(produtoDTO.getEstoque() + registroEstoque.getQuantidade());
			}
			produtoNegocio.alterar(produtoDTO);
			registroDAO.excluir(registroEstoque);
			registroDAO.commitTransaction();
		} catch (PersistenciaException ex) {
			registroDAO.rollbackTransaction();
			throw new NegocioException("Erro ao excluir registro de estoque - " + ex.getMessage());
		}
	}

	public RegistroEstoqueDTO pesquisaCodigo(int codigo) throws NegocioException {
		try {
			RegistroEstoque registroEstoque = registroDAO.buscarPorCodigo(codigo);
			if (registroEstoque != null) {
				return this.toDTO(registroEstoque);
			} else {
				return null;
			}
		} catch (PersistenciaException ex) {
			throw new NegocioException("Erro ao pesquisar registro de estoque pelo codigo - " + ex.getMessage());
		}
	}

    public List<RegistroEstoqueDTO> buscarPorMovimento(MovimentoEstoqueDTO movimento) throws NegocioException {
        try {
			return this.toDTOAll(registroDAO.buscarPorMovimento(movimento));
		} catch (PersistenciaException ex) {
			throw new NegocioException("Erro ao pesquisar registro de estoque por tipo de movimento - " + ex.getMessage());
		}
    }

	public List<RegistroEstoqueDTO> buscarPorMovimentoEData(MovimentoEstoqueDTO movimento, LocalDate data) throws NegocioException {
		try {
			return this.toDTOAll(registroDAO.buscarPorMovimentoEData(movimento, data));
		} catch (PersistenciaException ex) {
			throw new NegocioException("Erro ao pesquisar registro de estoque por tipo de movimento e data - " + ex.getMessage());
		}
	}

	public List<RegistroEstoqueDTO> buscarPorProduto(Produto produto) throws NegocioException {
		try {
			return this.toDTOAll(registroDAO.buscarPorProduto(produto));
		} catch (PersistenciaException ex) {
			throw new NegocioException("Erro ao pesquisar registro de estoque por produto - " + ex.getMessage());
		}
	}

	public List<RegistroEstoqueDTO> toDTOAll(List<RegistroEstoque> listaRegistro) {
		List<RegistroEstoqueDTO> listaDTO = new ArrayList<RegistroEstoqueDTO>();

		for (RegistroEstoque registroEstoque : listaRegistro) {
			listaDTO.add(this.toDTO(registroEstoque));
		}
		return listaDTO;
	}

	public RegistroEstoqueDTO toDTO(RegistroEstoque registroEstoque) {
		return this.modelMapper.map(registroEstoque, RegistroEstoqueDTO.class);
	}

	public RegistroEstoque toEntity(RegistroEstoqueDTO registroEstoqueDTO) {
		return this.modelMapper.map(registroEstoqueDTO, RegistroEstoque.class);
	}
}
