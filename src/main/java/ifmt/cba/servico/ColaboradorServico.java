package ifmt.cba.servico;

import java.util.List;

import ifmt.cba.persistencia.FabricaEntityManager;
import ifmt.cba.persistencia.PersistenciaException;
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

import ifmt.cba.dto.ColaboradorDTO;
import ifmt.cba.persistencia.ColaboradorDAO;
import ifmt.cba.negocio.ColaboradorNegocio;

@Path("/colaborador")
public class ColaboradorServico {
    
    private static ColaboradorDAO colaboradorDAO;
    private static ColaboradorNegocio colaboradorNegocio;

    static {
        try {
            colaboradorDAO = new ColaboradorDAO(FabricaEntityManager.getEntityManagerProducao());
            colaboradorNegocio = new ColaboradorNegocio(colaboradorDAO);
        } catch (PersistenciaException e) {
            e.printStackTrace();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response adicionar(ColaboradorDTO colaboradorDTO) {
        ResponseBuilder resposta;
        try {
            colaboradorNegocio.inserir(colaboradorDTO);
            ColaboradorDTO colaboradorDTOTemp = colaboradorNegocio.pesquisaParteNome(colaboradorDTO.getNome()).get(0);
            colaboradorDTOTemp.setLink("/colaborador/codigo/"+colaboradorDTOTemp.getCodigo());
            resposta = Response.ok();
            resposta.entity(colaboradorDTOTemp);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response alterar(ColaboradorDTO colaboradorDTO) {
        ResponseBuilder resposta;
        try {
            colaboradorNegocio.alterar(colaboradorDTO);
            ColaboradorDTO colaboradorDTOTemp = colaboradorNegocio.pesquisaCodigo(colaboradorDTO.getCodigo());
            colaboradorDTOTemp.setLink("/colaborador/codigo/"+colaboradorDTOTemp.getCodigo());
            resposta = Response.ok();
            resposta.entity(colaboradorDTOTemp);
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
            colaboradorNegocio.excluir(codigo);
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
    public Response buscarPorCodigo(@PathParam("codigo") int codigo) {
        ResponseBuilder resposta;
        try {
            ColaboradorDTO colaboradorDTO = colaboradorNegocio.pesquisaCodigo(codigo);
            colaboradorDTO.setLink("/colaborador/codigo/"+colaboradorDTO.getCodigo());
            resposta = Response.ok();
            resposta.entity(colaboradorDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    @GET
    @Path("/nome/{nome}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorNome(@PathParam("nome") String nome) {
        ResponseBuilder resposta;
        try {
            List<ColaboradorDTO> listaColaboradorDTO = colaboradorNegocio.pesquisaParteNome(nome);
            for (ColaboradorDTO colaboradorDTO : listaColaboradorDTO){
                colaboradorDTO.setLink("/colaborador/codigo/"+colaboradorDTO.getCodigo());
            }
            resposta = Response.ok();
            resposta.entity(listaColaboradorDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

}
