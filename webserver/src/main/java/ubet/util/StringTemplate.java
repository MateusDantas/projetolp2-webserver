package ubet.util;

import java.util.HashMap;

public class StringTemplate {

	private String templateText;

	public StringTemplate(String text) {
		this.setTemplateText(text);
	}

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

	public String getTemplateText() {
		return templateText;
	}

	public void setTemplateText(String templateText) {
		this.templateText = templateText;
	}
}
