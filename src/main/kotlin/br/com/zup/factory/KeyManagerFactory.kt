package br.com.zup.factory

import br.com.zup.ChavePixServiceCarregaGrpc
import br.com.zup.ChavePixServiceRegistraGrpc
import br.com.zup.ChavePixServiceRemoveGrpc
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

    @Singleton
    fun removeChave(@GrpcChannel("keyManager") channel: ManagedChannel): ChavePixServiceRemoveGrpc.ChavePixServiceRemoveBlockingStub{
        return ChavePixServiceRemoveGrpc.newBlockingStub(channel)
    }

    @Singleton
    fun detalhaChave(@GrpcChannel("keyManager") channel: ManagedChannel): ChavePixServiceCarregaGrpc.ChavePixServiceCarregaBlockingStub{
        return ChavePixServiceCarregaGrpc.newBlockingStub(channel)
    }
}