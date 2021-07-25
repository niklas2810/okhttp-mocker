![okhttp-mocker](https://socialify.git.ci/niklas2810/okhttp-mocker/image?description=1&font=Raleway&language=1&owner=1&pattern=Plus&theme=Dark)


<p align="center">
<a href="https://search.maven.org/artifact/com.niklasarndt/okhttp-mocker">
<img alt="Maven Central" src="https://img.shields.io/maven-central/v/com.niklasarndt/okhttp-mocker?logo=java&style=for-the-badge">
</a>
<a href="https://github.com/niklas2810/okhttp-mocker/actions">
<img alt="GitHub Build" src="https://img.shields.io/github/workflow/status/niklas2810/okhttp-mocker/Build%20Project?logo=github&style=for-the-badge">
</a>
<a href="https://app.codacy.com/gh/niklas2810/okhttp-mocker/dashboard?branch=main">
<img alt="Codacy" src="https://img.shields.io/codacy/grade/d3c8c665d8f048f99c528250777fdc2a?logo=codacy&style=for-the-badge">
</a>
</p>

`okhttp-mocker` is a Java Testing library to provide fake
responses for an [OkHttp](https://github.com/square/okhttp) client.

**Table of Contents:**

- [Installation](#installation)
- [Usage](#usage)
- [Changes](#changes)
- [Dependencies](#dependencies)
- [License / Disclaimer](#license--disclaimer)


## Installation

```xml
<dependency>
    <groupId>com.niklasarndt</groupId>
    <artifactId>okhttp-mocker</artifactId>
    <version>1.0.1</version>
    <scope>test</scope>
</dependency>
```
_Please make sure that the version number is up to date (via the badge at the top)!_


## Usage

Copy of the [original usage notice](https://github.com/gmazzo/okhttp-client-mock#usage):

Example code:

```java
MockInterceptor interceptor = new MockInterceptor();

interceptor.addRule()
        .get().or().post().or().put()
        .url("https://testserver/api/login")
        .respond(HTTP_401_UNAUTHORIZED))
        .header("WWW-Authenticate", "Basic");

interceptor.addRule()
        .get("https://testserver/api/json")
        .respond("{succeed:true}", MEDIATYPE_JSON);

interceptor.addRule()
        .get("https://testserver/api/json")
        .respond(resource("sample.json"), MEDIATYPE_JSON);

interceptor.addRule()
        .pathMatches(Pattern.compile("/aPath/(\\w+)"))
        .anyTimes()
        .answer(request -> new Response.Builder()
            .code(200)
            .body(ResponseBody.create(null, "Path was " + request.url().encodedPath())));
```

Then add the interceptor to your OkHttpClient client and use it as usual:
```java
OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
```

Check an example [Integration Test](/src/test/java/com/niklasarndt/okhttpmock/Tests.java) with mocked HTTP responses!

You can use the following helper classes to provide mock responses from resources:
- `ClasspathResources.resource` to load content from classpath


## Changes 

I didn't write most of the project's code myself, much of it is from [gmazzo](https://github.com/gmazzo/okhttp-client-mock).
It has been published under the MIT License and I'm therefore allowed to modify and extend it as long as I mention its origin.

The problems I faced were these:

- The project targeted at Android users. **This is not a focus anymore** (which means that integrations like Roboelectric have been removed). 
However, the code might work just fine on Android devices, even though I didn't (and won't) test this.
- The project was no longer maintained. I updated the dependencies and make sure all deprecations
  have been fixed.
- The project was not available at Maven Central, only via Jitpack. I wanted to simplify the setup
  process and therefore put it on Maven Central.

## Dependencies

- [OkHttp](https://github.com/square/okhttp)
- [slf4j-api](https://mvnrepository.com/artifact/org.slf4j/slf4j-api)

Testing only:

- [junit-jupiter](https://junit.org/junit5/)

## License / Disclaimer

&copy; Niklas Arndt 2021, [MIT License](LICENSE.md)

Much inspiration (and code) was taken from [okhttp-client-mock](https://github.com/gmazzo/okhttp-client-mock).
Since this repo seems to be inactive and isn't available
from Maven Central, I decided to publish my own version.

You can find their original License [here](https://github.com/gmazzo/okhttp-client-mock/blob/master/LICENSE) (MIT).