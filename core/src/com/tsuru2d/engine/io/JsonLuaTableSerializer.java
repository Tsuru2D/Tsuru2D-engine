package com.tsuru2d.engine.io;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.tsuru2d.engine.lua.LuaArrayIterator;
import com.tsuru2d.engine.lua.LuaMapIterator;
import com.tsuru2d.engine.lua.LuaUtils;
import org.luaj.vm2.*;

/**
 * A {@link LuaTable} serializer module for {@link Json}. Note that if
 * the table is an array (see {@link LuaUtils#isArray(LuaTable)}),
 * it will be serialized as a JSON array. Otherwise, all keys will
 * be converted to strings and the table will be serialized as a JSON object.
 * Also note that {@code nil} cannot be distinguished from no value at all,
 * so be careful with trailing {@code nil} values in your arrays.
 */
public class JsonLuaTableSerializer implements Json.Serializer<LuaTable> {
    private static void writeArray(Json json, LuaTable table) {
        json.writeArrayStart();
        LuaArrayIterator iterator = new LuaArrayIterator(table);
        while (iterator.hasNext()) {
            Varargs entry = iterator.next();
            Object value = LuaUtils.bridgeLuaToJava(entry.arg(2));
            json.writeValue(value);
        }
        json.writeArrayEnd();
    }

    private static void writeObject(Json json, LuaTable table) {
        json.writeObjectStart();
        LuaMapIterator iterator = new LuaMapIterator(table);
        while (iterator.hasNext()) {
            Varargs entry = iterator.next();
            String key = entry.arg1().checkjstring();
            Object value = LuaUtils.bridgeLuaToJava(entry.arg(2));
            json.writeValue(key, value);
        }
        json.writeObjectEnd();
    }

    @Override
    public void write(Json json, LuaTable table, Class knownType) {
        if (LuaUtils.isArray(table)) {
            writeArray(json, table);
        } else {
            writeObject(json, table);
        }
    }

    private static LuaValue readValue(Json json, JsonValue entry) {
        switch (entry.type()) {
        case object:
            return readObject(json, entry);
        case array:
            return readArray(json, entry);
        case stringValue:
            return LuaString.valueOf(entry.asString());
        case doubleValue:
            return LuaDouble.valueOf(entry.asDouble());
        case longValue:
            return LuaInteger.valueOf(entry.asLong());
        case booleanValue:
            return LuaBoolean.valueOf(entry.asBoolean());
        case nullValue:
            return LuaValue.NIL;
        default:
            throw new AssertionError();
        }
    }

    private static LuaTable readArray(Json json, JsonValue jsonData) {
        LuaTable table = new LuaTable();
        int i = 0;
        for (JsonValue entry = jsonData.child(); entry != null; entry = entry.next()) {
            table.set(++i, readValue(json, entry));
        }
        return table;
    }

    private static LuaTable readObject(Json json, JsonValue jsonData) {
        LuaTable table = new LuaTable();
        for (JsonValue entry = jsonData.child(); entry != null; entry = entry.next()) {
            table.set(entry.name(), readValue(json, entry));
        }
        return table;
    }

    @Override
    public LuaTable read(Json json, JsonValue jsonData, Class type) {
        if (jsonData.isObject()) {
            return readObject(json, jsonData);
        } else if (jsonData.isArray()) {
            return readArray(json, jsonData);
        } else {
            throw new IllegalArgumentException(jsonData.type() + " cannot be converted to a Lua table");
        }
    }
}
