package br.com.zup.dto.request

import br.com.zup.RemoverChavePixRequest
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class RemoveChaveRequest(
    @field:NotBlank val pixId: String,
    @field:NotBlank val isbp: String
) {
    fun toGrpc(): RemoverChavePixRequest? {
        return RemoverChavePixRequest.newBuilder()
            .setIsbp(isbp)
            .setPixId(pixId)
            .build()
    }
}
