package API.petStore.tasks;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;

public class getInventory implements Task {

    public static getInventory petStore() {
        return instrumented(getInventory.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Get.resource("/store/inventory")
        );
        actor.should(
                seeThatResponse("Pet store inventory is available",
                        response -> {
                            response.statusCode(200);
                        })
        );

        int approved = SerenityRest.lastResponse().jsonPath().getInt("approved");
        int placed = SerenityRest.lastResponse().jsonPath().getInt("placed");
        int delivered = SerenityRest.lastResponse().jsonPath().getInt("delivered");

        actor.remember("approved", approved);
        actor.remember("placed", placed);
        actor.remember("delivered", delivered);
    }
}
