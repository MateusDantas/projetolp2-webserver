package ubet.util;

import java.util.HashMap;

/**
 * @author mdantas
 * @version $Revision: 1.0 $
 */
public class StringTemplate {

	private String templateText;

	/**
	 * Constructor for StringTemplate.
	 * @param text String
	 */
	public StringTemplate(String text) {
		this.setTemplateText(text);
	}

	/**
	 * Method getString.
	 * @param values HashMap<String,Object>
	
	
	 * @return String * @throws Exception */
	public String getString(HashMap<String, Object> values) throws Exception {

		String templateResult = this.templateText;

		try {
			for (String key : values.keySet()) {
				int pos = templateResult.indexOf("$(" + key + ")");
				if (pos == -1)
					throw new Exception("Unmatched template");

				String originalTemplate = templateResult;
				templateResult = originalTemplate.substring(0, pos);
				templateResult += String.valueOf(values.get(key));
				templateResult += originalTemplate.substring(pos
						+ ((String) ("$(" + key + ")")).length());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return templateResult;
	}

	/**
	 * Method getTemplateText.
	
	 * @return String */
	public String getTemplateText() {
		return templateText;
	}

	/**
	 * Method setTemplateText.
	 * @param templateText String
	 */
	public void setTemplateText(String templateText) {
		this.templateText = templateText;
	}
}
