package com.niklasarndt.okhttpmock;

import com.niklasarndt.okhttpmock.matchers.HeaderMatcher;
import com.niklasarndt.okhttpmock.matchers.MatcherHelper;
import okhttp3.Request;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.regex.Pattern;

public class HeaderMatcherTests {
    private Request request;

    @BeforeEach
    public void setup() {
        request = new Request.Builder()
                .url("http://test.com")
                .header("a", "")
                .header("b", "aValue")
                .build();
    }

    private boolean hasHeader(String name) {
        return new HeaderMatcher(name, MatcherHelper.any()).matches(request);
    }

    private boolean headerIs(String name, String value) {
        return new HeaderMatcher(name, MatcherHelper.exact(value)).matches(request);
    }

    @Test
    public void testHasHeader_a() {
        assertTrue(hasHeader("a"));
    }

    @Test
    public void testHasHeader_A() {
        assertTrue(hasHeader("A"));
    }

    @Test
    public void testHasHeader_b() {
        assertTrue(hasHeader("b"));
    }

    @Test
    public void testHasHeader_B() {
        assertTrue(hasHeader("b"));
    }

    @Test
    public void testHasHeader_c() {
        assertFalse(hasHeader("c"));
    }

    @Test
    public void testHasHeader_C() {
        assertFalse(hasHeader("C"));
    }

    @Test
    public void testHeaderIs_a() {
        assertFalse(headerIs("a", "someValue"));
    }

    @Test
    public void testHeaderIs_a_Empty() {
        assertTrue(headerIs("a", ""));
    }

    @Test
    public void testHeaderIs_b() {
        assertTrue(headerIs("b", "aValue"));
    }

    @Test
    public void testHeaderIs_b_Empty() {
        assertFalse(headerIs("b", ""));
    }

    @Test
    public void testHeaderIs_b_Partial() {
        assertFalse(headerIs("b", "aVa"));
    }

    @Test
    public void testHeaderIs_b_Like() {
        assertTrue(new HeaderMatcher("b", Pattern.compile(".*alue.*")).matches(request));
    }

}
