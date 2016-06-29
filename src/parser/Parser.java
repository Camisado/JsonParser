package parser;

import jsonEntity.JsonObject;
import jsonEntity.JsonValue;

import java.util.*;

public class Parser {

	private String source;
	private String[] data;
	private int index = 0;
	private String current;
	private String output = "";

	public Parser(String source) {
		this.source = source;
		data = this.source.trim().split("");
		data = removeNotJsonSymbols();
	}

	public String parse () {
		current = data[index];

		switch (current) {
			case Token.START_OBJECT : {
				readObject();
				break;
			}
			case Token.START_ARRAY : {
				readArray();
				break;
			}
			default: {
				System.out.println("unexpected symbol");
				break;
			}
		}

		System.out.println(Arrays.toString(data));

		return output;
	}

	private String[] removeNotJsonSymbols () {
		List<String> list = new ArrayList<String>(Arrays.asList(data));
		List<String> notJsonSymbols = new ArrayList<String>(Arrays.asList(new String[]{"\r", "\n", "\t", " "}));

		for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
			String string = iterator.next();
			if (notJsonSymbols.indexOf(string) != -1) {
				iterator.remove();
			}
		}

		String[] result = new String[list.size()];
		list.toArray(result);

		return result;
	}

	private void increase () {
		index++;
		current = data[index];
	}

	private JsonObject readObject () {
		JsonObject object = new JsonObject();
		increase();
		if (current.equals(Token.QUOTES)) {
			object.setKey(readKey());
		} else {
			System.out.println("unexpected symbol");
		}

	}

	private void readArray() {
		output += "start array";
	}

	private String readKey () {
		String key = "";
		while (Character.isLetterOrDigit(current.charAt(0))) {
			key += current;
			increase();
		}
		return key;
	}

	private JsonValue readValue () {
		JsonValue value;

		switch (current) {
			case Token.QUOTES : {
				break;
			}
		}

		return value;
	}
}
