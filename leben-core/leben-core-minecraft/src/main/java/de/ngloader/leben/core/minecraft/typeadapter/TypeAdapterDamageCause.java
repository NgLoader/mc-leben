package de.ngloader.leben.core.minecraft.typeadapter;

import java.lang.reflect.Type;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import de.ngloader.leben.core.synced.config.TypeAdapter;

public class TypeAdapterDamageCause implements TypeAdapter<DamageCause> {

	@Override
	public JsonElement serialize(DamageCause src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(src.name());
	}

	@Override
	public DamageCause deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		return DamageCause.valueOf(json.getAsString());
	}

	@Override
	public Class<DamageCause> getType() {
		return DamageCause.class;
	}
}