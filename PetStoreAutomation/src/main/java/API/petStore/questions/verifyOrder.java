package API.petStore.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.rest.interactions.Get;

import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.Matchers.equalTo;

public class verifyOrder implements Question<Boolean> {

    private final int orderId;
    private final int expectedStatusCode;

    public verifyOrder(int orderId, int expectedStatusCode) {
        this.orderId = orderId;
        this.expectedStatusCode = expectedStatusCode;
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        actor.attemptsTo(
                Get.resource("/store/order/{orderId}")
                        .with(request -> request.pathParam("orderId", orderId))
        );

        actor.should(
                seeThatResponse("Verification of order existence",
                        response -> {
                            response.statusCode(expectedStatusCode);
                            if (expectedStatusCode == 200) {
                                response.body("id", equalTo(orderId));
                            } else if (expectedStatusCode == 404) {
                                response.body(equalTo("Order not found"));
                            }
                        }
                )
        );

        return true;
    }

    public static verifyOrder forId(int orderId, int expectedStatusCode) {
        return new verifyOrder(orderId, expectedStatusCode);
    }
}
