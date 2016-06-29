package parser;

public class Symbol {

	private String[] data;
	private int index = 0;
	private String current;
	private String prev;
	private String next;

	public Symbol(String[] data) {
		this.data = data;
		current = this.data[index];
	}

	public void increase () {
		index++;
		prev = index - 1 < 0 ? "" : data[index - 1];
		current = index == data.length ? "" : data[index];
		next = index + 1 > data.length - 1 ? "" : data[index + 1];
	}

	public void increase (int count) {
		index += count;
		prev = data[index-1];
		current = data[index];
		next = data[index+1];
	}

	public String getCurrent() {
		return current;
	}

	public String getPrev() {
		return prev;
	}

	public String getNext() {
		return next;
	}

	public String[] getData() {
		return data;
	}

	public int getIndex() {
		return index;
	}
}
