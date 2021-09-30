package br.com.zup.dto.response

import br.com.zup.dto.TipoDeChave

data class ChavePixResponse(
    val contaInfo: ContaResponse,
    val tipoChave: TipoDeChave,
    val valorChave: String
)