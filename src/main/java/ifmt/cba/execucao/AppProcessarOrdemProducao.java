package ifmt.cba.execucao;

import ifmt.cba.dto.OrdemProducaoDTO;
import ifmt.cba.negocio.NegocioException;
import ifmt.cba.negocio.OrdemProducaoNegocio;
import ifmt.cba.persistencia.FabricaEntityManager;
import ifmt.cba.persistencia.ItemOrdemProducaoDAO;
import ifmt.cba.persistencia.OrdemProducaoDAO;
import ifmt.cba.persistencia.PersistenciaException;

public class AppProcessarOrdemProducao {

    public static void main(String[] args) {

        try {
            OrdemProducaoDAO ordemProducaoDAO = new OrdemProducaoDAO(FabricaEntityManager.getEntityManagerProducao());
            ItemOrdemProducaoDAO itemOrdemProducaoDAO = new ItemOrdemProducaoDAO(
                    FabricaEntityManager.getEntityManagerProducao());
            OrdemProducaoNegocio ordemProducaoNegocio = new OrdemProducaoNegocio(ordemProducaoDAO,
                    itemOrdemProducaoDAO);

            OrdemProducaoDTO ordemProducaoDTO = ordemProducaoNegocio.pesquisaCodigo(1);

            ordemProducaoNegocio.processarOrdemProducao(ordemProducaoDTO);
        } catch (PersistenciaException | NegocioException e) {
            e.printStackTrace();
        }
    }
}
