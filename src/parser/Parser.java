package parser;

import jsonEntity.JsonArray;
import jsonEntity.JsonObject;
import jsonEntity.JsonString;
import jsonEntity.JsonValue;

import java.util.*;

public class Parser {

	private String source;
	private Symbol symbol;

	public Parser(String source) {
		this.source = source;
		String[] data = removeNotJsonSymbols(this.source.trim().split(""));
		symbol = new Symbol(data);
	}

	public JsonObject parse () {
		JsonObject object = null;

		switch (symbol.getCurrent()) {
			case Token.START_OBJECT : {
				object = readObject();
				break;
			}
			case Token.START_ARRAY : {
				symbol.increase();
				readArray();
				break;
			}
			default: {
				System.out.println("unexpected symbol");
				break;
			}
		}

		System.out.println(Arrays.toString(symbol.getData()));

		return object;
	}

	private String[] removeNotJsonSymbols (String[] data) {
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

	private JsonObject readObject () {
		symbol.increase();
		JsonObject object = new JsonObject();
		try {
			object.setKey(readKey());
			object.setValue(readValue());
		} catch (Exception e) {
			e.printStackTrace();
		}


		return object;
	}

	private String readKey() throws Exception {
		String key = null;

		if (symbol.getCurrent().equals(Token.QUOTES)) {
			symbol.increase();
			key = readString(Token.QUOTES);
			symbol.increase();
		} else {
			throw new Exception("unexpected symbol: " + symbol.getCurrent());
		}

		checkColon();

		return key;
	}

	private void checkColon () throws Exception {
		if (symbol.getCurrent().equals(Token.COLON)) {
			symbol.increase();
		} else {
			throw new Exception("unexpected symbol: " + symbol.getCurrent());
		}
	}

	private void readArray() {}

	private JsonValue readValue () throws Exception {
		JsonValue value = null;

		switch (symbol.getCurrent()) {
			case Token.QUOTES : {
				symbol.increase();
				String str = readString(Token.QUOTES);
				value = new JsonString(str);
				break;
			}
			case Token.START_OBJECT : {
				value = new JsonObject();
				break;
			}
			case Token.START_ARRAY : {
				value = new JsonArray();
				break;
			}
			default : {
				break;
			}
		}

		return value;
	}

	private String readString (String endToken) throws Exception {
		String token = "";

		if (Character.isLetterOrDigit(symbol.getCurrent().charAt(0)) && !symbol.getCurrent().equals(endToken)) {
			token += symbol.getCurrent();
			symbol.increase();
			token += readString(endToken);
		}else if(!Character.isLetterOrDigit(symbol.getCurrent().charAt(0)) && !symbol.getCurrent().equals(endToken)) {
			throw new Exception("unexpected symbol: " + symbol.getCurrent());
		}

		return token;
	}
}
