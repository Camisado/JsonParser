package parser;

import jsonEntity.*;

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

		try {
			switch (symbol.getCurrent()) {
				case Token.START_OBJECT : {
					object = readObject();
					if (!symbol.getCurrent().equals(Token.END_OBJECT)) {
						throw new Exception("unexpected symbol: " + symbol.getCurrent());
					}
					symbol.increase();
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
		} catch (Exception e) {
			e.printStackTrace();
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

	private JsonObject readObject () throws Exception {
		symbol.increase();
		JsonObject object = new JsonObject();
		addProperty(object);

		return object;
	}

	private void addProperty (JsonObject object) throws Exception {
		String key = readKey();
		JsonValue value = readValue();
		object.getProperties().put(key, value);
		if (symbol.getCurrent().equals(Token.COMMA)) {
			symbol.increase();
			addProperty(object);
		}
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
				symbol.increase();
				value = new JsonString(str);
				break;
			}
			case Token.START_OBJECT : {
				value = readObject();
				if (!symbol.getCurrent().equals(Token.END_OBJECT)) {
					throw new Exception("unexpected symbol: " + symbol.getCurrent());
				}
				symbol.increase();
				break;
			}
			case Token.START_ARRAY : {
				value = new JsonArray();
				break;
			}
			default : {
				if (isNumeric(symbol.getCurrent())) {
					String numberStr = readNumber();
					if (isNumeric(numberStr)) {
						value = new JsonNumber(Double.parseDouble(numberStr));
					} else {
						throw new Exception("too many '" + Token.DOT + "' in number");
					}
				}
				break;
			}
		}

		return value;
	}

	private boolean isNumeric (String symbol) {
		try {
			Number number = Double.parseDouble(symbol);
		} catch(NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	private String readNumber () throws Exception {
		String numberStr = "";

		if (isNumeric(symbol.getCurrent()) || symbol.getCurrent().equals(Token.DOT)) {
			numberStr += symbol.getCurrent();
			symbol.increase();
			numberStr += readNumber();
		} else if (symbol.getCurrent().equals(Token.COMMA) || symbol.getCurrent().equals(Token.END_OBJECT)) {
			return numberStr;
		} else {
			throw new Exception("unexpected symbol: " + symbol.getCurrent());
		}

		return numberStr;
	}

	private String readString (String endToken) throws Exception {
		String token = "";

		if (Character.isLetterOrDigit(symbol.getCurrent().charAt(0))) {
			token += symbol.getCurrent();
			symbol.increase();
			token += readString(endToken);
		}else if(!symbol.getCurrent().equals(endToken)) {
			throw new Exception("unexpected symbol: " + symbol.getCurrent());
		}

		return token;
	}
}
