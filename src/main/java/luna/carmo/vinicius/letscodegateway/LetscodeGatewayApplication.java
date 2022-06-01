package luna.carmo.vinicius.letscodegateway;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import java.time.Duration;


@SpringBootApplication
@EnableEurekaClient
public class LetscodeGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(LetscodeGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/api/sale/**")
                        .filters(f -> f.circuitBreaker(c -> c.setName("sale").setFallbackUri("/defaultFallback")))
                        .uri("lb://course-sales"))
                .route(p -> p
                        .path("/api/product/**")
                        .filters(f -> f.circuitBreaker(c -> c.setName("product-catalog").setFallbackUri("/defaultFallback")))
                        .uri("lb://product-catalog"))
                .route(p -> p
                        .path("/api/user/**")
                        .filters(f -> f.circuitBreaker(c -> c.setName("users-system").setFallbackUri("/defaultFallback")))
                        .uri("http://users-system:8080"))
                .route(p -> p
                        .path("/api/users")
                        .filters(f -> f.circuitBreaker(c -> c.setName("users-system").setFallbackUri("/defaultFallback")))
                        .uri("http://users-system:8080"))
                .build();
    }

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
            return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                    .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                    .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(10)).build())
                    .build());

    }

}
