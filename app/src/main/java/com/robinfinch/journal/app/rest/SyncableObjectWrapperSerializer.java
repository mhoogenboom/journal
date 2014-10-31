package com.robinfinch.journal.app.rest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Serializes an object for use in a polymorphic list.
 *
 * @author Mark Hoogenboom
 */
public class SyncableObjectWrapperSerializer implements JsonSerializer<SyncableObjectWrapper> {

    @Override
    public JsonElement serialize(SyncableObjectWrapper wrapper, Type type,
                                 JsonSerializationContext context) {

        String clsName = "." + wrapper.entity.getClass().getSimpleName();

        JsonElement entityJson = context.serialize(wrapper.entity);

        JsonObject wrapperJson = new JsonObject();
        wrapperJson.add(clsName, entityJson);

        return wrapperJson;
    }
}