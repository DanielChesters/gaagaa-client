package fr.oni.gaagaa.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Type;

public class DateTimeConverter implements JsonDeserializer<DateTime>, JsonSerializer<DateTime> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("E MMM d H:m:s Z YYYY");

    @Override
    public DateTime deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {
        return DATE_TIME_FORMATTER.parseDateTime(json.getAsString());
    }

    @Override
    public JsonElement serialize(DateTime src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString(DATE_TIME_FORMATTER));
    }
}
