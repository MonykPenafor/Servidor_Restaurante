package ifmt.cba.execucao;

import ifmt.cba.dto.GrupoAlimentarDTO;
import ifmt.cba.dto.ProdutoDTO;
import ifmt.cba.negocio.GrupoAlimentarNegocio;
import ifmt.cba.negocio.NegocioException;
import ifmt.cba.negocio.ProdutoNegocio;
import ifmt.cba.persistencia.FabricaEntityManager;
import ifmt.cba.persistencia.GrupoAlimentarDAO;
import ifmt.cba.persistencia.PersistenciaException;
import ifmt.cba.persistencia.ProdutoDAO;

public class AppInserirProduto {

    public static void main(String[] args) {

        try {
            GrupoAlimentarDAO grupoAlimentarDAO = new GrupoAlimentarDAO(
                    FabricaEntityManager.getEntityManagerProducao());
            ProdutoDAO produtoDAO = new ProdutoDAO(FabricaEntityManager.getEntityManagerProducao());
            GrupoAlimentarNegocio grupoAlimentarNegocio = new GrupoAlimentarNegocio(grupoAlimentarDAO, produtoDAO);
            ProdutoNegocio produtoNegocio = new ProdutoNegocio(produtoDAO);

            GrupoAlimentarDTO grupoDTO = grupoAlimentarNegocio.pesquisaCodigo(3); // proteina

            ProdutoDTO produtoDTO = new ProdutoDTO();
            produtoDTO.setNome("Alcatra bovina");
            produtoDTO.setEstoque(1000);
            produtoDTO.setEstoqueMinimo(100);
            produtoDTO.setCustoUnidade(2.0f);
            produtoDTO.setValorEnergetico(50);
            produtoDTO.setGrupoAlimentar(grupoDTO);
            produtoNegocio.inserir(produtoDTO);

            produtoDTO = new ProdutoDTO();
            produtoDTO.setNome("Costela suina");
            produtoDTO.setEstoque(30);
            produtoDTO.setEstoqueMinimo(50);
            produtoDTO.setCustoUnidade(1.5f);
            produtoDTO.setValorEnergetico(60);
            produtoDTO.setGrupoAlimentar(grupoDTO);
            produtoNegocio.inserir(produtoDTO);

            grupoDTO = grupoAlimentarNegocio.pesquisaCodigo(2); // Legumes

            produtoDTO = new ProdutoDTO();
            produtoDTO.setNome("Batata Inglesa");
            produtoDTO.setEstoque(2000);
            produtoDTO.setEstoqueMinimo(300);
            produtoDTO.setCustoUnidade(1.0f);
            produtoDTO.setValorEnergetico(80);
            produtoDTO.setGrupoAlimentar(grupoDTO);
            produtoNegocio.inserir(produtoDTO);

            produtoDTO = new ProdutoDTO();
            produtoDTO.setNome("Batata Doce");
            produtoDTO.setEstoque(100);
            produtoDTO.setEstoqueMinimo(200);
            produtoDTO.setCustoUnidade(1.3f);
            produtoDTO.setValorEnergetico(70);
            produtoDTO.setGrupoAlimentar(grupoDTO);
            produtoNegocio.inserir(produtoDTO);


            grupoDTO = grupoAlimentarNegocio.pesquisaCodigo(1); // Carboidrato

            produtoDTO = new ProdutoDTO();
            produtoDTO.setNome("Arroz Branco");
            produtoDTO.setEstoque(1000);
            produtoDTO.setEstoqueMinimo(500);
            produtoDTO.setCustoUnidade(1.7f);
            produtoDTO.setValorEnergetico(100);
            produtoDTO.setGrupoAlimentar(grupoDTO);
            produtoNegocio.inserir(produtoDTO);

            produtoDTO = new ProdutoDTO();
            produtoDTO.setNome("Arroz Integral");
            produtoDTO.setEstoque(1000);
            produtoDTO.setEstoqueMinimo(500);
            produtoDTO.setCustoUnidade(1.9f);
            produtoDTO.setValorEnergetico(90);
            produtoDTO.setGrupoAlimentar(grupoDTO);
            produtoNegocio.inserir(produtoDTO);
            
            produtoDTO = new ProdutoDTO();
            produtoDTO.setNome("Fubá de Milho");
            produtoDTO.setEstoque(500);
            produtoDTO.setEstoqueMinimo(200);
            produtoDTO.setCustoUnidade(1.4f);
            produtoDTO.setValorEnergetico(75);
            produtoDTO.setGrupoAlimentar(grupoDTO);
            produtoNegocio.inserir(produtoDTO);

        } catch (PersistenciaException | NegocioException e) {
            e.printStackTrace();
        }
    }
}
