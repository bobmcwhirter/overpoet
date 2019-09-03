package io.overpoet.core.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathMatcher {

    public PathMatcher(String path) {
        this.path = path;
        this.names.add("*");
        this.pattern = Pattern.compile(analyzeAndConvert(path));
    }

    public PathMatch attemptMatch(String path) {
        Matcher matcher = this.pattern.matcher(path);
        if (matcher.matches()) {
            PathMatch match = new PathMatch();
            for ( int i = 0 ; i < this.names.size() ; ++i ) {
                match.set( names.get(i), matcher.group(i));
            }
            return match;
        }

        return null;
    }

    String path() {
        return this.path;
    }

    Pattern pattern() {
        return this.pattern;
    }

    private static final Pattern SEGMENT = Pattern.compile("\\{([^}]*)\\}");

    String analyzeAndConvert(String str) {
        Matcher matcher = SEGMENT.matcher(str);

        int i = 0;
        while (i < str.length()) {
            if (matcher.find(i)) {
                this.names.add(matcher.group(1));
                i = matcher.end(1);
            } else {
                break;
            }
        }

        String result = matcher.replaceAll("([^/]*)");
        return "^" + result + ( result.endsWith("/") ? "?" : "/?" ) + "$";
    }

    @Override
    public String toString() {
        return "[PathMatcher: " + this.path + "]";
    }

    private final String path;

    private final Pattern pattern;

    private final List<String> names = new ArrayList<>();

}
