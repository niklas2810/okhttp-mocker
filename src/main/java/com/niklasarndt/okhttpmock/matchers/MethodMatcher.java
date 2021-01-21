package com.niklasarndt.okhttpmock.matchers;

import static com.niklasarndt.okhttpmock.matchers.MatcherHelper.reason;
import okhttp3.Request;

public class MethodMatcher implements Matcher {
    private final String method;

    public MethodMatcher(String method) {
        this.method = method;
    }

    @Override
    public boolean matches(Request request) {
        return method.equalsIgnoreCase(request.method());
    }

    @Override
    public String failReason(Request request) {
        return reason(method, request.method());
    }

    @Override
    public String toString() {
        return "method(" + method + ")";
    }

}
