package ifmt.cba.execucao;

import ifmt.cba.dto.PreparoProdutoDTO;
import ifmt.cba.dto.ProdutoDTO;
import ifmt.cba.dto.TipoPreparoDTO;
import ifmt.cba.negocio.NegocioException;
import ifmt.cba.negocio.PreparoProdutoNegocio;
import ifmt.cba.negocio.ProdutoNegocio;
import ifmt.cba.negocio.TipoPreparoNegocio;
import ifmt.cba.persistencia.FabricaEntityManager;
import ifmt.cba.persistencia.PersistenciaException;
import ifmt.cba.persistencia.PreparoProdutoDAO;
import ifmt.cba.persistencia.ProdutoDAO;
import ifmt.cba.persistencia.TipoPreparoDAO;

public class AppInserirPreparoProduto {
        public static void main(String[] args) {

        try {
            PreparoProdutoDAO preparoProdutoDAO = new PreparoProdutoDAO(
                    FabricaEntityManager.getEntityManagerProducao());
            ProdutoDAO produtoDAO = new ProdutoDAO(FabricaEntityManager.getEntityManagerProducao());
            TipoPreparoDAO tipoPreparoDAO = new TipoPreparoDAO(FabricaEntityManager.getEntityManagerProducao());
            
            TipoPreparoNegocio tipoPreparoNegogio = new TipoPreparoNegocio(tipoPreparoDAO);
            PreparoProdutoNegocio preparoProdutoNegocio = new PreparoProdutoNegocio(preparoProdutoDAO);
            ProdutoNegocio produtoNegocio = new ProdutoNegocio(produtoDAO);

            ProdutoDTO produtoDTO = produtoNegocio.pesquisaParteNome("Arroz Branco").get(0);
            TipoPreparoDTO tipoPreparoDTO = tipoPreparoNegogio.pesquisaParteDescricao("Cozimento em agua").get(0);
            PreparoProdutoDTO preparoProdutoDTO = new PreparoProdutoDTO();
            preparoProdutoDTO.setNome("Arroz Cozido");
            preparoProdutoDTO.setProduto(produtoDTO);
            preparoProdutoDTO.setTipoPreparo(tipoPreparoDTO);
            preparoProdutoDTO.setTempoPreparo(25);
            preparoProdutoDTO.setValorPreparo(0.5f);
            preparoProdutoNegocio.inserir(preparoProdutoDTO);

            produtoDTO = produtoNegocio.pesquisaParteNome("Costela suina").get(0);
            tipoPreparoDTO = tipoPreparoNegogio.pesquisaParteDescricao("Assado no forno").get(0);
            preparoProdutoDTO = new PreparoProdutoDTO();
            preparoProdutoDTO.setNome("Costela suina no forno");
            preparoProdutoDTO.setProduto(produtoDTO);
            preparoProdutoDTO.setTipoPreparo(tipoPreparoDTO);
            preparoProdutoDTO.setTempoPreparo(60);
            preparoProdutoDTO.setValorPreparo(4.0f);
            preparoProdutoNegocio.inserir(preparoProdutoDTO);

            produtoDTO = produtoNegocio.pesquisaParteNome("Alcatra bovina").get(0);
            tipoPreparoDTO = tipoPreparoNegogio.pesquisaParteDescricao("Grelhado").get(0);
            preparoProdutoDTO = new PreparoProdutoDTO();
            preparoProdutoDTO.setNome("Alcatra bovina grelhada");
            preparoProdutoDTO.setProduto(produtoDTO);
            preparoProdutoDTO.setTipoPreparo(tipoPreparoDTO);
            preparoProdutoDTO.setTempoPreparo(20);
            preparoProdutoDTO.setValorPreparo(3.0f);
            preparoProdutoNegocio.inserir(preparoProdutoDTO);

        } catch (PersistenciaException | NegocioException e) {
            e.printStackTrace();
        }
    }
}
