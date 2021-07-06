package de.ngloader.leben.core.synced.translation;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ResourceBundle;

import de.ngloader.leben.core.synced.translation.args.LanguageArgument;
import io.sentry.Sentry;
import io.sentry.SentryLevel;

public class Language {

	protected final ResourceBundle resourceBundle;
	protected final Language fallback;

	public final DateFormat dateFormat;
	public final NumberFormat numberFormat;
	public final String percentFormat;

	public Language(ResourceBundle resourceBundle, Language fallback) {
		this.resourceBundle = resourceBundle;
		this.fallback = fallback;

		this.dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, this.resourceBundle.getLocale());
		this.numberFormat = NumberFormat.getNumberInstance(this.resourceBundle.getLocale());
		this.percentFormat = "{0,number,#.##%}";
	}

	public String format(String key, LanguageArgument... args) {
		if (this.resourceBundle.containsKey(key)) {
			if (this.fallback != null) {
				return this.fallback.format(key, args);
			}

			Sentry.captureMessage(String.format("Translation key \"%s\" was not found in \"%s\".", key, this.resourceBundle.getLocale().toString()), SentryLevel.WARNING);
			return key;
		}

		char[] array = this.resourceBundle.getString(key).toCharArray();
		StringBuilder builder = new StringBuilder();

		int step = 0;
		for (int i = 0; i < array.length; i++) {
			char letter = array[i];

			if (array[i] == '{' && array.length > i + 1 && array[i + 1] == '}') {
				i++;

				if (args.length > step) {
					builder.append(args[step++].format(this));
				}
				continue;
			}

			builder.append(letter);
		}

		return builder.toString();
	}
}