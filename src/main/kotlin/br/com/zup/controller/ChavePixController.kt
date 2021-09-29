package br.com.zup.controller

import br.com.zup.ChavePixServiceRegistraGrpc
import br.com.zup.ChavePixServiceRemoveGrpc
import br.com.zup.config.handler.ErrorAroundHandler
import br.com.zup.dto.request.NovaChaveRequest
import br.com.zup.dto.request.RemoveChaveRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import javax.validation.Valid

@Validated
@ErrorAroundHandler
@Controller("/pix")
class ChavePixController(val grpcClientRegistra: ChavePixServiceRegistraGrpc.ChavePixServiceRegistraBlockingStub,
    val grpcClientRemove: ChavePixServiceRemoveGrpc.ChavePixServiceRemoveBlockingStub
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

}
