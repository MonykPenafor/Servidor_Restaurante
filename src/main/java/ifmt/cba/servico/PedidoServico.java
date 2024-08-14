package ifmt.cba.servico;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import ifmt.cba.dto.ClienteDTO;
import ifmt.cba.dto.EstadoPedidoDTO;
import ifmt.cba.dto.PedidoDTO;
import ifmt.cba.negocio.PedidoNegocio;
import ifmt.cba.persistencia.ClienteDAO;
import ifmt.cba.persistencia.FabricaEntityManager;
import ifmt.cba.persistencia.ItemPedidoDAO;
import ifmt.cba.persistencia.PedidoDAO;
import ifmt.cba.persistencia.PersistenciaException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

@Path("/pedido")
public class PedidoServico {

    private static PedidoNegocio pedidoNegocio;
    private static PedidoDAO pedidoDAO;
    private static ClienteDAO clienteDAO;

    static {
        try {
            pedidoDAO = new PedidoDAO(FabricaEntityManager.getEntityManagerProducao());
            ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO(FabricaEntityManager.getEntityManagerProducao());
            clienteDAO = new ClienteDAO(FabricaEntityManager.getEntityManagerProducao());
            pedidoNegocio = new PedidoNegocio(pedidoDAO, itemPedidoDAO, clienteDAO);
        } catch (PersistenciaException e) {
            e.printStackTrace();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response adicionar(PedidoDTO pedidoDTO) {
        ResponseBuilder resposta;
        try {
            pedidoNegocio.inserir(pedidoDTO);
            resposta = Response.ok();
            resposta.entity(pedidoNegocio.pesquisaPorCliente(pedidoDTO.getCliente()).get(0));
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity("{\"erro\": \"" + ex.getMessage() + "\"}");
        }
        return resposta.build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response alterar(PedidoDTO pedidoDTO) {
        ResponseBuilder resposta;
        try {
            pedidoNegocio.alterar(pedidoDTO);
            resposta = Response.ok();
            resposta.entity(pedidoNegocio.pesquisaCodigo(pedidoDTO.getCodigo()));
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity("{\"erro\": \"" + ex.getMessage() + "\"}");
        }
        return resposta.build();
    }

    @DELETE
    @Path("/{codigo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response excluir(@PathParam("codigo") int codigo) {
        ResponseBuilder resposta;
        try {
            pedidoNegocio.excluir(pedidoNegocio.pesquisaCodigo(codigo));
            resposta = Response.ok();
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity("{\"erro\": \"" + ex.getMessage() + "\"}");
        }
        return resposta.build();
    }

    @PUT
    @Path("/producao")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response mudarPedidoParaProducao(PedidoDTO pedidoDTO) {
        ResponseBuilder resposta;
        try {
            pedidoNegocio.mudarPedidoParaProducao(pedidoDTO);
            resposta = Response.ok();
            resposta.entity(pedidoNegocio.pesquisaCodigo(pedidoDTO.getCodigo()));
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity("{\"erro\": \"" + ex.getMessage() + "\"}");
        }
        return resposta.build();
    }

    @PUT
    @Path("/pronto")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response mudarPedidoParaPronto(PedidoDTO pedidoDTO) {
        ResponseBuilder resposta;
        try {
            pedidoNegocio.mudarPedidoParaPronto(pedidoDTO);
            resposta = Response.ok();
            resposta.entity(pedidoNegocio.pesquisaCodigo(pedidoDTO.getCodigo()));
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity("{\"erro\": \"" + ex.getMessage() + "\"}");
        }
        return resposta.build();
    }

    @PUT
    @Path("/entrega")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response mudarPedidoParaEntrega(PedidoDTO pedidoDTO) {
        ResponseBuilder resposta;
        try {
            pedidoNegocio.mudarPedidoParaEntrega(pedidoDTO);
            resposta = Response.ok();
            resposta.entity(pedidoNegocio.pesquisaCodigo(pedidoDTO.getCodigo()));
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity("{\"erro\": \"" + ex.getMessage() + "\"}");
        }
        return resposta.build();
    }

    @PUT
    @Path("/concluido")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response mudarPedidoParaConcluido(PedidoDTO pedidoDTO) {
        ResponseBuilder resposta;
        try {
            pedidoNegocio.mudarPedidoParaConcluido(pedidoDTO);
            resposta = Response.ok();
            resposta.entity(pedidoNegocio.pesquisaCodigo(pedidoDTO.getCodigo()));
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity("{\"erro\": \"" + ex.getMessage() + "\"}");
        }
        return resposta.build();
    }

    @GET
    @Path("/codigo/{codigo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorCodigo(@PathParam("codigo") int codigo) {
        ResponseBuilder resposta;
        try {
            PedidoDTO pedidoDTO = pedidoNegocio.pesquisaCodigo(codigo);
            resposta = Response.ok();
            resposta.entity(pedidoDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity("{\"erro\": \"" + ex.getMessage() + "\"}");
        }
        return resposta.build();
    }

    @GET
    @Path("/dataproducao")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorDataProducao(@QueryParam ("dataInicial") String dataInicial, @QueryParam ("dataFinal") String dataFinal) {
        ResponseBuilder resposta;
        try {
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            List<PedidoDTO> listaPedidoDTO = pedidoNegocio.pesquisaPorDataProducao(LocalDate.parse(dataInicial, formato), LocalDate.parse(dataFinal, formato));
            resposta = Response.ok();
            resposta.entity(listaPedidoDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity("{\"erro\": \"" + ex.getMessage() + "\"}");
        }
        return resposta.build();
    }

    @GET
    @Path("/estado")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorEstado(@QueryParam ("estado") String estado) {
        ResponseBuilder resposta;
        try {
            List<PedidoDTO> listaPedidoDTO = pedidoNegocio.pesquisaPorEstado(EstadoPedidoDTO.valueOf(estado));
            resposta = Response.ok();
            resposta.entity(listaPedidoDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity("{\"erro\": \"" + ex.getMessage() + "\"}");
        }
        return resposta.build();
    }

    @GET
    @Path("/estadodata")
    @Produces(MediaType.APPLICATION_JSON)
    public Response pesquisaPorEstadoEData(@QueryParam ("estado") String estado, @QueryParam ("data") String data) {
        ResponseBuilder resposta;
        try {
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            List<PedidoDTO> listaPedidoDTO = pedidoNegocio.pesquisaPorEstadoEData(EstadoPedidoDTO.valueOf(estado), LocalDate.parse(data, formato));
            resposta = Response.ok();
            resposta.entity(listaPedidoDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity("{\"erro\": \"" + ex.getMessage() + "\"}");
        }
        return resposta.build();
    }

    @GET
    @Path("/cliente")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorCliente(ClienteDTO clienteDTO) {
        ResponseBuilder resposta;
        try {
            List<PedidoDTO> listaPedidoDTO = pedidoNegocio.pesquisaPorCliente(clienteDTO);
            resposta = Response.ok();
            resposta.entity(listaPedidoDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity("{\"erro\": \"" + ex.getMessage() + "\"}");
        }
        return resposta.build();
    }
}
