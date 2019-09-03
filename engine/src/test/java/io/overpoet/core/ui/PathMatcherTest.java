package io.overpoet.core.ui;

import io.overpoet.core.ui.PathMatch;
import io.overpoet.core.ui.PathMatcher;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class PathMatcherTest {

    @Test
    public void testSimpleNoTrailingSlash() {
        PathMatcher matcher = new PathMatcher("/foo/bar");
        assertThat( matcher.pattern().toString() ).isEqualTo("^/foo/bar/?$");
        assertThat( matcher.attemptMatch( "/foo")).isNull();
        assertThat( matcher.attemptMatch( "/foo/bar")).isNotNull();
        assertThat( matcher.attemptMatch( "/foo/bar/")).isNotNull();
        assertThat( matcher.attemptMatch( "/foo/bar/baz")).isNull();
    }

    @Test
    public void testSimpleWithTrailingSlash() {
        PathMatcher matcher = new PathMatcher("/foo/bar/");
        assertThat( matcher.pattern().toString() ).isEqualTo("^/foo/bar/?$");
        assertThat( matcher.attemptMatch( "/foo")).isNull();
        assertThat( matcher.attemptMatch( "/foo/bar")).isNotNull();
        assertThat( matcher.attemptMatch( "/foo/bar/")).isNotNull();
        assertThat( matcher.attemptMatch( "/foo/bar/baz")).isNull();
    }

    @Test
    public void testSegmentsNoTrailingSlash() {
        PathMatcher matcher = new PathMatcher("/foo/{name}/bar/{age}");
        assertThat( matcher.pattern().toString() ).isEqualTo("^/foo/([^/]*)/bar/([^/]*)/?$");
        PathMatch match = matcher.attemptMatch("/foo/bob/bar/42");
        assertThat(match).isNotNull();
        assertThat(match.get("name")).isEqualTo("bob");
        assertThat(match.get("age")).isEqualTo("42");

        match = matcher.attemptMatch("/foo/bob/bar/42/");
        assertThat(match).isNotNull();
        assertThat(match.get("name")).isEqualTo("bob");
        assertThat(match.get("age")).isEqualTo("42");

        assertThat( matcher.attemptMatch("/foo/bob/bar/42/tacos") ).isNull();
        assertThat( matcher.attemptMatch("/foo/bar") ).isNull();
    }

    @Test
    public void testSegmentsWithTrailingSlash() {
        PathMatcher matcher = new PathMatcher("/foo/{name}/bar/{age}/");
        assertThat( matcher.pattern().toString() ).isEqualTo("^/foo/([^/]*)/bar/([^/]*)/?$");
        PathMatch match = matcher.attemptMatch("/foo/bob/bar/42");
        assertThat(match).isNotNull();
        assertThat(match.get("name")).isEqualTo("bob");
        assertThat(match.get("age")).isEqualTo("42");

        match = matcher.attemptMatch("/foo/bob/bar/42/");
        assertThat(match).isNotNull();
        assertThat(match.get("name")).isEqualTo("bob");
        assertThat(match.get("age")).isEqualTo("42");

        assertThat( matcher.attemptMatch("/foo/bob/bar/42/tacos") ).isNull();
        assertThat( matcher.attemptMatch("/foo/bar") ).isNull();
    }
}
