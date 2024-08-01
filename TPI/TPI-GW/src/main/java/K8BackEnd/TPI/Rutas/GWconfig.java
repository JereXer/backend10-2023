package K8BackEnd.TPI.Rutas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtGrantedAuthoritiesConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebFluxSecurity
public class GWconfig {

    @Bean
    public RouteLocator configurarRutas(RouteLocatorBuilder builder){

        return builder.routes()
                // Ruteo al Microservicio de Personas
                .route(p -> p.path("/api/alquileres/**").uri("http://localhost:8081"))
                .route(p -> p.path("/api/estaciones/**").uri("http://localhost:8082"))
                .build();
     }

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) throws Exception {
        //http.authorizeExchange(exchanges -> exchanges
        http.authorizeExchange(exchanges -> exchanges
                        // Esta ruta puede ser accedida por cualquiera, sin autorización
                                .pathMatchers("/api/estaciones/**")
                                .hasRole("CLIENTE")
                                .pathMatchers("/api/alquileres/**")
                                .hasRole("ADMINISTRADOR")
                                // Cualquier otra petición...
                                .anyExchange()
                                .authenticated()
                ).oauth2ResourceServer(oauth ->
                    oauth.jwt(Customizer.withDefaults()))
                .csrf(csrf -> csrf.disable());
        return http.build();
    }
//
//
    @Bean
    public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
        var jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverter();
        var grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        // Se especifica el nombre del claim a analizar
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        // Se agrega este prefijo en la conversión por una convención de Spring
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");


        // Se asocia el conversor de Authorities al Bean que convierte el token JWT a un objeto Authorization
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new ReactiveJwtGrantedAuthoritiesConverterAdapter(grantedAuthoritiesConverter));
        // También se puede cambiar el claim que corresponde al nombre que luego se utilizará en el objeto
        // Authorization
        // jwtAuthenticationConverter.setPrincipalClaimName("user_name");
        return jwtAuthenticationConverter;
    }
//
}
