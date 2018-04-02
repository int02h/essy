package com.dpforge.essy.engine.script;

import com.dpforge.essy.engine.HttpRequest;
import com.dpforge.essy.engine.HttpResponse;
import com.dpforge.essy.engine.script.api.ScriptApiContext;
import com.dpforge.essy.engine.sugar.HttpResponseSugar;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;

import javax.annotation.Nullable;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class ScriptEngine {

    public static final String REQUEST_VARIABLE = "REQ";
    public static final String RESPONSE_VARIABLE = "RESP";

    private final ScriptApiContext apiContext;

    public ScriptEngine(final RequestExecutor requestExecutor) {
        apiContext = new ScriptApiContext(requestExecutor);
    }

    @Nullable
    public HttpResponse execute(final HttpRequest request, final String script) throws ScriptExecutionException {
        return execute(request, script, new OutputStreamWriter(System.out));
    }

    @Nullable
    public HttpResponse execute(final HttpRequest request, final String script, final Writer logWriter)
            throws ScriptExecutionException {
        final GroovyShell shell = createShell(new EngineBinding(apiContext));

        try {
            shell.setVariable(REQUEST_VARIABLE, apiContext.wrapRequest(request));
            shell.setVariable("out", logWriter);
            shell.evaluate(script);
        } catch (Exception e) {
            throw new ScriptExecutionException("Exception occurred during script execution", e);
        }

        return extractResponseVariable(shell);
    }

    private static GroovyShell createShell(final Binding binding) {
        final CompilerConfiguration configuration = new CompilerConfiguration();
        configuration.setScriptBaseClass(ScriptApiBase.class.getName());
        return new GroovyShell(binding, configuration);
    }

    @Nullable
    private static HttpResponse extractResponseVariable(final GroovyShell shell) {
        final Binding binding = shell.getContext();
        if (binding.hasVariable(RESPONSE_VARIABLE)) {
            final Object response = binding.getVariable(RESPONSE_VARIABLE);
            if (response instanceof HttpResponseSugar) {
                return ((HttpResponseSugar) response).buildResponse();
            }
        }
        return null;
    }
}
