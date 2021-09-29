package br.com.zup.dto.request

import br.com.zup.TipoChave
import br.com.zup.TipoConta
import br.com.zup.dto.TipoDeChave
import br.com.zup.dto.TipoDeConta
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class NovaChaveRequestTest {

    var id = ""
    @BeforeEach
    fun setUp() {
        id = UUID.randomUUID().toString()
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun `testa chave do tipo CNPJ`(){
        val request = NovaChaveRequest(
            id,
            TipoDeChave.CNPJ,
            "23.321.321/0001-32",
            TipoDeConta.CONTA_POUPANCA
        ).toGrpc()
        assertEquals(TipoChave.CNPJ.name, request!!.tipoChave.name)
        assertEquals(TipoConta.CONTA_POUPANCA.name, request.tipoConta.name)
    }

    @Test
    fun `testa chave do tipo EMAIL`(){
        val request = NovaChaveRequest(
            id,
            TipoDeChave.EMAIL,
            "pedro.marques@zup.com.br",
            TipoDeConta.CONTA_POUPANCA
        ).toGrpc()

        assertEquals(TipoChave.EMAIL.name, request!!.tipoChave.name)
        assertEquals(TipoConta.CONTA_POUPANCA.name, request.tipoConta.name)
    }

    @Test
    fun `testa chave do tipo PHONE`(){
        val request = NovaChaveRequest(
            id,
            TipoDeChave.PHONE,
            "+55(34)99999-9999",
            TipoDeConta.CONTA_POUPANCA
        ).toGrpc()

        assertEquals(TipoChave.PHONE.name, request!!.tipoChave.name)
        assertEquals(TipoConta.CONTA_POUPANCA.name, request.tipoConta.name)
    }

    @Test
    fun `testa chave do tipo RANDOM`(){
        val request = NovaChaveRequest(
            id,
            TipoDeChave.RANDOM,
            "",
            TipoDeConta.CONTA_POUPANCA
        ).toGrpc()

        assertEquals(TipoChave.RANDOM.name, request!!.tipoChave.name)
        assertEquals(TipoConta.CONTA_POUPANCA.name, request.tipoConta.name)
    }
}