package br.com.zup.config.validator

import br.com.zup.dto.TipoDeChave
import br.com.zup.dto.request.NovaChaveRequest
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import jakarta.inject.Singleton
import javax.validation.Constraint
import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidPixKeyClass::class])
annotation class ValidPixKey(
    val message:String = "Verifique o tipo e o valor de sua chave",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Any>> = []
)

@Singleton
class ValidPixKeyClass: ConstraintValidator<ValidPixKey, NovaChaveRequest> {

    override fun isValid(
        value: NovaChaveRequest?,
        annotationMetadata: AnnotationValue<ValidPixKey>,
        context: ConstraintValidatorContext
    ): Boolean {
        return value?.valor?.let { key ->
            value.tipoChave?.validaKey(key)
        } ?: true
    }
}


fun TipoDeChave.validaKey(key: String): Boolean {
    when (this){
        TipoDeChave.CPF -> {
            return key.matches("^[0-9]{11}\$".toRegex())
        }
        TipoDeChave.CNPJ -> {
            return key.matches("^\\d{2}\\.\\d{3}\\.\\d{3}\\/\\d{4}\\-\\d{2}\$".toRegex())
        }
        TipoDeChave.PHONE -> {
            return key.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
        }
        TipoDeChave.EMAIL -> {
            return key.matches("[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?".toRegex())
        }
        TipoDeChave.RANDOM -> {
            return key.isEmpty()
        }
    }
}
