package de.ngloader.leben.core.synced.translation.args;

import de.ngloader.leben.core.synced.translation.Language;

public class LanguageArgumentDate extends LanguageArgument {

	public LanguageArgumentDate(Object value) {
		super(value);
	}

	@Override
	public String format(Language language) {
		return language.dateFormat.format(value);
	}
}