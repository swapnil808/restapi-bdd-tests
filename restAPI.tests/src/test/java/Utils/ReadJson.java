package Utils;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ReadJson {
	
	public static String readJson(String filePath, String field) {
		FileReader reader;
		String value = null;
		try {
			reader = new FileReader(filePath);
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
			value = jsonObject.get(field).toString();
			reader.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return value;
	}

}
