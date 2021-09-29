package br.com.zup.dto.request

import br.com.alura.ChavePixRequest
import br.com.alura.TipoChave
import br.com.alura.TipoConta
import br.com.zup.config.validator.ValidPixKey
import br.com.zup.dto.TipoDeChave
import br.com.zup.dto.TipoDeConta
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@ValidPixKey
@Introspected
data class NovaChaveRequest(
    @field:NotBlank(message = "Id do cliente não pode ser vazio")
    val idCliente: String,
    @field:NotNull(message = "Tipo de chave não pode ser vazio")
    val tipoChave: TipoDeChave,
    @field:NotBlank(message = "Valor da chave não pode ser vazio")
    val valor: String,
    @field:NotNull(message = "Tipo de conta não pode ser vazio")
    val tipoConta: TipoDeConta
) {
    fun toGrpc(): ChavePixRequest? {
        return ChavePixRequest.newBuilder()
            .setIdCliente(idCliente)
            .setTipoChave(when(tipoChave){
                TipoDeChave.CPF -> TipoChave.CPF
                TipoDeChave.CNPJ -> TipoChave.CNPJ
                TipoDeChave.EMAIL -> TipoChave.EMAIL
                TipoDeChave.PHONE -> TipoChave.PHONE
                else -> TipoChave.RANDOM
            })
            .setValorChave(valor)
            .setTipoConta(when(tipoConta){
                TipoDeConta.CONTA_CORRENTE -> TipoConta.CONTA_CORRENTE
                else -> TipoConta.CONTA_POUPANCA
            })
            .build()
    }
}