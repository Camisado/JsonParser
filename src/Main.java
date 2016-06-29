import parser.Parser;

import java.io.*;

public class Main {

	public static void main(String args[]) {
		try {
			File file = new File("input.json");
			FileInputStream fis = new FileInputStream(file);
			byte[] data = new byte[(int) file.length()];
			fis.read(data);
			fis.close();

			String source = new String(data, "UTF-8");
			Parser parser = new Parser(source);
			System.out.println(parser.parse());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
