package API.petStore.stepDefinitions;

import API.petStore.interactions.checkHealth;
import API.petStore.interactions.sendOrder;
import API.petStore.questions.updatedInventory;
import API.petStore.tasks.deleteOrder;
import API.petStore.tasks.getInventory;
import API.petStore.tasks.createOrder;
import API.petStore.questions.verifyOrder;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;

import static io.restassured.RestAssured.baseURI;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.Matchers.equalTo;

public class stepsPetStoreOrders {
    private Actor actor;

    @Before
    public void setStage() {
        OnStage.setTheStage(new OnlineCast());
        baseURI = "http://localhost:8080/api/v3";
        actor = Actor.named("Pet store owner").whoCan(CallAnApi.at(baseURI));
    }

    @After("@CleanupOrders")
    public void cleanupOrders() {

        int orderId = actor.recall("orderId");

        actor.attemptsTo(
                deleteOrder.with()
                        .id(orderId)
                        .expectedStatusCode(200)
        );
    }

    @Given("that the pet store is open and ready to process orders")
    public void thatThePetStoreIsOpenAndReadyToProcessOrders() {
        actor.attemptsTo(
                checkHealth.petStore()
        );
    }

    @When("I place a new pet order with id {string} and status {string}")
    public void iPlaceANewPetOrderWithIdAndStatus(String id, String statusName) {
        int orderId = Integer.parseInt(id);

        actor.attemptsTo(
                createOrder.with()
                        .id(orderId)
                        .petID(12497)
                        .quantity(4)
                        .shipDate("2025-02-22T05:06:40.802Z")
                        .status(statusName)
                        .complete(true)
                        .expectedStatusCode(200)
        );
    }

    @Then("the order must be confirmed and saved in the system")
    public void theOrderMustBeConfirmedAndSavedInTheSystem() {

        int orderId = actor.recall("orderId");

        actor.asksFor(
                verifyOrder.forId(orderId,200)
        );
    }

    @Given("that I check the current status of the pet store inventory")
    public void thatICheckTheCurrentStatusOfThePetStoreInventory() {
        actor.attemptsTo(
                getInventory.petStore()
        );
    }

    @Then("the inventory count for status {string} must be updated accordingly")
    public void theInventoryCountForStatusMustBeUpdatedAccordingly(String statusName) {
        actor.asksFor(
                updatedInventory.forStatus(statusName)
        );
    }

    @And("I delete the order I just created")
    public void iDeleteTheOrderIJustCreated() {

        int orderId = actor.recall("orderId");

        actor.attemptsTo(
                deleteOrder.with()
                        .id(orderId)
                        .expectedStatusCode(200)
        );
    }
    @Then("the order must no longer exist in the system")
    public void theOrderMustNoLongerExistInTheSystem() {

        int orderId = actor.recall("orderId");

        actor.asksFor(
                verifyOrder.forId(orderId,404)
        );
    }

    @When("I attempt to place a new pet order with invalid data")
    public void iAttemptToPlaceANewPetOrderWithInvalidData() {
        actor.attemptsTo(
                sendOrder.invalidBody()
        );
    }

    @Then("the order must not be created")
    public void theOrderMustNotBeCreated() {

        int orderId = actor.recall("orderId");

        actor.asksFor(
                verifyOrder.forId(orderId,400)
        );
    }

    @When("I attempt to delete an non-existent order")
    public void iAttemptToDeleteAnNonExistentOrder() {
        actor.attemptsTo(
                deleteOrder.with()
                        .id(10)
                        .expectedStatusCode(404)
        );

    }

    @Then("the system must return that the order was not found")
    public void theSystemMustReturnThatTheOrderWasNotFound() {
        actor.should(
                seeThatResponse("Verification of a non-existent order",
                        response -> {
                    response.statusCode(404);
                    response.body(equalTo("Order not found"));}
                )
        );
    }
}
