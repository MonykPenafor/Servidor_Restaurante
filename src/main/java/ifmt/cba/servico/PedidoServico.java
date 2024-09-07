package ifmt.cba.servico;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import java.time.Duration;

import ifmt.cba.dto.ClienteDTO;
import ifmt.cba.dto.EstadoPedidoDTO;
import ifmt.cba.dto.PedidoDTO;
import ifmt.cba.negocio.ClienteNegocio;
import ifmt.cba.negocio.PedidoNegocio;
import ifmt.cba.persistencia.ClienteDAO;
import ifmt.cba.persistencia.FabricaEntityManager;
import ifmt.cba.persistencia.ItemPedidoDAO;
import ifmt.cba.persistencia.PedidoDAO;
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

@Path("/pedido")
public class PedidoServico {

    private static PedidoNegocio pedidoNegocio;
    private static PedidoDAO pedidoDAO;
    private static ClienteDAO clienteDAO;   
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

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
            int id = pedidoNegocio.inserir(pedidoDTO);
            PedidoDTO pedidoDTOTemp = pedidoNegocio.pesquisaCodigo(id);
            pedidoDTOTemp.setLink("/pedido/codigo/" + pedidoDTOTemp.getCodigo());
            resposta = Response.ok();
            resposta.entity(pedidoDTOTemp);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
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
            PedidoDTO pedidoDTOTemp = pedidoNegocio.pesquisaCodigo(pedidoDTO.getCodigo());
            pedidoDTOTemp.setLink("/pedido/codigo/" + pedidoDTO.getCodigo());
            resposta = Response.ok();
            resposta.entity(pedidoDTOTemp);
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
            pedidoNegocio.excluir(pedidoNegocio.pesquisaCodigo(codigo));
            resposta = Response.noContent();
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
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

            pedidoDTO.setHoraProducao(LocalTime.parse(LocalTime.now().format(formatter)));
            pedidoNegocio.mudarPedidoParaProducao(pedidoDTO);
            PedidoDTO pedidoDTOTemp = pedidoNegocio.pesquisaCodigo(pedidoDTO.getCodigo());
            pedidoDTOTemp.setLink("/pedido/codigo/" + pedidoDTO.getCodigo());
            
            resposta = Response.ok();
            resposta.entity(pedidoDTOTemp);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
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
           
            pedidoDTO.setHoraPronto(LocalTime.parse(LocalTime.now().format(formatter)));
            pedidoNegocio.mudarPedidoParaPronto(pedidoDTO);
            PedidoDTO pedidoDTOTemp = pedidoNegocio.pesquisaCodigo(pedidoDTO.getCodigo());
            pedidoDTOTemp.setLink("/pedido/codigo/" + pedidoDTO.getCodigo());
            resposta = Response.ok();
            resposta.entity(pedidoDTOTemp);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
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
            pedidoDTO.setHoraEntrega(LocalTime.parse(LocalTime.now().format(formatter)));
            pedidoNegocio.mudarPedidoParaEntrega(pedidoDTO);
            PedidoDTO pedidoDTOTemp = pedidoNegocio.pesquisaCodigo(pedidoDTO.getCodigo());
            pedidoDTOTemp.setLink("/pedido/codigo/" + pedidoDTO.getCodigo());
            resposta = Response.ok();
            resposta.entity(pedidoDTOTemp);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
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
            pedidoDTO.setHoraFinalizado(LocalTime.parse(LocalTime.now().format(formatter)));
            pedidoNegocio.mudarPedidoParaConcluido(pedidoDTO);
            PedidoDTO pedidoDTOTemp = pedidoNegocio.pesquisaCodigo(pedidoDTO.getCodigo());
            pedidoDTOTemp.setLink("/pedido/codigo/" + pedidoDTO.getCodigo());
            resposta = Response.ok();
            resposta.entity(pedidoDTOTemp);
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
            PedidoDTO pedidoDTO = pedidoNegocio.pesquisaCodigo(codigo);
            pedidoDTO.setLink("/pedido/codigo/" + pedidoDTO.getCodigo());
            resposta = Response.ok();
            resposta.entity(pedidoDTO);
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
            List<PedidoDTO> listaPedidoDTO = pedidoNegocio.pesquisaPorDataProducao(LocalDate.parse(dataInicial, formato), LocalDate.parse(dataFinal, formato));
            for (PedidoDTO pedidoDTO : listaPedidoDTO){
                pedidoDTO.setLink("/pedido/codigo/" + pedidoDTO.getCodigo());
            }
            resposta = Response.ok();
            resposta.entity(listaPedidoDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
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
            for (PedidoDTO pedidoDTO : listaPedidoDTO){
                pedidoDTO.setLink("/pedido/codigo/" + pedidoDTO.getCodigo());
            }
            resposta = Response.ok();
            resposta.entity(listaPedidoDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
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
            for (PedidoDTO pedidoDTO : listaPedidoDTO){
                pedidoDTO.setLink("/pedido/codigo/" + pedidoDTO.getCodigo());
            }
            resposta = Response.ok();
            resposta.entity(listaPedidoDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    @GET
    @Path("/cliente/{codigo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorCliente(@PathParam("codigo") int codigo) {
        ResponseBuilder resposta;
        try {
            ClienteNegocio clienteNegocio = new ClienteNegocio(clienteDAO, pedidoDAO);
            ClienteDTO clienteDTO = clienteNegocio.pesquisaCodigo(codigo);

            List<PedidoDTO> listaPedidoDTO = pedidoNegocio.pesquisaPorCliente(clienteDTO);
            for (PedidoDTO pedidoDTO : listaPedidoDTO){
                pedidoDTO.setLink("/pedido/codigo/" + pedidoDTO.getCodigo());
            }
            resposta = Response.ok();
            resposta.entity(listaPedidoDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    @GET
    @Path("/tempomedioproducao")
    @Produces(MediaType.APPLICATION_JSON)
    public Response calcularMediaTempoProducao() {
        ResponseBuilder resposta;
        try {
            long totalMinutos = 0;

            List<PedidoDTO> listaPedidoPronto = pedidoNegocio.pesquisaPorEstado(EstadoPedidoDTO.PRONTO);
            List<PedidoDTO> listaPedidoEntrega = pedidoNegocio.pesquisaPorEstado(EstadoPedidoDTO.ENTREGA);
            List<PedidoDTO> listaPedidoConcluido = pedidoNegocio.pesquisaPorEstado(EstadoPedidoDTO.CONCLUIDO);

            listaPedidoConcluido.addAll(listaPedidoEntrega);
            listaPedidoConcluido.addAll(listaPedidoPronto);

            for (PedidoDTO pedidoDTO : listaPedidoConcluido){
                pedidoDTO.setLink("/pedido/codigo/" + pedidoDTO.getCodigo());

                Duration tempo = Duration.between(pedidoDTO.getHoraPedido(), pedidoDTO.getHoraPronto());

                totalMinutos += tempo.toMinutes();
            }

            long tempoMedioEmMinutos = (listaPedidoConcluido.size() > 0) ? totalMinutos / listaPedidoConcluido.size() : 0;

            long horas = tempoMedioEmMinutos / 60;
            long minutos = tempoMedioEmMinutos % 60;

            String tempoMedioFormatado = String.format("%02d:%02d", horas, minutos);

            resposta = Response.ok();
            resposta.entity(tempoMedioFormatado);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    
    @GET
    @Path("/tempomediofinalizacao")
    @Produces(MediaType.APPLICATION_JSON)
    public Response calcularMediaTempoDeProntoAConcluido() {
        ResponseBuilder resposta;
        try {
            long totalMinutos = 0;

            List<PedidoDTO> listaPedidoConcluido = pedidoNegocio.pesquisaPorEstado(EstadoPedidoDTO.CONCLUIDO);

            for (PedidoDTO pedidoDTO : listaPedidoConcluido){
                pedidoDTO.setLink("/pedido/codigo/" + pedidoDTO.getCodigo());

                Duration tempo = Duration.between(pedidoDTO.getHoraPronto(), pedidoDTO.getHoraFinalizado());

                totalMinutos += tempo.toMinutes();
            }

            long tempoMedioEmMinutos = (listaPedidoConcluido.size() > 0) ? totalMinutos / listaPedidoConcluido.size() : 0;

            long horas = tempoMedioEmMinutos / 60;
            long minutos = tempoMedioEmMinutos % 60;

            String tempoMedioFormatado = String.format("%02d:%02d", horas, minutos);

            resposta = Response.ok();
            resposta.entity(tempoMedioFormatado);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    
}
