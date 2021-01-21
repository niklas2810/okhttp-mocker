package com.niklasarndt.okhttpmock.matchers;

import okhttp3.Request;

public class NotMatcher implements Matcher {
    private final Matcher matcher;

    public NotMatcher(Matcher matcher) {
        this.matcher = matcher;
    }

    @Override
    public boolean matches(Request request) {
        return !matcher.matches(request);
    }

    @Override
    public String failReason(Request request) {
        return "not(" + matcher.failReason(request) + ")";
    }

    @Override
    public String toString() {
        return "not(" + matcher + ")";
    }

}
