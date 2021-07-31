package de.ngloader.leben.core.synced.translation;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import de.ngloader.leben.core.synced.util.ReflectionUtil;
import io.sentry.Sentry;
import io.sentry.SentryLevel;

public class TranslationHandler {

	private final Map<Locale, Language> languages = new ConcurrentHashMap<>();

	private Language fallback = null;

	public TranslationHandler() {
		Set<ResourceBundle> bundles = new HashSet<>();
		Set<Locale> availableLocales = Set.of(Locale.getAvailableLocales());

		try {
			Files.walkFileTree(Path.of(TranslationHandler.class.getClassLoader().getResource("lang/").toURI()), new SimpleFileVisitor<Path>() {
				
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					try (BufferedReader bufferedReader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
						ResourceBundle resourceBundle = new PropertyResourceBundle(bufferedReader);

						if (!resourceBundle.containsKey("locale")) {
							Sentry.captureMessage("Translation file \"" + file.toString() + "\" not contains locale value!", SentryLevel.WARNING);
							return FileVisitResult.CONTINUE;
						}

						Locale locale = Locale.forLanguageTag(resourceBundle.getString("locale"));
						if (!availableLocales.contains(locale)) {
							Sentry.captureMessage("Translation file \"" + file.toString() + "\" has a invalid locale value!", SentryLevel.WARNING);
							return FileVisitResult.CONTINUE;
						}

						ReflectionUtil.setField(resourceBundle, "locale", locale);

						bundles.add(resourceBundle);

						if (TranslationHandler.this.fallback == null && resourceBundle.containsKey("fallbackLanguage")) {
							TranslationHandler.this.fallback = new Language(resourceBundle, null);
						}
					}
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		for (ResourceBundle resourceBundle : bundles) {
			Language language;

			if (this.fallback != null && this.fallback.resourceBundle.getLocale().equals(resourceBundle.getLocale())) {
				language = this.fallback;
			} else {
				language = new Language(resourceBundle, this.fallback);
			}

			this.languages.put(resourceBundle.getLocale(), language);
		}

		if (this.fallback == null && !this.languages.isEmpty()) {
			this.fallback = languages.values().toArray(Language[]::new)[0];
		}
	}

	public Language getLanguage(Locale locale) {
		return this.languages.getOrDefault(locale, this.fallback);
	}
}