package ifmt.cba.servico;

import ifmt.cba.dto.CardapioDTO;
import ifmt.cba.negocio.CardapioNegocio;
import ifmt.cba.persistencia.CardapioDAO;
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

@Path("/cardapio")
public class CardapioServico {

    private static CardapioDAO cardapioDAO;
    private static CardapioNegocio cardapioNegocio;

    static {
        try {
            cardapioDAO = new CardapioDAO(FabricaEntityManager.getEntityManagerProducao());
            cardapioNegocio = new CardapioNegocio(cardapioDAO);
        } catch (PersistenciaException e) {
            e.printStackTrace();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response adicionar(CardapioDTO cardapioDTO) {
        ResponseBuilder resposta;
        try {
            cardapioNegocio.inserir(cardapioDTO);
            resposta = Response.ok();
            resposta.entity(cardapioNegocio.pesquisaPorNome(cardapioDTO.getNome()));
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity("{\"erro\": \"" + ex.getMessage() + "\"}");
        }
        return resposta.build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response alterar(CardapioDTO cardapioDTO) {
        ResponseBuilder resposta;
        try {
            cardapioNegocio.alterar(cardapioDTO);
            resposta = Response.ok();
            resposta.entity(cardapioNegocio.pesquisaCodigo(cardapioDTO.getCodigo()));
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
            cardapioNegocio.excluir(codigo);
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
            CardapioDTO cardapioDTO = cardapioNegocio.pesquisaCodigo(codigo);
            resposta = Response.ok();
            resposta.entity(cardapioDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity("{\"erro\": \""+ex.getMessage()+"\"}");
        }
        return resposta.build();
    }

    @GET
    @Path("/nome/{nome}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarProdutoPorNome(@PathParam("nome") String nome) {
        ResponseBuilder resposta;
        try {
            CardapioDTO cardapioDTO = cardapioNegocio.pesquisaPorNome(nome).get(0);
            resposta = Response.ok();
            resposta.entity(cardapioDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity("{\"erro\": \""+ex.getMessage()+"\"}");
        }
        return resposta.build();
    }
}
