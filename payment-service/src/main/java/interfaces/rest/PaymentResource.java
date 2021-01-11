package interfaces.rest;

import exceptions.TransactionException;
import exceptions.customer.CustomerException;
import exceptions.customer.CustomerNotFoundException;
import exceptions.merchant.MerchantException;
import exceptions.merchant.MerchantNotFoundException;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.tags.Tags;
import services.interfaces.IPaymentService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Troels (s161791)
 * UserNotFoundException to use when a user cannot be found.
 */

@Tag(ref = "Payment")
@Path("/payment")
public class PaymentResource {

    @Inject
    IPaymentService ps;

    /**
     * Pay x amount to a merchant
     * @param cid - Customer id.
     * @param mid - Merchant id.
     * @Param amount - amount of money to be payed.
     */

    @Operation(summary = "Pay x amount to a merchant")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response pay(@QueryParam("cid") String cid,
                        @QueryParam("mid") String mid,
                        @QueryParam("amount") int amount) {
        try{
            ps.createTransaction(cid, mid, amount);
            return Response
                    .created(new URI(""))
                    .build();
        }catch (CustomerException | MerchantException e){
            throw new NotFoundException(e.getMessage());
        }catch (URISyntaxException e){
            throw new BadRequestException(e.getMessage());
        }catch (TransactionException e){
            throw new BadRequestException(e.getMessage());
        }
    }

    /**
     * Refund amount to customer
     * @param cid - Customer id.
     * @param mid - Merchant id.
     * @Param amount - amount of money to be refunded.
     */

    @Operation(summary = "Refund amount to customer")
    @POST
    @Path("refund")
    @Produces(MediaType.APPLICATION_JSON)
    public Response refund(@QueryParam("cid") String cid,
                        @QueryParam("mid") String mid,
                        @QueryParam("amount") int amount) {
        try{
            ps.createTransaction(cid, mid, amount);
            return Response
                    .created(new URI(""))
                    .build();
        }catch (CustomerException | MerchantException e){
            throw new NotFoundException(e.getMessage());
        }catch (URISyntaxException e){
            throw new BadRequestException(e.getMessage());
        }catch (TransactionException e){
            throw new BadRequestException(e.getMessage());
        }
    }

}