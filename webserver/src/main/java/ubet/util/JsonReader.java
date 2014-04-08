package ubet.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONObject;

/**
 * @author mdantas
 * @version $Revision: 1.0 $
 */
public class JsonReader {

	/**
	 * Method readAll.
	 * @param rd Reader
	
	
	 * @return String * @throws IOException */
	private static String readAll(Reader rd) throws IOException {

		StringBuilder stringBuilder = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			stringBuilder.append((char) cp);
		}
		return stringBuilder.toString();
	}

	/**
	 * Method fix.
	 * @param str String
	
	 * @return String */
	private static String fix(String str) {
		
		String nowStr = "{" + String.valueOf('"');
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '(')
				nowStr += '"' +  " :";
			else if (!(str.charAt(i) == ')'))
				nowStr += str.charAt(i);
			else
				nowStr += "}";
		}
		return nowStr;
	}
	/**
	 * Method getJson.
	 * @param url String
	
	
	
	 * @return JSONObject * @throws MalformedURLException * @throws IOException */
	public static JSONObject getJson(String url) throws MalformedURLException,
			IOException {

		InputStream inputStream = new URL(url).openStream();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String jsonText = fix(readAll(reader));
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			inputStream.close();
		}
	}
}
