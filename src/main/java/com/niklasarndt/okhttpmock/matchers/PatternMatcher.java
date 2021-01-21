package com.niklasarndt.okhttpmock.matchers;

import okhttp3.Request;
import java.util.regex.Pattern;

public abstract class PatternMatcher implements Matcher {
    protected final Pattern pattern;

    public PatternMatcher(Pattern pattern) {
        this.pattern = pattern;
    }

    protected abstract String getText(Request request);

    @Override
    public boolean matches(Request request) {
        String text = getText(request);
        return text != null && pattern.matcher(text).matches();
    }

    @Override
    public String failReason(Request request) {
        String actual = getText(request);
        return MatcherHelper.reason(pattern.pattern(), actual);
    }

}
