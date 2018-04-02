package com.dpforge.essy.proxy;

import com.dpforge.essy.proxy.decoder.ScriptDecoder;
import com.dpforge.essy.proxy.decoder.ScriptDecoderException;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScriptDecoderTest {
    @Test
    public void decode() throws ScriptDecoderException {
        final ScriptDecoder decoder = new ScriptDecoder();
        final String script = decoder.decode("H4sIAAAAAAAA/wtyDQ5QsFVIrUhNLi1JDUotLE0tLtEIcg3UBACtezGbGgAAAA==");
        assertEquals("RESP = executeRequest(REQ)", script);
    }
}