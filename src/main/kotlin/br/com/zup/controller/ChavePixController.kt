package br.com.zup.controller

import br.com.zup.CarregaChavePixResponse
import br.com.zup.ChavePixServiceCarregaGrpc
import br.com.zup.ChavePixServiceRegistraGrpc
import br.com.zup.ChavePixServiceRemoveGrpc
import br.com.zup.config.handler.ErrorAroundHandler
import br.com.zup.dto.request.ListaChaveIdRequest
import br.com.zup.dto.request.NovaChaveRequest
import br.com.zup.dto.request.RemoveChaveRequest
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
    val grpcClientCarrega: ChavePixServiceCarregaGrpc.ChavePixServiceCarregaBlockingStub
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

    @Get()
    fun detalhaChavePorId(@Body @Valid request: ListaChaveIdRequest): HttpResponse<Any>{
        val response = grpcClientCarrega.carrega(request.toGrpc())

        val responseDto = response.toDto()
        return HttpResponse.ok(responseDto)
    }

}

private fun CarregaChavePixResponse.toDto(): ListaChaveIdResponse? {
    return
}
