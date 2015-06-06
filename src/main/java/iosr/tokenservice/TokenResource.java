package iosr.tokenservice;

import iosr.tokenservice.access.TokenProvider;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Map;
import java.util.Optional;

@Path("/token")
@Produces(MediaType.APPLICATION_JSON)
public class TokenResource {
    private final Map<String, TokenProvider> providers;

    public TokenResource(Map<String, TokenProvider> providers) {
        this.providers = providers;
    }

    @GET
    public Token getToken(@QueryParam("cloud") String cloud) {
        Optional<TokenProvider> tokenProvider = Optional.of(providers.get(cloud));

        if (tokenProvider.isPresent()) {
            return tokenProvider.get().getToken();
        }

        throw new IllegalArgumentException("cloud");
    }
}
