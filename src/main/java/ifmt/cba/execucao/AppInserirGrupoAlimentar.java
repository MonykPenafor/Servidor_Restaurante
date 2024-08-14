package ifmt.cba.execucao;

import ifmt.cba.dto.GrupoAlimentarDTO;
import ifmt.cba.negocio.GrupoAlimentarNegocio;
import ifmt.cba.negocio.NegocioException;
import ifmt.cba.persistencia.FabricaEntityManager;
import ifmt.cba.persistencia.GrupoAlimentarDAO;
import ifmt.cba.persistencia.PersistenciaException;
import ifmt.cba.persistencia.ProdutoDAO;

public class AppInserirGrupoAlimentar {
    public static void main(String[] args) {

        try {
            GrupoAlimentarDAO grupoAlimentarDAO = new GrupoAlimentarDAO(
                    FabricaEntityManager.getEntityManagerProducao());
            ProdutoDAO produtoDAO = new ProdutoDAO(FabricaEntityManager.getEntityManagerProducao());
            GrupoAlimentarNegocio grupoAlimentarNegocio = new GrupoAlimentarNegocio(grupoAlimentarDAO, produtoDAO);
    
            GrupoAlimentarDTO grupoDTO = new GrupoAlimentarDTO();
            grupoDTO.setNome("Carboidrato");
            grupoAlimentarNegocio.inserir(grupoDTO);

            grupoDTO = new GrupoAlimentarDTO();
            grupoDTO.setNome("Legumes");
            grupoAlimentarNegocio.inserir(grupoDTO);

            grupoDTO = new GrupoAlimentarDTO();
            grupoDTO.setNome("Proteinas");
            grupoAlimentarNegocio.inserir(grupoDTO);
            
            grupoDTO = new GrupoAlimentarDTO();
            grupoDTO.setNome("Verduras");
            grupoAlimentarNegocio.inserir(grupoDTO);

        } catch (PersistenciaException | NegocioException e) {
            e.printStackTrace();
        }
    }
}
