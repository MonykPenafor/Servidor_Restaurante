package ifmt.cba.execucao;

import ifmt.cba.dto.BairroDTO;
import ifmt.cba.negocio.BairroNegocio;
import ifmt.cba.negocio.NegocioException;
import ifmt.cba.persistencia.BairroDAO;
import ifmt.cba.persistencia.FabricaEntityManager;
import ifmt.cba.persistencia.PersistenciaException;

public class AppInserirBairro {
public static void main(String[] args) {

        try {
            BairroDAO bairroDAO = new BairroDAO(
                    FabricaEntityManager.getEntityManagerProducao());
            BairroNegocio bairroNegocio = new BairroNegocio(bairroDAO);
    
            BairroDTO bairroDTO = new BairroDTO();
            bairroDTO.setNome("Centro");
            bairroDTO.setCustoEntrega(7.00f);
            bairroNegocio.inserir(bairroDTO);

            bairroDTO = new BairroDTO();
            bairroDTO.setNome("Coxipo");
            bairroDTO.setCustoEntrega(8.00f);
            bairroNegocio.inserir(bairroDTO);

        } catch (PersistenciaException | NegocioException e) {
            e.printStackTrace();
        }
    }
}
