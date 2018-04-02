package com.dpforge.essy.engine.sugar;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Content {

    @Nonnull
    private Representation currentRepresentation;

    private Content(@Nullable final byte[] data) {
        currentRepresentation = new ByteArrayRepresentation(data);
    }

    @Nullable
    public byte[] asBytes() {
        final ByteArrayRepresentation representation;
        if (currentRepresentation instanceof ByteArrayRepresentation) {
            representation = (ByteArrayRepresentation) currentRepresentation;
        } else {
            currentRepresentation = representation = new ByteArrayRepresentation(currentRepresentation);
        }
        return representation.data;
    }

    @Nullable
    public String asPlainText() {
        final StringRepresentation representation;
        if (currentRepresentation instanceof StringRepresentation) {
            representation = (StringRepresentation) currentRepresentation;
        } else {
            currentRepresentation = representation = new StringRepresentation(currentRepresentation);
        }
        return representation.value;
    }

    @Nullable
    public Map<String, Object> asJson() {
        final JsonRepresentation representation;
        if ((currentRepresentation instanceof JsonRepresentation)) {
            representation = (JsonRepresentation) currentRepresentation;
        } else {
            currentRepresentation = representation = new JsonRepresentation(currentRepresentation);
        }
        return representation.json;
    }

    public void setPlainText(@Nullable final String text) {
        currentRepresentation = new StringRepresentation(text);
    }

    public void setBytes(@Nullable final byte[] bytes) {
        currentRepresentation = new ByteArrayRepresentation(bytes);
    }

    static Content create(@Nullable final byte[] content) {
        return new Content(content);
    }

    static Content create() {
        return create(null);
    }

    private interface Representation {
        @Nullable
        byte[] getBytes();
    }

    private static class ByteArrayRepresentation implements Representation {

        @Nullable
        private final byte[] data;

        private ByteArrayRepresentation(@Nullable final byte[] data) {
            this.data = data;
        }

        ByteArrayRepresentation(final Representation representation) {
            this(representation.getBytes());
        }

        @Nullable
        @Override
        public byte[] getBytes() {
            return data;
        }
    }

    private static class StringRepresentation implements Representation {

        @Nullable
        private final String value;

        private StringRepresentation(final Representation representation) {
            final byte[] data = representation.getBytes();
            this.value = (data == null) ? null : new String(data);
        }

        StringRepresentation(@Nullable final String text) {
            this.value = text;
        }

        @Nullable
        @Override
        public byte[] getBytes() {
            return value == null ? null : value.getBytes(StandardCharsets.UTF_8);
        }
    }

    private static class JsonRepresentation implements Representation {

        private static final JsonParser JSON_PARSER = new JsonParser();

        @Nullable
        private final Map<String, Object> json;

        private JsonRepresentation(final Representation representation) {
            final byte[] data = representation.getBytes();
            if (data != null) {
                json = JSON_PARSER.toJson(data);
            } else {
                json = null;
            }
        }

        @Nullable
        @Override
        public byte[] getBytes() {
            return (json == null) ? null : JSON_PARSER.fromJson(json);
        }
    }
}
