package ifmt.cba.servico;

import java.util.List;

import ifmt.cba.dto.PreparoProdutoDTO;
import ifmt.cba.negocio.PreparoProdutoNegocio;
import ifmt.cba.persistencia.FabricaEntityManager;
import ifmt.cba.persistencia.PersistenciaException;
import ifmt.cba.persistencia.PreparoProdutoDAO;
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

@Path("/preparo")
public class PreparoProdutoServico {

    private static PreparoProdutoNegocio preparoProdutoNegocio;
    private static PreparoProdutoDAO preparoProdutoDAO;

    static {
        try {
            preparoProdutoDAO = new PreparoProdutoDAO(FabricaEntityManager.getEntityManagerProducao());
            preparoProdutoNegocio = new PreparoProdutoNegocio(preparoProdutoDAO);
        } catch (PersistenciaException e) {
            e.printStackTrace();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response adicionar(PreparoProdutoDTO preparoProdutoDTO) {
        ResponseBuilder resposta;
        try {
            preparoProdutoNegocio.inserir(preparoProdutoDTO);
            PreparoProdutoDTO preparoProdutoDTOTemp = preparoProdutoNegocio.pesquisaPorParteNome(preparoProdutoDTO.getNome()).get(0);
            preparoProdutoDTOTemp.setLink("/preparoproduto/codigo/" + preparoProdutoDTOTemp.getCodigo());
            resposta = Response.ok();
            resposta.entity(preparoProdutoDTOTemp);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response alterar(PreparoProdutoDTO preparoProdutoDTO) {
        ResponseBuilder resposta;
        try {
            preparoProdutoNegocio.alterar(preparoProdutoDTO);
            PreparoProdutoDTO preparoProdutoDTOTemp = preparoProdutoNegocio.pesquisaPorCodigo(preparoProdutoDTO.getCodigo());
            preparoProdutoDTOTemp.setLink("/preparoproduto/codigo/" + preparoProdutoDTO.getCodigo());
            resposta = Response.ok();
            resposta.entity(preparoProdutoDTOTemp);
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
            preparoProdutoNegocio.excluir(codigo);
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
    public Response buscarProdutoPorCodigo(@PathParam("codigo") int codigo) {
        ResponseBuilder resposta;
        try {
            PreparoProdutoDTO preparoProdutoDTO = preparoProdutoNegocio.pesquisaPorCodigo(codigo);
            preparoProdutoDTO.setLink("/preparoproduto/codigo/"+preparoProdutoDTO.getCodigo());
            resposta = Response.ok();
            resposta.entity(preparoProdutoDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    @GET
    @Path("/nome/{nome}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPreparoProdutoPorNome(@PathParam("nome") String nome) {
        ResponseBuilder resposta;
        try {
            List<PreparoProdutoDTO> listaPreparoProdutoDTO = preparoProdutoNegocio.pesquisaPorParteNome(nome);
            for (PreparoProdutoDTO preparoProdutoDTO : listaPreparoProdutoDTO) {
                preparoProdutoDTO.setLink("/preparoproduto/codigo/" + preparoProdutoDTO.getCodigo());

            };
            resposta = Response.ok();
            resposta.entity(listaPreparoProdutoDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    @GET
    @Path("/produto/{produto}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPreparoProdutoPorProduto(@PathParam("produto") int codigoProduto) {
        ResponseBuilder resposta;
        try {
            List<PreparoProdutoDTO> listaPreparoProdutoDTO = preparoProdutoNegocio.pesquisaPorProduto(codigoProduto);
            for(PreparoProdutoDTO preparoProdutoDTO : listaPreparoProdutoDTO){
                preparoProdutoDTO.setLink("/preparoproduto/codigo/" + preparoProdutoDTO.getCodigo());
            }
            resposta = Response.ok();
            resposta.entity(listaPreparoProdutoDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }
}