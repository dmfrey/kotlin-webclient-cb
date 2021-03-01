package com.example.kotlinwebclientcb

import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class RestClient(
    val webClient: WebClient,
    val reactiveCircuitBreakerFactory: ReactiveCircuitBreakerFactory<*,*>
) {

    fun call(): String {

        val responseMono =
            this.webClient.get()
                .uri( "http://example.com" )
                .retrieve()
                    .bodyToMono(SomeResponse::class.java)
                    .transform {
                        this.reactiveCircuitBreakerFactory.create("example-api")
                            .run {
                                    it,
                                    throwable -> Mono.just( SomeResponse( -1, "error messageL $throwable.localizedMessage") )
                            }
                    }
        val response = responseMono.block()

        return response.someMessage
    }

}