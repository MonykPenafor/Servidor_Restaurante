package ifmt.cba.execucao;

import java.util.ArrayList;
import java.util.List;

import ifmt.cba.dto.CardapioDTO;
import ifmt.cba.dto.PreparoProdutoDTO;
import ifmt.cba.negocio.CardapioNegocio;
import ifmt.cba.negocio.NegocioException;
import ifmt.cba.negocio.PreparoProdutoNegocio;
import ifmt.cba.persistencia.CardapioDAO;
import ifmt.cba.persistencia.FabricaEntityManager;
import ifmt.cba.persistencia.PersistenciaException;
import ifmt.cba.persistencia.PreparoProdutoDAO;

public class AppInserirCardapio {

    
    public static void main(String[] args) {

        try {
            CardapioDAO cardapioDAO = new CardapioDAO(FabricaEntityManager.getEntityManagerProducao());
            PreparoProdutoDAO preparoProdutoDAO = new PreparoProdutoDAO(FabricaEntityManager.getEntityManagerProducao());

            CardapioNegocio cardapioNegocio = new CardapioNegocio(cardapioDAO);
            PreparoProdutoNegocio preparoProdutoNegocio = new PreparoProdutoNegocio(preparoProdutoDAO);

            List<PreparoProdutoDTO> listaItens = new ArrayList<PreparoProdutoDTO>();
            PreparoProdutoDTO preparoProdutoDTO = preparoProdutoNegocio.pesquisaPorCodigo(1); //arroz cozido
            listaItens.add(preparoProdutoDTO);

            preparoProdutoDTO = preparoProdutoNegocio.pesquisaPorCodigo(2); //costela suina
            listaItens.add(preparoProdutoDTO);

            preparoProdutoDTO = preparoProdutoNegocio.pesquisaPorCodigo(3); //alcatra bovina grelhada
            listaItens.add(preparoProdutoDTO);

            CardapioDTO cardapioDTO = new CardapioDTO();
            cardapioDTO.setNome("Carnes vermelhas com arroz cozido");
            cardapioDTO.setDescricao("O cardapio oferece duas opcoes de carnes vermelhas acompanhado com arroz cozido");
            cardapioDTO.setListaPreparoProduto(listaItens);

            cardapioNegocio.inserir(cardapioDTO);

         } catch (PersistenciaException | NegocioException e) {
            e.printStackTrace();
        }
    }

}
