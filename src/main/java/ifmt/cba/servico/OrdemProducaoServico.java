package ifmt.cba.servico;

import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import ifmt.cba.dto.EstadoOrdemProducaoDTO;
import ifmt.cba.dto.OrdemProducaoDTO;
import ifmt.cba.negocio.NegocioException;
import ifmt.cba.negocio.OrdemProducaoNegocio;
import ifmt.cba.persistencia.FabricaEntityManager;
import ifmt.cba.persistencia.ItemOrdemProducaoDAO;
import ifmt.cba.persistencia.OrdemProducaoDAO;
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
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

@Path("/ordemproducao")
public class OrdemProducaoServico {

    private static OrdemProducaoNegocio ordemProducaoNegocio;
    private static OrdemProducaoDAO ordemProducaoDAO;
    private static ItemOrdemProducaoDAO itemOrdemProducaoDAO;

    static {
        try {
            ordemProducaoDAO = new OrdemProducaoDAO(FabricaEntityManager.getEntityManagerProducao());
            itemOrdemProducaoDAO = new ItemOrdemProducaoDAO(FabricaEntityManager.getEntityManagerProducao());
            ordemProducaoNegocio = new OrdemProducaoNegocio(ordemProducaoDAO, itemOrdemProducaoDAO);
        } catch (PersistenciaException | NegocioException e) {
            e.printStackTrace();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response adicionar(OrdemProducaoDTO ordemProducaoDTO) {
        ResponseBuilder resposta;
        try {
            int id = ordemProducaoNegocio.inserir(ordemProducaoDTO);
            OrdemProducaoDTO ordemProducaoDTOTemp = ordemProducaoNegocio.pesquisaCodigo(id);
            ordemProducaoDTOTemp.setLink("/ordemproducao/codigo/" + ordemProducaoDTOTemp.getCodigo());
            resposta = Response.ok();
            resposta.entity(ordemProducaoDTOTemp);

        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response alterar(OrdemProducaoDTO ordemProducaoDTO) {
        ResponseBuilder resposta;
        try {
            ordemProducaoNegocio.alterar(ordemProducaoDTO);
            OrdemProducaoDTO ordemProducaoDTOTemp = ordemProducaoNegocio.pesquisaCodigo(ordemProducaoDTO.getCodigo());
            ordemProducaoDTOTemp.setLink("/ordemproducao/codigo/" + ordemProducaoDTO.getCodigo());
            resposta = Response.ok();
            resposta.entity(ordemProducaoDTOTemp);
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
            ordemProducaoNegocio.excluir(ordemProducaoNegocio.pesquisaCodigo(codigo));
            resposta = Response.noContent();
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
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
            List<OrdemProducaoDTO> listaOrdemProducaoDTO = ordemProducaoNegocio.pesquisaPorDataProducao(LocalDate.parse(dataInicial, formato), LocalDate.parse(dataFinal, formato));
            for (OrdemProducaoDTO ordemProducaoDTO : listaOrdemProducaoDTO){
                ordemProducaoDTO.setLink("/ordemproducao/codigo/" + ordemProducaoDTO.getCodigo());
            }
            resposta = Response.ok();
            resposta.entity(listaOrdemProducaoDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    @GET
    @Path("/estadodata")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorEstadoEDataProducao(@QueryParam ("dataInicial") String dataInicial, @QueryParam ("dataFinal") String dataFinal, @QueryParam ("estado") EstadoOrdemProducaoDTO estado) {
        ResponseBuilder resposta;
        try {
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            List<OrdemProducaoDTO> listaOrdemProducaoDTO = ordemProducaoNegocio.pesquisaPorEstadoEDataProcucao(estado, LocalDate.parse(dataInicial, formato), LocalDate.parse(dataFinal, formato));
            for (OrdemProducaoDTO ordemProducaoDTO : listaOrdemProducaoDTO){
                ordemProducaoDTO.setLink("/ordemproducao/codigo/" + ordemProducaoDTO.getCodigo());
            }
            resposta = Response.ok();
            resposta.entity(listaOrdemProducaoDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }



    @PUT
    @Path("/processar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response processarOrdemProducao(OrdemProducaoDTO ordemProducaoDTO) {
        ResponseBuilder resposta;
        try {
            ordemProducaoNegocio.processarOrdemProducao(ordemProducaoDTO);
            OrdemProducaoDTO ordemProducaoDTOTemp = ordemProducaoNegocio.pesquisaCodigo(ordemProducaoDTO.getCodigo());
            ordemProducaoDTOTemp.setLink("/ordemproducao/codigo/" + ordemProducaoDTO.getCodigo());
            resposta = Response.ok();
            resposta.entity(ordemProducaoDTOTemp);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }





}