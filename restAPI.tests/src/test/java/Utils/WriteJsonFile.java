package Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import groovy.json.JsonParser;

public class WriteJsonFile {
	
	
	public static void updateJson(String filePath, String field, String value) {
		
		FileReader reader = null;
		try {
			reader = new FileReader(filePath);
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
			jsonObject.put(field, value);
			BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
			writer.write(jsonObject.toString());
			writer.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void writeToJsonFile(String filePath, JSONObject jsonObj) {
		
		FileReader reader = null;
		try {
	        File file=new File(filePath);
	        
	        FileWriter fileWriter = new FileWriter(file);
	        System.out.println("Writing JSON object to file");
	        fileWriter.write(jsonObj.toJSONString());
	        fileWriter.flush();
	        fileWriter.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
