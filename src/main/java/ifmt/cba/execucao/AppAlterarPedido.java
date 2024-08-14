package ifmt.cba.execucao;

import java.util.List;

import ifmt.cba.dto.ClienteDTO;
import ifmt.cba.dto.ItemPedidoDTO;
import ifmt.cba.dto.PedidoDTO;
import ifmt.cba.negocio.ClienteNegocio;
import ifmt.cba.negocio.NegocioException;
import ifmt.cba.negocio.PedidoNegocio;
import ifmt.cba.persistencia.ClienteDAO;
import ifmt.cba.persistencia.FabricaEntityManager;
import ifmt.cba.persistencia.ItemPedidoDAO;
import ifmt.cba.persistencia.PedidoDAO;
import ifmt.cba.persistencia.PersistenciaException;

public class AppAlterarPedido {

    public static void main(String[] args) {
        try {
            ClienteDAO clienteDAO = new ClienteDAO(
                    FabricaEntityManager.getEntityManagerProducao());
            PedidoDAO pedidoDAO = new PedidoDAO(FabricaEntityManager.getEntityManagerProducao());
            ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO(FabricaEntityManager.getEntityManagerProducao());
            ClienteNegocio clienteNegocio = new ClienteNegocio(clienteDAO, pedidoDAO);
            PedidoNegocio pedidoNegocio = new PedidoNegocio(pedidoDAO, itemPedidoDAO, clienteDAO);

            ClienteDTO clienteDTO = clienteNegocio.pesquisaParteNome("Cliente 02").get(0);
            PedidoDTO pedidoDTO = pedidoNegocio.pesquisaPorCliente(clienteDTO).get(0);
            if (pedidoDTO != null) {

                List<ItemPedidoDTO> lista = pedidoDTO.getListaItens();
                ItemPedidoDTO itemPedidoDTO = lista.get(0);
                //pedidoNegocio.excluirItemPedido(itemPedidoDTO);
                //itemPedidoDTO = lista.get(0);
                itemPedidoDTO.setQuantidadePorcao(4);
                pedidoNegocio.alterarItemPedido(itemPedidoDTO);
            }

            
        } catch (PersistenciaException | NegocioException e) {
            e.printStackTrace();
            }
    }
}
