package API.petStore.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.rest.interactions.Get;

import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.Matchers.equalTo;

public class checkHealth implements Interaction {

    public static checkHealth petStore() {
        return Tasks.instrumented(checkHealth.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Get.resource("/openapi.json")
        );
        actor.should(
                seeThatResponse("Pet store services version 3.0.2 are available",
                        response -> {
                            response.statusCode(200);
                            response.body("openapi", equalTo("3.0.2"));
                        })
        );
    }
}
