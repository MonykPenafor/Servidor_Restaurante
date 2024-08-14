package ifmt.cba.execucao;

import java.time.LocalDate;

import ifmt.cba.dto.MovimentoEstoqueDTO;
import ifmt.cba.dto.ProdutoDTO;
import ifmt.cba.dto.RegistroEstoqueDTO;
import ifmt.cba.negocio.NegocioException;
import ifmt.cba.negocio.ProdutoNegocio;
import ifmt.cba.negocio.RegistroEstoqueNegocio;
import ifmt.cba.persistencia.FabricaEntityManager;
import ifmt.cba.persistencia.PersistenciaException;
import ifmt.cba.persistencia.ProdutoDAO;
import ifmt.cba.persistencia.RegistroEstoqueDAO;

public class AppInserirRegistroEstoque {

    public static void main(String[] args) {

        try {
            RegistroEstoqueDAO registroEstoqueDAO = new RegistroEstoqueDAO(FabricaEntityManager.getEntityManagerProducao());
            ProdutoDAO produtoDAO = new ProdutoDAO(FabricaEntityManager.getEntityManagerProducao());

            RegistroEstoqueNegocio registroEstoqueNegocio = new RegistroEstoqueNegocio(registroEstoqueDAO, produtoDAO);
            ProdutoNegocio produtoNegocio = new ProdutoNegocio(produtoDAO);

            ProdutoDTO produtoDTO = produtoNegocio.pesquisaParteNome("Arroz Branco").get(0);

            RegistroEstoqueDTO registroEstoqueDTO = new RegistroEstoqueDTO();
            registroEstoqueDTO.setData(LocalDate.now());
            registroEstoqueDTO.setMovimento(MovimentoEstoqueDTO.COMPRA);
            registroEstoqueDTO.setProduto(produtoDTO);
            registroEstoqueDTO.setQuantidade(100);
            registroEstoqueNegocio.inserir(registroEstoqueDTO);

            produtoDTO = produtoNegocio.pesquisaParteNome("Alcatra bovina").get(0);
            registroEstoqueDTO = new RegistroEstoqueDTO();
            registroEstoqueDTO.setData(LocalDate.now());
            registroEstoqueDTO.setMovimento(MovimentoEstoqueDTO.VENCIMENTO);
            registroEstoqueDTO.setProduto(produtoDTO);
            registroEstoqueDTO.setQuantidade(20);
            registroEstoqueNegocio.inserir(registroEstoqueDTO);

            produtoDTO = produtoNegocio.pesquisaParteNome("Batata Doce").get(0);
            registroEstoqueDTO = new RegistroEstoqueDTO();
            registroEstoqueDTO.setData(LocalDate.now());
            registroEstoqueDTO.setMovimento(MovimentoEstoqueDTO.PRODUCAO);
            registroEstoqueDTO.setProduto(produtoDTO);
            registroEstoqueDTO.setQuantidade(10);
            registroEstoqueNegocio.inserir(registroEstoqueDTO);

        } catch (PersistenciaException | NegocioException e) {
            e.printStackTrace();
        }
    }
}
