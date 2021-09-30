package br.com.zup.dto.response

import br.com.zup.dto.TipoDeConta

data class ContaResponse(
    val tipoConta: TipoDeConta,
    val instituicao: String,
    val nomeDoTitular: String,
    val cpfDoTitular: String,
    val agencia: String,
    val numeroDaConta: String
)
