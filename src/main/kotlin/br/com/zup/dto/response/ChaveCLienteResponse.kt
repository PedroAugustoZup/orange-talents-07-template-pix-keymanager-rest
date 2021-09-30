package br.com.zup.dto.response

import br.com.zup.dto.TipoDeChave
import br.com.zup.dto.TipoDeConta

data class ChaveCLienteResponse(
    val pixId: String,
    val clientId: String,
    val tipoDeChave: TipoDeChave,
    val valorChave: String,
    val tipoDeConta: TipoDeConta
)
