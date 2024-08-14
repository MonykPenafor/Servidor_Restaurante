package ifmt.cba.execucao;

import ifmt.cba.dto.EntregadorDTO;
import ifmt.cba.negocio.EntregadorNegocio;
import ifmt.cba.negocio.NegocioException;
import ifmt.cba.persistencia.EntregadorDAO;
import ifmt.cba.persistencia.FabricaEntityManager;
import ifmt.cba.persistencia.PersistenciaException;

public class AppInserirEntregador {
    public static void main(String[] args) {

        try {
            EntregadorDAO entregadorDAO = new EntregadorDAO(
                    FabricaEntityManager.getEntityManagerProducao());
            EntregadorNegocio entregadorNegocio = new EntregadorNegocio(entregadorDAO);
    
            EntregadorDTO entregadorDTO = new EntregadorDTO();
            entregadorDTO.setNome("Entregador 01");
            entregadorDTO.setTelefone("65 99999-7070");
            entregadorDTO.setRG("456789-1");
            entregadorDTO.setCPF("234.432.567-12");

            entregadorNegocio.inserir(entregadorDTO);

            entregadorDTO = new EntregadorDTO();
            entregadorDTO.setNome("Entregador 02");
            entregadorDTO.setTelefone("65 98888-7070");
            entregadorDTO.setRG("987654-5");
            entregadorDTO.setCPF("345.765.890-20");

            entregadorNegocio.inserir(entregadorDTO);


        } catch (PersistenciaException | NegocioException e) {
            e.printStackTrace();
        }
    }
}
