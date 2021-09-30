package br.com.zup.controller

import br.com.zup.*
import br.com.zup.config.handler.ErrorAroundHandler
import br.com.zup.dto.TipoDeChave
import br.com.zup.dto.TipoDeConta
import br.com.zup.dto.request.ListaChaveIdRequest
import br.com.zup.dto.request.NovaChaveRequest
import br.com.zup.dto.request.RemoveChaveRequest
import br.com.zup.dto.response.ChaveCLienteResponse
import br.com.zup.dto.response.ChavePixResponse
import br.com.zup.dto.response.ContaResponse
import br.com.zup.dto.response.ListaChaveIdResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.validation.Validated
import javax.validation.Valid

@Validated
@ErrorAroundHandler
@Controller("/pix")
class ChavePixController(
    val grpcClientRegistra: ChavePixServiceRegistraGrpc.ChavePixServiceRegistraBlockingStub,
    val grpcClientRemove: ChavePixServiceRemoveGrpc.ChavePixServiceRemoveBlockingStub,
    val grpcClientCarrega: ChavePixServiceCarregaGrpc.ChavePixServiceCarregaBlockingStub,
    val grpcClientListaPorCliente: ChavePixServiceListaChaveClienteGrpc.ChavePixServiceListaChaveClienteBlockingStub,
) {

    @Post
    fun registra(@Body @Valid request: NovaChaveRequest): HttpResponse<Any>{
        val response = grpcClientRegistra.registra(request.toGrpc())
        return HttpResponse.ok(response.idChave)
    }

    @Delete
    fun remove(@Body @Valid request: RemoveChaveRequest): HttpResponse<Any>{
        val response = grpcClientRemove.excluir(request.toGrpc())
        return HttpResponse.ok(response.mensagem)
    }

    @Get
    fun detalhaChavePorId(@Body @Valid request: ListaChaveIdRequest): HttpResponse<Any>{
        val response = grpcClientCarrega.carrega(request.toGrpc())
        val responseDto = response.toDto()
        return HttpResponse.ok(responseDto)
    }

    @Get("?{chave}")
    fun detalhaChavePorValor(@QueryValue("chave") chave: String): HttpResponse<Any>{
        val response = grpcClientCarrega.carrega(CarregaChavePixRequest.newBuilder()
            .setChave(chave).build())
        val responseDto = response.toDto()
        return HttpResponse.ok(responseDto)
    }

    @Get("/cliente/{id}")
    fun detalhaChavePorCliente(@PathVariable("id") idCliente: String): HttpResponse<Any>{
        val response = grpcClientListaPorCliente.lista(ListaChaveClienteRequest.newBuilder()
            .setClientId(idCliente)
            .build())
        return HttpResponse.ok(response.toDto())
    }

}

private fun ListaChaveClienteResponse.toDto(): List<ChaveCLienteResponse> {
    return chavesList.map{ChaveCLienteResponse(it.pixId, it.clientId, when(it.tipoChave){
        TipoChave.CNPJ -> TipoDeChave.CNPJ
        TipoChave.CPF -> TipoDeChave.CPF
        TipoChave.EMAIL -> TipoDeChave.EMAIL
        TipoChave.PHONE -> TipoDeChave.PHONE
        TipoChave.RANDOM -> TipoDeChave.RANDOM
        else -> TipoDeChave.RANDOM
    }, it.valorChave, when(it.tipoConta){
        TipoConta.CONTA_POUPANCA -> TipoDeConta.CONTA_POUPANCA
        TipoConta.CONTA_CORRENTE -> TipoDeConta.CONTA_CORRENTE
        else -> TipoDeConta.CONTA_POUPANCA
    })}
}

private fun CarregaChavePixResponse.toDto(): ListaChaveIdResponse? {
    val contaResponse = chave.conta
    val conta = ContaResponse(
        when (contaResponse.tipoConta) {
            TipoConta.CONTA_POUPANCA -> TipoDeConta.CONTA_POUPANCA
            TipoConta.CONTA_CORRENTE -> TipoDeConta.CONTA_CORRENTE
            else -> TipoDeConta.CONTA_POUPANCA
        },
        contaResponse.instituicao,
        contaResponse.nomeDoTitular,
        contaResponse.cpfDoTitular,
        contaResponse.agencia,
        contaResponse.numeroDaConta
    )

    val chave = ChavePixResponse(
        conta,
        when (chave.tipoChave) {
            TipoChave.CNPJ -> TipoDeChave.CNPJ
            TipoChave.CPF -> TipoDeChave.CPF
            TipoChave.EMAIL -> TipoDeChave.EMAIL
            TipoChave.PHONE -> TipoDeChave.PHONE
            TipoChave.RANDOM -> TipoDeChave.RANDOM
            else -> TipoDeChave.RANDOM
        },
        chave.chave
    )
    return ListaChaveIdResponse(clienteId, pixId, chave)
}
