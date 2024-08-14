package ifmt.cba.execucao;

import ifmt.cba.dto.BairroDTO;
import ifmt.cba.dto.ClienteDTO;
import ifmt.cba.negocio.BairroNegocio;
import ifmt.cba.negocio.ClienteNegocio;
import ifmt.cba.negocio.NegocioException;
import ifmt.cba.persistencia.BairroDAO;
import ifmt.cba.persistencia.ClienteDAO;
import ifmt.cba.persistencia.FabricaEntityManager;
import ifmt.cba.persistencia.PedidoDAO;
import ifmt.cba.persistencia.PersistenciaException;

public class AppInserirCliente {
    public static void main(String[] args) {

        try {
            ClienteDAO clienteDAO = new ClienteDAO(
                    FabricaEntityManager.getEntityManagerProducao());
            PedidoDAO pedidoDAO = new PedidoDAO(FabricaEntityManager.getEntityManagerProducao());
            ClienteNegocio clienteNegocio = new ClienteNegocio(clienteDAO, pedidoDAO);
            BairroDAO bairroDAO = new BairroDAO(FabricaEntityManager.getEntityManagerProducao());
            BairroNegocio bairroNegocio = new BairroNegocio(bairroDAO);

    
            BairroDTO bairroDTO = bairroNegocio.pesquisaParteNome("Centro").get(0);
            ClienteDTO clienteDTO = new ClienteDTO();
            clienteDTO.setNome("Cliente 01");
            clienteDTO.setCPF("234.345.656-55");
            clienteDTO.setRG("234567-9");
            clienteDTO.setTelefone("65 99999-7070");
            clienteDTO.setLogradouro("Rua das flores");
            clienteDTO.setNumero("123");
            clienteDTO.setPontoReferencia("Proximo a nada");
            clienteDTO.setBairro(bairroDTO);
            clienteNegocio.inserir(clienteDTO);

            bairroDTO = bairroNegocio.pesquisaParteNome("Coxipo").get(0);
            clienteDTO = new ClienteDTO();
            clienteDTO.setNome("Cliente 02");
            clienteDTO.setCPF("123.432.678-99");
            clienteDTO.setRG("123456-8");
            clienteDTO.setTelefone("65 98888-3030");
            clienteDTO.setLogradouro("Rua das pedras");
            clienteDTO.setNumero("456");
            clienteDTO.setPontoReferencia("Final da rua");
            clienteDTO.setBairro(bairroDTO);
            clienteNegocio.inserir(clienteDTO);

        } catch (PersistenciaException | NegocioException e) {
            e.printStackTrace();
        }
    }
}
