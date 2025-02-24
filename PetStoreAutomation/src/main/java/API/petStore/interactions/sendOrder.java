package API.petStore.interactions;

import API.petStore.utils.obtainIdFromJson;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class sendOrder implements Interaction {
    
    public static sendOrder invalidBody() {
        return Tasks.instrumented(sendOrder.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> orders;

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("dataJSON/invalidOrder.json");
            if (inputStream == null) {
                throw new RuntimeException("Could not find JSON file in path: dataJSON/invalidOrder.json");
            }

            orders = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<Map<String, Object>>>() {}
            );

            Random random = new Random();
            Map<String, Object> randomBody = orders.get(random.nextInt(orders.size()));

            int selectedId = obtainIdFromJson.obtainId(randomBody);

            actor.attemptsTo(
                    Post.to("/store/order")
                            .with(request -> request.header("Content-Type", "application/json")
                                    .body(randomBody)
                            )
            );
            actor.remember("orderId", selectedId);
        } catch (Exception e) {
            throw new RuntimeException("Error reading JSON file or sending request", e);
        }
    }
}