# Kotlin WebClient CircuitBreaker

Look in the `RestClient` class. Note the fallback return type cannot be determined.

Comment out the fallback lambda like below and it seems fine, however you lose the functionality of the circuit breaker pattern.

```kotlin
fun call(): String {

    val responseMono =
        this.webClient.get()
            .uri( "http://example.com" )
            .retrieve()
                .bodyToMono(SomeResponse::class.java)
                .transform {
                    this.reactiveCircuitBreakerFactory.create("example-api")
                        .run {
                                it //,
//                                throwable -> Mono.just( SomeResponse( -1, "error messageL $throwable.localizedMessage") )
                        }
                }
    val response = responseMono.block()

    return response.someMessage
}
```