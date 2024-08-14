package ifmt.cba.execucao;

import ifmt.cba.dto.ProdutoDTO;
import ifmt.cba.negocio.NegocioException;
import ifmt.cba.negocio.ProdutoNegocio;
import ifmt.cba.persistencia.FabricaEntityManager;
import ifmt.cba.persistencia.PersistenciaException;
import ifmt.cba.persistencia.ProdutoDAO;

public class AppAlterarProduto {

    public static void main(String[] args) {

        try {
            
            ProdutoDAO produtoDAO = new ProdutoDAO(FabricaEntityManager.getEntityManagerProducao());

            ProdutoNegocio produtoNegocio = new ProdutoNegocio(produtoDAO);

            ProdutoDTO produtoDTO = produtoNegocio.pesquisaParteNome("Alcatra bovina").get(0);
            produtoDTO.setNome("Alcatra bovina");
            produtoDTO.setEstoque(produtoDTO.getEstoque() - 100);
            produtoNegocio.alterar(produtoDTO);

           
        } catch (PersistenciaException | NegocioException e) {
            e.printStackTrace();
        }
    }
}
