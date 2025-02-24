package API.petStore.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.rest.interactions.Delete;

import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;

public class deleteOrder implements Task {

    private int id;
    private int statusCode;

    public static deleteOrder with() {
        return Tasks.instrumented(deleteOrder.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Delete.from("/store/order/{orderId}")
                        .with(request -> request.pathParam("orderId", id))
        );
        actor.should(
                seeThatResponse("The order deletion service responds properly",
                        response -> response.statusCode(statusCode)
                )
        );
    }

    public deleteOrder id(int id) {
        this.id = id;
        return this;
    }

    public deleteOrder expectedStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

}
