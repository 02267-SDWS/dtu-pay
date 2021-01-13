package interfaces.rest;

import domain.CustomerTokens;
import domain.Token;
import exceptions.CustomerAlreadyRegisteredException;
import exceptions.CustomerNotFoundException;
import exceptions.TokenNotFoundException;
import exceptions.TooManyTokensException;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import services.interfaces.ITokenService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Tag(ref = "TokenResource")
@Path("/token")
public class TokenResource {

    @Inject
    ITokenService service;

    @Tag(ref = "registerCustomer")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{customerId}")
    public Response registerCustomer(@PathParam("customerId") String customerId) {
        try {
            service.registerCustomer(customerId);
            return Response
                    .status(Response.Status.OK)
                    .build();
        } catch (CustomerAlreadyRegisteredException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @Tag(ref = "requestTokens")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response requestTokens(@QueryParam("customerId") String customerId,
                                  @QueryParam("amount") int amount) {
        try {
            service.requestTokens(customerId, amount);
            return Response
                    .status(Response.Status.OK)
                    .build();
        } catch (CustomerNotFoundException | TooManyTokensException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @Tag(ref = "getTokens")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{customerId}")
    public Response getTokens(@PathParam("customerId") String customerId) {
        try {
            Token token = service.getToken(customerId);
            return Response
                    .status(Response.Status.OK)
                    .entity(token)
                    .build();
        } catch (CustomerNotFoundException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @Tag(ref = "invalidateToken")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response invalidateToken(@QueryParam("customerId") String customerId,
                                    @QueryParam("tokenId") String tokenId) {
        try {
            service.invalidateToken(tokenId);
            return Response
                    .status(Response.Status.OK)
                    .build();
        } catch (CustomerNotFoundException | TokenNotFoundException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

}