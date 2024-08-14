package ifmt.cba.negocio;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import ifmt.cba.dto.BairroDTO;
import ifmt.cba.entity.Bairro;
import ifmt.cba.persistencia.BairroDAO;
import ifmt.cba.persistencia.PersistenciaException;

public class BairroNegocio {

    private ModelMapper modelMapper;
    private BairroDAO bairroDAO;

    public BairroNegocio(BairroDAO bairroDAO) {
        this.bairroDAO = bairroDAO;
        this.modelMapper = new ModelMapper();
    }

    public void inserir(BairroDTO bairroDTO) throws NegocioException {

        Bairro bairro = this.toEntity(bairroDTO);
        String mensagemErros = bairro.validar();

        if (!mensagemErros.isEmpty()) {
            throw new NegocioException(mensagemErros);
        }

        try {
            if (!bairroDAO.buscarPorParteNome(bairro.getNome()).isEmpty()) {
                throw new NegocioException("Ja existe esse bairro");
            }
            bairroDAO.beginTransaction();
            bairroDAO.incluir(bairro);
            bairroDAO.commitTransaction();
        } catch (PersistenciaException ex) {
            bairroDAO.rollbackTransaction();
            throw new NegocioException("Erro ao incluir o bairro - " + ex.getMessage());
        }
    }

    public void alterar(BairroDTO bairroDTO) throws NegocioException {

        Bairro bairro = this.toEntity(bairroDTO);
        String mensagemErros = bairro.validar();
        if (!mensagemErros.isEmpty()) {
            throw new NegocioException(mensagemErros);
        }
        try {
            if (bairroDAO.buscarPorCodigo(bairro.getCodigo()) == null) {
                throw new NegocioException("Nao existe esse bairro");
            }
            bairroDAO.beginTransaction();
            bairroDAO.alterar(bairro);
            bairroDAO.commitTransaction();
        } catch (PersistenciaException ex) {
            bairroDAO.rollbackTransaction();
            throw new NegocioException("Erro ao alterar o bairro - " + ex.getMessage());
        }
    }

    public void excluir(int codigo) throws NegocioException {

        try {
            Bairro bairro = bairroDAO.buscarPorCodigo(codigo);
            if (bairro == null) {
                throw new NegocioException("Nao existe esse bairro");
            }

            bairroDAO.beginTransaction();
            bairroDAO.excluir(bairro);
            bairroDAO.commitTransaction();
        } catch (PersistenciaException ex) {
            bairroDAO.rollbackTransaction();
            throw new NegocioException("Erro ao excluir o bairro - " + ex.getMessage());
        }
    }

    public List<BairroDTO> pesquisaParteNome(String parteNome) throws NegocioException {
        try {
            return this.toDTOAll(bairroDAO.buscarPorParteNome(parteNome));
        } catch (PersistenciaException ex) {
            throw new NegocioException("Erro ao pesquisar bairro pelo nome - " + ex.getMessage());
        }
    }

    public BairroDTO pesquisaCodigo(int codigo) throws NegocioException {
        try {
            return this.toDTO(bairroDAO.buscarPorCodigo(codigo));
        } catch (PersistenciaException ex) {
            throw new NegocioException("Erro ao pesquisar bairro pelo codigo - " + ex.getMessage());
        }
    }

    public List<BairroDTO> toDTOAll(List<Bairro> listaBarro) {
        List<BairroDTO> listaDTO = new ArrayList<BairroDTO>();

        for (Bairro bairro : listaBarro) {
            listaDTO.add(this.toDTO(bairro));
        }
        return listaDTO;
    }

    public BairroDTO toDTO(Bairro bairro) {
        return this.modelMapper.map(bairro, BairroDTO.class);
    }

    public Bairro toEntity(BairroDTO bairroDTO) {
        return this.modelMapper.map(bairroDTO, Bairro.class);
    }
}
