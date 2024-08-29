package ifmt.cba.servico;

import java.util.List;

import ifmt.cba.dto.GrupoAlimentarDTO;
import ifmt.cba.negocio.GrupoAlimentarNegocio;
import ifmt.cba.negocio.NegocioException;
import ifmt.cba.persistencia.FabricaEntityManager;
import ifmt.cba.persistencia.GrupoAlimentarDAO;
import ifmt.cba.persistencia.PersistenciaException;
import ifmt.cba.persistencia.ProdutoDAO;
import ifmt.cba.servico.util.MensagemErro;
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

@Path("/grupoalimentar")
public class GrupoAlimentarServico {

    private static GrupoAlimentarNegocio grupoAlimentarNegocio;

    private static GrupoAlimentarDAO grupoAlimentarDAO;
    private static ProdutoDAO produtoDAO;

    static {
        try {
            grupoAlimentarDAO = new GrupoAlimentarDAO(FabricaEntityManager.getEntityManagerProducao());
            produtoDAO = new ProdutoDAO(FabricaEntityManager.getEntityManagerProducao());
            grupoAlimentarNegocio = new GrupoAlimentarNegocio(grupoAlimentarDAO, produtoDAO);
        } catch (PersistenciaException | NegocioException e) {
            e.printStackTrace();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response adicionar(GrupoAlimentarDTO grupoAlimentarDTO) {
        ResponseBuilder resposta;
        try {
            grupoAlimentarNegocio.inserir(grupoAlimentarDTO);
            GrupoAlimentarDTO grupoAlimentarDTOTemp = grupoAlimentarNegocio.pesquisaParteNome(grupoAlimentarDTO.getNome()).get(0);
            grupoAlimentarDTOTemp.setLink("/grupoalimentar/codigo/"+grupoAlimentarDTOTemp.getCodigo());
            resposta = Response.ok();
            resposta.entity(grupoAlimentarDTOTemp);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response alterar(GrupoAlimentarDTO grupoAlimentarDTO) {
        ResponseBuilder resposta;
        try {
            grupoAlimentarNegocio.alterar(grupoAlimentarDTO);
            GrupoAlimentarDTO grupoAlimentarDTOTemp = grupoAlimentarNegocio.pesquisaCodigo(grupoAlimentarDTO.getCodigo());
            grupoAlimentarDTOTemp.setLink("/grupoalimentar/codigo/"+grupoAlimentarDTO.getCodigo());
            resposta = Response.ok();
            resposta.entity(grupoAlimentarDTOTemp);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    @DELETE
    @Path("/{codigo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response excluir(@PathParam("codigo") int codigo) {
        ResponseBuilder resposta;
        try {
            grupoAlimentarNegocio.excluir(codigo);
            resposta = Response.noContent();
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    @GET
    @Path("/codigo/{codigo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarGrupoAlimentarPorCodigo(@PathParam("codigo") int codigo) {
        ResponseBuilder resposta;
        try {
            GrupoAlimentarDTO grupoAlimentarDTO = grupoAlimentarNegocio.pesquisaCodigo(codigo);
            grupoAlimentarDTO.setLink("/grupoalimentar/codigo/"+grupoAlimentarDTO.getCodigo());
            resposta = Response.ok();
            resposta.entity(grupoAlimentarDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    @GET
    @Path("/nome/{nome}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarGrupoAlimentarPorNome(@PathParam("nome") String nome) {
        ResponseBuilder resposta;
        try {
            List<GrupoAlimentarDTO> listaGrupoAlimentarDTO = grupoAlimentarNegocio.pesquisaParteNome(nome);
            for (GrupoAlimentarDTO grupoAlimentarDTO : listaGrupoAlimentarDTO) {
                grupoAlimentarDTO.setLink("/grupoalimentar/codigo/"+grupoAlimentarDTO.getCodigo());
            }
            resposta = Response.ok();
            resposta.entity(listaGrupoAlimentarDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }
}