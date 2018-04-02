package com.dpforge.essy.engine.sugar;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HttpHeaders {
    private final Map<String, List<String>> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public static final String CONTENT_ENCODING = "Content-Encoding";

    private HttpHeaders() {
    }

    @Nullable
    public String get(final String name) {
        final List<String> value = getAll(name);
        return join(value);
    }

    @Nullable
    public List<String> getAll(final String name) {
        return map.get(name);
    }

    public void set(final String name, @Nullable final String value) {
        setAll(name, split(value));
    }

    public void setAll(final String name, @Nullable final List<String> value) {
        map.put(name, value);
    }

    public void setMap(@Nullable final Map<String, String> map) {
        if (map == null) {
            return;
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            set(entry.getKey(), entry.getValue());
        }
    }

    public void addValue(final String name, @Nullable final String value) {
        if (value == null) {
            return;
        }
        final List<String> items = getAll(name);
        if (items == null) {
            set(name, value);
        } else {
            items.add(value);
        }
    }

    public void addMap(@Nullable final Map<String, String> map) {
        if (map == null) {
            return;
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            addValue(entry.getKey(), entry.getValue());
        }
    }

    public boolean remove(final String name) {
        return map.remove(name) != null;
    }

    public boolean removeValue(final String name, @Nullable final String value) {
        if (value == null) {
            return false;
        }
        final List<String> items = getAll(name);
        return items != null && items.remove(value);
    }

    public boolean contains(final String name) {
        return map.containsKey(name);
    }

    public boolean containsValue(final String name, final String value) {
        final List<String> items = getAll(name);
        return items != null && items.contains(value);
    }

    public void clear() {
        map.clear();
    }

    public int size() {
        return map.size();
    }

    public Map<String, String> buildMap() {
        final Map<String, String> result = new HashMap<>(map.size());
        for (String name : map.keySet()) {
            result.put(name, get(name));
        }
        return result;
    }

    static HttpHeaders create() {
        return create(Collections.emptyMap());
    }

    static HttpHeaders create(final Map<String, String> map) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setMap(map);
        return headers;
    }

    private static List<String> split(@Nullable final String value) {
        if (value == null) {
            return null;
        }
        if (value.isEmpty()) {
            return new ArrayList<>();
        }
        final String[] items = value.split("\\s*,\\s*");
        final List<String> list = new ArrayList<>(items.length);
        Collections.addAll(list, items);
        return list;
    }

    private static String join(@Nullable final List<String> list) {
        if (list == null) {
            return null;
        }
        if (list.isEmpty()) {
            return "";
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        final StringBuilder builder = new StringBuilder();
        String separator = "";
        for (String item : list) {
            builder.append(separator).append(item);
            separator = ", ";
        }
        return builder.toString();
    }
}
