package ifmt.cba.execucao;

import ifmt.cba.dto.ColaboradorDTO;
import ifmt.cba.negocio.ColaboradorNegocio;
import ifmt.cba.negocio.NegocioException;
import ifmt.cba.persistencia.ColaboradorDAO;
import ifmt.cba.persistencia.FabricaEntityManager;
import ifmt.cba.persistencia.PersistenciaException;

public class AppInserirColaborador{
    public static void main(String[] args) {

        try {
            ColaboradorDAO colaboradorDAO = new ColaboradorDAO(
                    FabricaEntityManager.getEntityManagerProducao());
            ColaboradorNegocio colaboradorNegocio = new ColaboradorNegocio(colaboradorDAO);
    
            ColaboradorDTO colaboradorDTO = new ColaboradorDTO();
            colaboradorDTO.setNome("Colaborador 01");
            colaboradorDTO.setTelefone("65 99999-7070");
            colaboradorDTO.setRG("456789-1");
            colaboradorDTO.setCPF("234.432.567-12");

            colaboradorNegocio.inserir(colaboradorDTO);

            colaboradorDTO = new ColaboradorDTO();
            colaboradorDTO.setNome("Colaborador 02");
            colaboradorDTO.setTelefone("65 98888-7070");
            colaboradorDTO.setRG("987654-5");
            colaboradorDTO.setCPF("345.765.890-20");

            colaboradorNegocio.inserir(colaboradorDTO);


        } catch (PersistenciaException | NegocioException e) {
            e.printStackTrace();
        }
    }
}
