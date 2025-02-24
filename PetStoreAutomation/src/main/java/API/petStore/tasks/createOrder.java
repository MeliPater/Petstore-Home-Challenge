package API.petStore.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.util.HashMap;
import java.util.Map;

import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;

public class createOrder implements Task {

    private int id;
    private int petID;
    private int quantity;
    private String shipDate;
    private String status;
    private boolean complete;
    private int statusCode;

    public static createOrder with() {
        return Tasks.instrumented(createOrder.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {

        Map<String, Object> body = new HashMap<>();
        body.put("id", id);
        body.put("petId", petID);
        body.put("quantity", quantity);
        body.put("shipDate", shipDate);
        body.put("status", status);
        body.put("complete", complete);

        actor.attemptsTo(
                Post.to("/store/order"
                        )
                .with(request -> request.header("Content-Type", "application/json")
                        .body(body)
                )
        );
        actor.should(
                seeThatResponse("The order creation service responds properly",
                        response -> response.statusCode(statusCode)
                )
        );
        actor.remember("orderId", id);
        actor.remember("quantity", quantity);
    }

    public createOrder id(int id) {
        this.id = id;
        return this;
    }

    public createOrder petID(int petID) {
        this.petID = petID;
        return this;
    }

    public createOrder quantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public createOrder shipDate(String shipDate) {
        this.shipDate = shipDate;
        return this;
    }

    public createOrder status(String status) {
        this.status = status;
        return this;
    }

    public createOrder complete(boolean complete) {
        this.complete = complete;
        return this;
    }

    public createOrder expectedStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }
}
