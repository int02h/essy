package com.dpforge.essy.engine.script;

import com.dpforge.essy.engine.script.api.ScriptApiContext;
import groovy.lang.Binding;

class EngineBinding extends Binding {

    private final ScriptApiContext apiContext;

    EngineBinding(final ScriptApiContext apiContext) {
        this.apiContext = apiContext;
    }

    ScriptApiContext getApiContext() {
        return apiContext;
    }
}
