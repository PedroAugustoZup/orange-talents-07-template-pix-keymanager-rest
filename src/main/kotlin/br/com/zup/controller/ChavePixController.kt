package br.com.zup.controller

import br.com.alura.config.handler.ErrorAroundHandler
import br.com.zup.dto.request.NovaChaveRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import javax.validation.Valid
import javax.validation.Validator

@Validated
@ErrorAroundHandler
@Controller("/pix")
class ChavePixController(private val validator: Validator) {

    @Post
    fun registra(@Body @Valid request: NovaChaveRequest): HttpResponse<Any>{
        return HttpResponse.ok()
    }

}