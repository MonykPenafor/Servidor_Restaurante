package ifmt.cba.servico;

import java.util.List;

import ifmt.cba.dto.ClienteDTO;
import ifmt.cba.negocio.ClienteNegocio;
import ifmt.cba.persistencia.ClienteDAO;
import ifmt.cba.persistencia.FabricaEntityManager;
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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

@Path("/cliente")
public class ClienteServico {

    private static ClienteDAO clienteDAO;
    private static PedidoDAO pedidoDAO;
    private static ClienteNegocio clienteNegocio;

    static {
        try {
            clienteDAO = new ClienteDAO(FabricaEntityManager.getEntityManagerProducao());
            pedidoDAO = new PedidoDAO(FabricaEntityManager.getEntityManagerProducao());
            clienteNegocio = new ClienteNegocio(clienteDAO, pedidoDAO);
        } catch (PersistenciaException e) {
            e.printStackTrace();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response adicionar(ClienteDTO clienteDTO) {
        ResponseBuilder resposta;
        try {
            clienteNegocio.inserir(clienteDTO);
            resposta = Response.ok();
            resposta.entity(clienteNegocio.pesquisaParteNome(clienteDTO.getNome()));
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity("{\"erro\": \"" + ex.getMessage() + "\"}");
        }
        return resposta.build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response alterar(ClienteDTO clienteDTO) {
        ResponseBuilder resposta;
        try {
            clienteNegocio.alterar(clienteDTO);
            resposta = Response.ok();
            resposta.entity(clienteNegocio.pesquisaCodigo(clienteDTO.getCodigo()));
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
            clienteNegocio.excluir(codigo);
            resposta = Response.ok();
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity("{\"erro\": \""+ex.getMessage()+"\"}");
        }
        return resposta.build();
    }

    @GET
    @Path("/codigo/{codigo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorCodigo(@PathParam("codigo") int codigo) {
        ResponseBuilder resposta;
        try {
            ClienteDTO clienteDTO = clienteNegocio.pesquisaCodigo(codigo);
            resposta = Response.ok();
            resposta.entity(clienteDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity("{\"erro\": \""+ex.getMessage()+"\"}");
        }
        return resposta.build();
    }

    @GET
    @Path("/nome/{nome}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorNome(@PathParam("nome") String nome) {
        ResponseBuilder resposta;
        try {
            List<ClienteDTO> listaClienteDTO = clienteNegocio.pesquisaParteNome(nome);
            resposta = Response.ok();
            resposta.entity(listaClienteDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity("{\"erro\": \""+ex.getMessage()+"\"}");
        }
        return resposta.build();
    }
}
