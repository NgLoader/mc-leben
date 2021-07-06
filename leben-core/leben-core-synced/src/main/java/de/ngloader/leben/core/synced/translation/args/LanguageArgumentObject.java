package de.ngloader.leben.core.synced.translation.args;

import de.ngloader.leben.core.synced.translation.Language;

public class LanguageArgumentObject extends LanguageArgument {

	public LanguageArgumentObject(Object value) {
		super(value);
	}

	@Override
	public String format(Language language) {
		return this.value.toString();
	}
}