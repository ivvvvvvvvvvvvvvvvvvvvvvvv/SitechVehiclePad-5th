package com.sitechdev.net;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Gson工具类
 *
 * @author liuhe
 * @created 2017/8/3
 */
public class GsonUtils {
    private static final String TAG = GsonUtils.class.getName();

    private static Gson GSON = createGson();
    private static final Gson GSON_NO_NULLS = createGson(false);

    private GsonUtils() {
    }

    /**
     * Create the standard {@link Gson} configuration
     *
     * @return created gson, never null
     */
    private static final Gson createGson() {
        return createGson(true);
    }

    /**
     * Create the standard {@link Gson} configuration
     *
     * @param serializeNulls whether nulls should be serialized
     * @return created gson, never null
     */
    private static Gson createGson(final boolean serializeNulls) {
        final GsonBuilder builder = new GsonBuilder();
        // 是否序列化带空的参数到gson中
        if (serializeNulls) {
            builder.serializeNulls();
        }
        return builder.create();
    }

    /**
     * Get reusable pre-configured {@link Gson} instance
     *
     * @return Gson instance
     */
    public static final Gson getGson() {
        return GSON;
    }

    /**
     * Get reusable pre-configured {@link Gson} instance
     *
     * @return Gson instance
     */
    public static final Gson getGson(final boolean serializeNulls) {
        return serializeNulls ? GSON : GSON_NO_NULLS;
    }

    /**
     * Convert string to given type
     *
     * @return instance of type
     */
    public static <T> T jsonToBean(String json, Class<T> clazz) {
        try {
            return GSON.fromJson(json, clazz);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * Convert string to List
     */
    public static  <T> List<T> jsonToList(String json, Class<T> cls) {
        try {
            List<T> list = new ArrayList<T>();
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (final JsonElement elem : array) {
                list.add(GSON.fromJson(elem, cls));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Convert string to Map
     */
    public static <K, V> Map<K, V> jsonToMap(String json) {
        try {
            Map<K, V> map = GSON.fromJson(json, new TypeToken<Map<K, V>>() {
            }.getType());
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Convert object to json
     *
     * @return json string
     */
    public static final String toJson(final Object object) {
        return toJson(object, true);
    }

    /**
     * Convert object to json
     *
     * @return json string
     */
    public static final String toJson(final Object object, final boolean includeNulls) {
        try {
            return includeNulls ? GSON.toJson(object) : GSON_NO_NULLS.toJson(object);
        } catch (Exception e) {
            Log.e(TAG, "gson toJson failed");
        }
        return null;
    }

    public static Object getJsonValue(String data, String key) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            return jsonObject.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TextUtils.isEmpty(data) ? null : data;
    }

    public static String getJsonString(String sourceData) {
        try {
            JSONObject obj = new JSONObject(sourceData);
            return obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断是否是正确json
     */
    public static boolean isGoodJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return false;
        }
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            Log.e(TAG, "bad json:" + json);
            return false;
        }
    }
}