package br.com.zup.config.validator

import br.com.zup.dto.TipoDeChave
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class ValidPixKeyKtTest {
    @Test
    fun `deve dar true para o CPF`() {
        assertTrue(TipoDeChave.CPF.validaKey("78394589634"))
    }
    @Test
    fun `deve dar false para o CPF`() {
        assertFalse(TipoDeChave.CPF.validaKey(""))
    }
    @Test
    fun `deve dar true para o RANDOM`() {
        assertTrue(TipoDeChave.RANDOM.validaKey(""))
    }
    @Test
    fun `deve dar false para o RANDOM`() {
        assertFalse(TipoDeChave.RANDOM.validaKey("dsdasdsa"))
    }
    @Test
    fun `deve dar true para o PHONE`() {
        assertTrue(TipoDeChave.PHONE.validaKey("+5534999999999"))
    }
    @Test
    fun `deve dar false para o PHONE`() {
        assertFalse(TipoDeChave.PHONE.validaKey(""))
    }
    @Test
    fun `deve dar true para o CNPJ`() {
        assertTrue(TipoDeChave.CNPJ.validaKey("28.422.647/0001-20"))
    }
    @Test
    fun `deve dar false para o CNPJ`() {
        assertFalse(TipoDeChave.CNPJ.validaKey(""))
    }
    @Test
    fun `deve dar true para o EMAIL`() {
        assertTrue(TipoDeChave.EMAIL.validaKey("dasdas@gmail.com"))
    }
    @Test
    fun `deve dar false para o EMAIL`() {
        assertFalse(TipoDeChave.EMAIL.validaKey(""))
    }
}