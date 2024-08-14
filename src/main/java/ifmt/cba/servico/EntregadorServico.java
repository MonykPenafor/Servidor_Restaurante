package ifmt.cba.servico;

import java.util.List;

import ifmt.cba.dto.EntregadorDTO;
import ifmt.cba.negocio.EntregadorNegocio;
import ifmt.cba.persistencia.EntregadorDAO;
import ifmt.cba.persistencia.FabricaEntityManager;
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

@Path("/entregador")
public class EntregadorServico {
    
    private static EntregadorDAO entregadorDAO;
    private static EntregadorNegocio entregadorNegocio;

    static {
        try {
            entregadorDAO = new EntregadorDAO(FabricaEntityManager.getEntityManagerProducao());
            entregadorNegocio = new EntregadorNegocio(entregadorDAO);
        } catch (PersistenciaException e) {
            e.printStackTrace();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response adicionar(EntregadorDTO entregadorDTO) {
        ResponseBuilder resposta;
        try {
            entregadorNegocio.inserir(entregadorDTO);
            resposta = Response.ok();
            resposta.entity(entregadorNegocio.pesquisaParteNome(entregadorDTO.getNome()));
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity("{\"erro\": \"" + ex.getMessage() + "\"}");
        }
        return resposta.build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response alterar(EntregadorDTO entregadorDTO) {
        ResponseBuilder resposta;
        try {
            entregadorNegocio.alterar(entregadorDTO);
            resposta = Response.ok();
            resposta.entity(entregadorNegocio.pesquisaCodigo(entregadorDTO.getCodigo()));
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
            entregadorNegocio.excluir(codigo);
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
            EntregadorDTO entregadorDTO = entregadorNegocio.pesquisaCodigo(codigo);
            resposta = Response.ok();
            resposta.entity(entregadorDTO);
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
            List<EntregadorDTO> listaEntregadorDTO = entregadorNegocio.pesquisaParteNome(nome);
            resposta = Response.ok();
            resposta.entity(listaEntregadorDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity("{\"erro\": \""+ex.getMessage()+"\"}");
        }
        return resposta.build();
    }

}
