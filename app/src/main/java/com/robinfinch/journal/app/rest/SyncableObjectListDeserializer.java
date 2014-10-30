package com.robinfinch.journal.app.rest;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.robinfinch.journal.app.persistence.DbHelper;
import com.robinfinch.journal.domain.SyncableObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Deserializes a polymorphic list.
 *
 * @author Mark Hoogenboom
 */
public class SyncableObjectListDeserializer implements JsonDeserializer<List<SyncableObject>> {

    public List<SyncableObject> deserialize(JsonElement entitiesJson, Type type,
                                            JsonDeserializationContext context) throws JsonParseException {

        List<SyncableObject> entities = new ArrayList<>();

        JsonArray entitiesJsonArray = entitiesJson.getAsJsonArray();

        for (JsonElement entityJson : entitiesJsonArray) {
            JsonObject entityJsonObject = entityJson.getAsJsonObject();

            for (String subClassName : DbHelper.CLASSES_BY_NAME.keySet()) {
                if (entityJsonObject.has(subClassName)) {
                    SyncableObject entity = context.deserialize(entityJsonObject.get(subClassName), DbHelper.CLASSES_BY_NAME.get(subClassName));
                    entities.add(entity);
                }
            }
        }

        return entities;
    }
}