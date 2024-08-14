package ifmt.cba.execucao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ifmt.cba.dto.CardapioDTO;
import ifmt.cba.dto.EstadoOrdemProducaoDTO;
import ifmt.cba.dto.ItemOrdemProducaoDTO;
import ifmt.cba.dto.OrdemProducaoDTO;
import ifmt.cba.dto.PreparoProdutoDTO;
import ifmt.cba.negocio.CardapioNegocio;
import ifmt.cba.negocio.NegocioException;
import ifmt.cba.negocio.OrdemProducaoNegocio;
import ifmt.cba.negocio.PreparoProdutoNegocio;
import ifmt.cba.persistencia.CardapioDAO;
import ifmt.cba.persistencia.FabricaEntityManager;
import ifmt.cba.persistencia.ItemOrdemProducaoDAO;
import ifmt.cba.persistencia.OrdemProducaoDAO;
import ifmt.cba.persistencia.PersistenciaException;
import ifmt.cba.persistencia.PreparoProdutoDAO;

public class AppInserirOrdemProducao {

    public static void main(String[] args) {

        try {
            OrdemProducaoDAO ordemProducaoDAO = new OrdemProducaoDAO(FabricaEntityManager.getEntityManagerProducao());
            ItemOrdemProducaoDAO itemOrdemProducaoDAO = new ItemOrdemProducaoDAO(FabricaEntityManager.getEntityManagerProducao());
            PreparoProdutoDAO preparoProdutoDAO = new PreparoProdutoDAO(FabricaEntityManager.getEntityManagerProducao());
            CardapioDAO cardapioDAO = new CardapioDAO(FabricaEntityManager.getEntityManagerProducao());

            OrdemProducaoNegocio ordemProducaoNegocio = new OrdemProducaoNegocio(ordemProducaoDAO, itemOrdemProducaoDAO);
            PreparoProdutoNegocio preparoProdutoNegocio = new PreparoProdutoNegocio(preparoProdutoDAO);
            CardapioNegocio cardapioNegocio = new CardapioNegocio(cardapioDAO);

            CardapioDTO cardapioDTO = cardapioNegocio.pesquisaPorNome("Carnes vermelhas").get(0);
            OrdemProducaoDTO ordemProducaoDTO = new OrdemProducaoDTO();
            List<ItemOrdemProducaoDTO> listaItens = new ArrayList<ItemOrdemProducaoDTO>();
            ItemOrdemProducaoDTO itemOrdemProducaoDTO = new ItemOrdemProducaoDTO();
            PreparoProdutoDTO preparoProdutoDTO = preparoProdutoNegocio.pesquisaPorCodigo(1); //arroz cozido
            itemOrdemProducaoDTO.setPreparoProduto(preparoProdutoDTO);
            itemOrdemProducaoDTO.setQuantidadePorcao(50);
            listaItens.add(itemOrdemProducaoDTO);

            itemOrdemProducaoDTO = new ItemOrdemProducaoDTO();
            preparoProdutoDTO = preparoProdutoNegocio.pesquisaPorCodigo(2); //costela suina
            itemOrdemProducaoDTO.setPreparoProduto(preparoProdutoDTO);
            itemOrdemProducaoDTO.setQuantidadePorcao(30);
            listaItens.add(itemOrdemProducaoDTO);

            itemOrdemProducaoDTO = new ItemOrdemProducaoDTO();
            preparoProdutoDTO = preparoProdutoNegocio.pesquisaPorCodigo(3); //alcatra bovina grelhada
            itemOrdemProducaoDTO.setPreparoProduto(preparoProdutoDTO);
            itemOrdemProducaoDTO.setQuantidadePorcao(40);
            listaItens.add(itemOrdemProducaoDTO);

            ordemProducaoDTO.setCardapio(cardapioDTO);
            ordemProducaoDTO.setDataProducao(LocalDate.now());
            ordemProducaoDTO.setEstado(EstadoOrdemProducaoDTO.REGISTRADA);
            ordemProducaoDTO.setListaItens(listaItens);

            ordemProducaoNegocio.inserir(ordemProducaoDTO);


         } catch (PersistenciaException | NegocioException e) {
            e.printStackTrace();
        }
    }
}
