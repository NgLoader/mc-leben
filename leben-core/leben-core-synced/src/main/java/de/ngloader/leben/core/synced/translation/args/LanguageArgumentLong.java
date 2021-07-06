package de.ngloader.leben.core.synced.translation.args;

import de.ngloader.leben.core.synced.translation.Language;

public class LanguageArgumentLong extends LanguageArgument {

	public LanguageArgumentLong(Object value) {
		super(value);
	}

	@Override
	public String format(Language language) {
		return language.numberFormat.format(this.value);
	}
}