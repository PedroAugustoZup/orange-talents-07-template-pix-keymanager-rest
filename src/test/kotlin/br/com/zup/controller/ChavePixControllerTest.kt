package br.com.zup.controller

import br.com.zup.*
import br.com.zup.dto.TipoDeChave
import br.com.zup.dto.TipoDeConta
import br.com.zup.dto.request.NovaChaveRequest
import br.com.zup.dto.request.RemoveChaveRequest
import br.com.zup.dto.response.ListaChaveIdResponse
import br.com.zup.factory.KeyManagerFactory
import io.grpc.Status
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import java.util.*

@MicronautTest
internal class ChavePixControllerTest {

    @field:Inject
    lateinit var grpcClientRegistra: ChavePixServiceRegistraGrpc.ChavePixServiceRegistraBlockingStub

    @field:Inject
    lateinit var grpcClientRemove: ChavePixServiceRemoveGrpc.ChavePixServiceRemoveBlockingStub

    @field:Inject
    lateinit var grpcClientCarrega: ChavePixServiceCarregaGrpc.ChavePixServiceCarregaBlockingStub

    @field:Inject
    lateinit var grpcClientLista: ChavePixServiceListaChaveClienteGrpc.ChavePixServiceListaChaveClienteBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    var id = ""
    @BeforeEach
    fun setUp() {
        id = UUID.randomUUID().toString()
    }

    @Test
    fun `deve retornar o ok de chave cadastrada`() {

        Mockito.`when`(grpcClientRegistra.registra(
            ChavePixRequest.newBuilder()
            .setValorChave("14331247648")
            .setTipoConta(TipoConta.CONTA_CORRENTE)
            .setTipoChave(TipoChave.CPF)
            .setIdCliente(id)
            .build()))
            .thenReturn(
                ChavePixResponse.newBuilder()
                .setIdChave("1")
                .build())

        val httpRequest = HttpRequest.POST("pix", NovaChaveRequest(
            id,
            TipoDeChave.CPF,
            "14331247648",
            TipoDeConta.CONTA_CORRENTE
        ))
        val response = httpClient.toBlocking().exchange(httpRequest, String::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertEquals("1", response.body())
    }

    @Test
    fun `deve dar erro ao cadastrar uma chave já existente`(){
        Mockito.`when`(grpcClientRegistra.registra(
            ChavePixRequest.newBuilder()
                .setValorChave("14331247648")
                .setTipoConta(TipoConta.CONTA_CORRENTE)
                .setTipoChave(TipoChave.CPF)
                .setIdCliente(id)
                .build()))
            .thenThrow(Status.ALREADY_EXISTS
                .withDescription("Chave já existente")
                .asRuntimeException())

        val httpRequest = HttpRequest.POST("pix", NovaChaveRequest(
            id,
            TipoDeChave.CPF,
            "14331247648",
            TipoDeConta.CONTA_CORRENTE
        ))

        val error = assertThrows<HttpClientResponseException>{
            httpClient.toBlocking().exchange(httpRequest, String::class.java)
        }

        with(error){
            assertEquals(HttpResponse.unprocessableEntity<String>().code(), status.code)
        }
    }

    @Test
    fun `deve dar erro ao cadastrar uma chave com argumentos invalidos`(){
        Mockito.`when`(grpcClientRegistra.registra(
            ChavePixRequest.newBuilder()
                .setValorChave("14331247648")
                .setTipoConta(TipoConta.CONTA_CORRENTE)
                .setTipoChave(TipoChave.CPF)
                .setIdCliente(id)
                .build()))
            .thenThrow(Status.INVALID_ARGUMENT
                .withDescription("Argumento invalido")
                .asRuntimeException())

        val httpRequest = HttpRequest.POST("pix", NovaChaveRequest(
            id,
            TipoDeChave.CPF,
            "14331247648",
            TipoDeConta.CONTA_CORRENTE
        ))

        val error = assertThrows<HttpClientResponseException>{
            httpClient.toBlocking().exchange(httpRequest, String::class.java)
        }

        with(error){
            assertEquals(HttpResponse.badRequest("Argumento invalido").code(), status.code)
        }
    }

    @Test
    fun `deve excluir a chave pix`(){
        Mockito.`when`(grpcClientRemove.excluir(RemoverChavePixRequest.newBuilder()
            .setPixId("78394589634").setIsbp("60701190")
            .build()))
            .thenReturn(
                RemoverChavePixResponse
                .newBuilder().setMensagem("Chave removida com sucesso").build())

        val httpRequest = HttpRequest.DELETE("pix", RemoveChaveRequest(
            "78394589634",
            "60701190"
        ))
        val response = httpClient.toBlocking().exchange(httpRequest, HttpResponse::class.java)

        assertEquals(HttpStatus.OK, response.status)
    }

    @Test
    fun `deve listar uma chave específica`(){

        val filtroPorPixId = CarregaChavePixRequest.FiltroPorPixId.newBuilder()
            .setPixId("1").setClienteId(id).build()

        val contaInfo = CarregaChavePixResponse.ChavePix.ContaInfo.newBuilder()
            .setTipoConta(TipoConta.CONTA_CORRENTE)
            .setInstituicao("Itau")
            .setNomeDoTitular("Pedro")
            .setCpfDoTitular("78394589634")
            .setAgencia("213213")
            .setNumeroDaConta("1231321").build()
        val chavePix = CarregaChavePixResponse.ChavePix.newBuilder()
            .setConta(contaInfo)
            .setTipoChave(TipoChave.CPF)
            .setChave("78394589634").build()

        Mockito.`when`(grpcClientCarrega.carrega(CarregaChavePixRequest.newBuilder()
            .setPixId(filtroPorPixId)
            .build())).thenReturn(CarregaChavePixResponse.newBuilder()
            .setChave(chavePix)
            .setPixId("1")
            .setClienteId(id).build())

        val httpRequest = HttpRequest.GET<Any>("pix?clientId=$id&pixId=1")
        val response = httpClient.toBlocking().exchange(httpRequest, ListaChaveIdResponse::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertEquals("78394589634", response.body().chave.valorChave)

    }

    @Test
    fun `deve listar uma chave de cliente específico`(){
        val chaveCliente = ListaChaveClienteResponse.ChaveCliente.newBuilder()
            .setPixId("1")
            .setClientId(id)
            .setTipoChave(TipoChave.CPF)
            .setValorChave("78394589634")
            .setTipoConta(TipoConta.CONTA_CORRENTE).build()
        Mockito.`when`(grpcClientLista.lista(ListaChaveClienteRequest.newBuilder().setClientId(id).build()))
            .thenReturn(ListaChaveClienteResponse.newBuilder().addAllChaves(listOf(chaveCliente)).build())

        val httpRequest = HttpRequest.GET<Any>("pix/cliente/$id")
        val response = httpClient.toBlocking().exchange(httpRequest, Any::class.java)

        val cast = response.body() as ArrayList<LinkedHashMap<String, Any>>
        assertEquals(HttpStatus.OK, response.status)
        assertEquals("78394589634", cast[0]["valorChave"])
    }
    @Factory
    @Replaces(factory = KeyManagerFactory::class)
    internal class MockitoFactory {
        @Singleton
        fun stubMock() = Mockito.mock(ChavePixServiceRegistraGrpc.ChavePixServiceRegistraBlockingStub::class.java)

        @Singleton
        fun clientRemove() = Mockito.mock(ChavePixServiceRemoveGrpc.ChavePixServiceRemoveBlockingStub::class.java)

        @Singleton
        fun detalhaChave() = Mockito.mock(ChavePixServiceCarregaGrpc.ChavePixServiceCarregaBlockingStub::class.java)

        @Singleton
        fun detalhaChavePorCliente() = Mockito.mock(ChavePixServiceListaChaveClienteGrpc.ChavePixServiceListaChaveClienteBlockingStub::class.java)
    }
}