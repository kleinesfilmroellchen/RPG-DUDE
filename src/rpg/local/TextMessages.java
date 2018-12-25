package rpg.local;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Auto-generated Text-requester. Allows quick change of messages and
 * translations.
 * @author kleinesfilmröllchen
 * @since 0.0.0009
 * @version 0.0.0009
 */
public class TextMessages {
	private static final String BUNDLE_NAME = "rpg.local.text"; //$NON-NLS-1$

	private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, Locale.GERMAN);

	private TextMessages() {
	}

	/**
	 * Gets a certain description text.
	 */
	public static String _t(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return "!---MISSING please contact developers---" + key + '!';
		}
	}
	
	public static void setLang(Locale locale) {
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, locale);
	}
}
