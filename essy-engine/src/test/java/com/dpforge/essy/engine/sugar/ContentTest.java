package com.dpforge.essy.engine.sugar;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

@SuppressWarnings("ConstantConditions")
public class ContentTest {

    @Test
    public void plainText() {
        final String text = "Hello World";
        final Content content = content(text);
        assertEquals(text, content.asPlainText());
    }

    @Test
    public void asJson() {
        final String text = "{\"name\":\"John Smith\",\"age\":26}";
        final Content content = content(text);
        assertEquals("John Smith", content.asJson().get("name"));
        assertEquals(26, content.asJson().get("age"));
    }

    @Test
    public void modifyJson() {
        final String text = "{\"name\":\"John Smith\",\"age\":26}";
        final Content content = content(text);
        content.asJson().put("age", 62);
        assertEquals("{\"name\":\"John Smith\",\"age\":62}", content.asPlainText());
    }

    @Test
    public void modifyBytes() {
        final String text = "aaa";
        final Content content = content(text);
        content.asBytes()[1] = 'b';
        assertEquals("aba", content.asPlainText());
    }

    @Test
    public void setPlainText() {
        final String text = "aaa";
        final Content content = content(text);
        content.setPlainText("aba");
        assertArrayEquals(new byte[]{'a', 'b', 'a'}, content.asBytes());
    }

    @Test
    public void setBytes() {
        final Content content = content("");
        content.setBytes("Hello".getBytes(StandardCharsets.UTF_8));
        assertEquals("Hello", content.asPlainText());
    }

    @Test
    public void sameJsonObject() {
        final String text = "{\"name\":\"John Smith\",\"age\":26}";
        final Content content = content(text);
        final Object o1 = content.asJson();
        final Object o2 = content.asJson();
        assertTrue(o1 == o2);
    }

    @Test
    public void sameByteArray() {
        final String text = "Hello World";
        final Content content = content(text);
        final byte[] b1 = content.asBytes();
        final byte[] b2 = content.asBytes();
        assertTrue(b1 == b2);
    }

    @SuppressWarnings("StringEquality")
    @Test
    public void samePlainString() {
        final String text = "Hello World";
        final Content content = content(text);
        final String s1 = content.asPlainText();
        final String s2 = content.asPlainText();
        assertTrue(s1 == s2);
    }

    @Test
    public void nullData() {
        final Content content = Content.create();
        assertNull(content.asPlainText());
        assertNull(content.asJson());
        assertNull(content.asBytes());
    }

    private static Content content(final String text) {
        return Content.create(text.getBytes(StandardCharsets.UTF_8));
    }
}