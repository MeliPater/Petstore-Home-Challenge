package API.petStore.utils;

import java.util.Map;

public class obtainIdFromJson {

    public static int obtainId(Map<String,Object> body) {
        int selectedId = 0;

        if (body.containsKey("id")) {
            Object idValue = body.get("id");
            if (idValue instanceof Number) {
                selectedId = ((Number) idValue).intValue();
            } else if (idValue instanceof String) {
                try {
                    selectedId = Integer.parseInt((String) idValue);
                } catch (NumberFormatException ignored) {
                }
            }
        }

        return selectedId;
    }
}
