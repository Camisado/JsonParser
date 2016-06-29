package jsonEntity;

import java.util.HashMap;

public class JsonObject extends JsonValue<JsonValue> {

	private HashMap<String, JsonValue> properties = new HashMap<String, JsonValue>();

	public HashMap<String, JsonValue> getProperties() {
		return properties;
	}
}
