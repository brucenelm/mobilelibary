package org.aru.mobilelibrary;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mmichalek on 10/24/15.
 */
public class Scope {

    public static final String USER_MODEL = "USER_MODEL";
    private static Map<String,   Object> scope = new HashMap<String, Object>();

    public static final String RESOURCE_SEARCH_RESULTS = "resourceSearchResults";
    public static final String CURRENT_LIB_RESOURCE = "currentLibResource";

    public static void setValue(String key, Object value) {
        scope.put(key, value);
    }

    public static <T> T getValue(String key) {
        return (T)scope.get(key);
    }

}
