package com.niklasarndt.okhttpmock;


import okhttp3.MediaType;

public class MediaTypes {

    public static final MediaType MEDIATYPE_TEXT = MediaType.parse("text/plain");

    public static final MediaType MEDIATYPE_HTML = MediaType.parse("text/html");

    public static final MediaType MEDIATYPE_XML = MediaType.parse("text/xml");

    public static final MediaType MEDIATYPE_JSON = MediaType.parse("application/json");

    public static final MediaType MEDIATYPE_FORM_DATA = MediaType.parse("multipart/form-data");

    public static final MediaType MEDIATYPE_FORM_URLENCODED = MediaType.parse("application/x-www-form-urlencoded");

    public static final MediaType MEDIATYPE_RAW_DATA = MediaType.parse("application/octet-stream");

}
