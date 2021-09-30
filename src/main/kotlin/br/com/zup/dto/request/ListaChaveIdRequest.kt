package br.com.zup.dto.request

import br.com.zup.CarregaChavePixRequest
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class ListaChaveIdRequest(
    @field:NotBlank val clientId: String,
    @field:NotBlank val pixId: String
) {
    fun toGrpc(): CarregaChavePixRequest? {
        val filtroPorPixId = CarregaChavePixRequest.FiltroPorPixId.newBuilder()
            .setPixId(pixId)
            .setClienteId(clientId)
            .build()
        return CarregaChavePixRequest.newBuilder()
            .setPixId(filtroPorPixId)
            .build()
    }
}
