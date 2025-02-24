package API.petStore.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.rest.interactions.Get;

public class updatedInventory implements Question<Boolean> {

    private final String statusName;

    public updatedInventory(String statusName) {
        this.statusName = statusName;
    }

    public static updatedInventory forStatus(String statusName) {
        return new updatedInventory(statusName);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        actor.attemptsTo(
                Get.resource("/store/inventory")
        );

        int updatedValue = SerenityRest.lastResponse().jsonPath().getInt(statusName);

        int previousValue = actor.recall(statusName);
        int quantity = actor.recall("quantity");

        return updatedValue == previousValue + quantity;
    }
}
