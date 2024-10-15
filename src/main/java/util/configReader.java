package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class configReader {	//can be totalUtilClass with other methods like validation

	public static Properties prop; // need this to read the property file

	public static String getProp(String key) {
		prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream("src\\main\\java\\util\\config.properties");
											//points at which file path to read
			prop.load(fis);					//loads the fis path
		} catch (FileNotFoundException e) {	//do this one first to know if fis was wrong
			e.printStackTrace();
		} catch (IOException e) {			//if this goes off then prop.load(fis) prolly errored
			e.printStackTrace();
		}

		return prop.getProperty(key);		//this returns the value of the given key in the prop file
	}
}
