package com.niklasarndt.okhttpmock.matchers;

import okhttp3.Request;

public interface Matcher {

    boolean matches(Request request);

    String failReason(Request request);

}
