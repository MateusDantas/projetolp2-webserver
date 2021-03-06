package ubet.util;

import java.util.regex.*;

/**
 * @author mdantas
 * @version $Revision: 1.0 $
 */
public class EmailValidator {

	private Pattern pattern;
	private Matcher matcher;

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	public EmailValidator() {
		
		pattern = Pattern.compile(EMAIL_PATTERN);
	}
	
	/**
	 * Method isEmailValid.
	 * @param hex String
	
	 * @return boolean */
	public boolean isEmailValid(final String hex) {
		
		matcher = pattern.matcher(hex);
		return matcher.matches();
	}
}