package de.ngloader.leben.core.synced.translation.args;

import java.text.MessageFormat;

import de.ngloader.leben.core.synced.translation.Language;

public class LanguageArgumentPercent extends LanguageArgument {

	public LanguageArgumentPercent(Object value) {
		super(value);
	}

	@Override
	public String format(Language language) {
		return MessageFormat.format(language.percentFormat, this.value);
	}
}