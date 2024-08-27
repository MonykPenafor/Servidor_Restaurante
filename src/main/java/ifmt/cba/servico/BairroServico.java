package ifmt.cba.servico;

import java.util.List;

import ifmt.cba.dto.BairroDTO;
import ifmt.cba.negocio.BairroNegocio;
import ifmt.cba.persistencia.BairroDAO;
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

@Path("/bairro")
public class BairroServico {

    private static BairroNegocio bairroNegocio;

    private static BairroDAO bairroDAO;

    static {
        try {
            bairroDAO = new BairroDAO(FabricaEntityManager.getEntityManagerProducao());
            bairroNegocio = new BairroNegocio(bairroDAO);
        } catch (PersistenciaException e) {
            e.printStackTrace();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response adicionar(BairroDTO bairroDTO) {
        ResponseBuilder resposta;
        try {
            bairroNegocio.inserir(bairroDTO);
            BairroDTO bairroDTOTemp = bairroNegocio.pesquisaParteNome(bairroDTO.getNome()).get(0);
            bairroDTOTemp.setLink("/bairro/codigo/"+ bairroDTOTemp.getCodigo());
            resposta = Response.ok();
            resposta.entity(bairroDTOTemp);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response alterar(BairroDTO bairroDTO) {
        ResponseBuilder resposta;
        try {
            bairroNegocio.alterar(bairroDTO);
            BairroDTO bairroDTOTemp = bairroNegocio.pesquisaCodigo(bairroDTO.getCodigo());
            bairroDTOTemp.setLink("/bairro/codigo/"+bairroDTO.getCodigo());
            resposta = Response.ok();
            resposta.entity(bairroDTOTemp);
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
            bairroNegocio.excluir(codigo);
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
    public Response buscarBairroPorCodigo(@PathParam("codigo") int codigo) {
        ResponseBuilder resposta;
        try {
            BairroDTO bairroDTO = bairroNegocio.pesquisaCodigo(codigo);
            bairroDTO.setLink("/bairro/codigo/"+bairroDTO.getCodigo());
            resposta = Response.ok();
            resposta.entity(bairroDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    @GET
    @Path("/nome/{nome}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarBairroPorNome(@PathParam("nome") String nome) {
        ResponseBuilder resposta;
        try {
            List<BairroDTO> listaBairroDTO = bairroNegocio.pesquisaParteNome(nome);
            for (BairroDTO bairroDTO : listaBairroDTO) {
                bairroDTO.setLink("/bairro/codigo/"+bairroDTO.getCodigo());
            }
            resposta = Response.ok();
            resposta.entity(listaBairroDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }
}
