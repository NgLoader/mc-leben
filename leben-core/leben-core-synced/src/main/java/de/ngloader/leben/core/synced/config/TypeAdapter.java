package de.ngloader.leben.core.synced.config;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

public interface TypeAdapter <T> extends JsonSerializer<T>, JsonDeserializer<T> {

	public Class<T> getType();
}