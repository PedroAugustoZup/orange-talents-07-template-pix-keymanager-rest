package br.com.zup.dto.response

data class ListaChaveIdResponse(
    val clientId: String,
    val pixId: String,
    val chave: ChavePixResponse
)
