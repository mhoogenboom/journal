package com.robinfinch.journal.app.rest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.robinfinch.journal.app.persistence.DbHelper;

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

        String subClassName = DbHelper.NAMES_BY_CLASS.get(wrapper.entity.getClass());

        JsonElement entityJson = context.serialize(wrapper.entity);

        JsonObject wrapperJson = new JsonObject();
        wrapperJson.add(subClassName, entityJson);

        return wrapperJson;
    }
}