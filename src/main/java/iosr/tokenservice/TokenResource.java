package iosr.tokenservice;

import iosr.tokenservice.access.TokenProvider;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/token")
@Produces(MediaType.APPLICATION_JSON)
public class TokenResource {
    private final TokenProvider provider;

    public TokenResource(TokenProvider provider) {
        this.provider = provider;
    }

    @GET
    public Token getToken() {
        return provider.getToken();
    }
}
