package br.com.zup.factory

import br.com.zup.ChavePixServiceRegistraGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import jakarta.inject.Singleton

@Factory
class KeyManagerFactory {

    @Singleton
    fun registraChave(@GrpcChannel("keyManager") channel: ManagedChannel): ChavePixServiceRegistraGrpc.ChavePixServiceRegistraBlockingStub{
        return ChavePixServiceRegistraGrpc.newBlockingStub(channel)
    }
}