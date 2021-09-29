package br.com.zup.controller

import br.com.alura.ChavePixServiceRegistraGrpc
import br.com.zup.config.handler.ErrorAroundHandler
import br.com.zup.dto.request.NovaChaveRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import javax.validation.Valid

@Validated
@ErrorAroundHandler
@Controller("/pix")
class ChavePixController(val grpcClient: ChavePixServiceRegistraGrpc.ChavePixServiceRegistraBlockingStub) {

    @Post
    fun registra(@Body @Valid request: NovaChaveRequest): HttpResponse<Any>{
        val response = grpcClient.registra(request.toGrpc())
        return HttpResponse.ok(response.idChave)
    }

}
