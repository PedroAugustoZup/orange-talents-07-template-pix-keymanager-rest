package br.com.alura.config.handler

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.aop.InterceptorBean
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import io.micronaut.http.HttpResponse
import jakarta.inject.Singleton
import javax.validation.ValidationException

@Singleton
@InterceptorBean(ErrorAroundHandler::class)
class ErrorAroundHandlerInterceptor : MethodInterceptor<Any, Any> {

    override fun intercept(context: MethodInvocationContext<Any, Any>): Any? {
        return try {
           context.proceed()
        } catch (ex: Exception) {

            if(ex is StatusRuntimeException){
                when(ex.status.code){
                    Status.INVALID_ARGUMENT.code -> HttpResponse.badRequest(ex.message)
                    Status.ALREADY_EXISTS.code -> HttpResponse.badRequest(ex.message)
                    else -> HttpResponse.serverError("Erro inesperado")
                }
            }else{
                when(ex){
                    is IllegalArgumentException -> HttpResponse.badRequest(ex.message)
                    is ValidationException -> HttpResponse.badRequest(ex.message)
                    else -> HttpResponse.serverError("Erro inesperado")
                }
            }
        }
    }

}