/*
 * Copyright 2018 Johns Hopkins University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dataconservancy.pass.loader.nihms;

import org.dataconservancy.pass.loader.nihms.util.ConfigUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds information and methods required to configure the NIHMS harvester tool.
 * @author Karen Hanson
 */
public class NihmsHarvesterConfig {

    private static final String API_HOST_KEY = "nihmsetl.api.host";

    private static final String DEFAULT_API_HOST = "www.nci.nlm.nih.gov";

    private static final String API_SCHEME_KEY = "nihmsetl.api.scheme";

    private static final String DEFAULT_API_SCHEME = "https";

    private static final String API_PATH_KEY = "nihmsetl.api.path";

    private static final String DEFAULT_API_PATH = "/pmc/utils/pacm/";

    static final String API_URL_PARAM_PREFIX = "nihmsetl.api.url.param.";

    private static final String HTTP_READ_TIMEOUT_KEY = "nihmsetl.http.read-timeout-ms";

    private static final String DEFAULT_HTTP_READ_TIMEOUT = "10000";

    private static final String HTTP_CONNECT_TIMEOUT_KEY = "nihmsetl.http.connect-timeout-ms";

    private static final String DEFAULT_HTTP_CONNECT_TIMEOUT = "10000";

    public static String getApiHost() {
        return ConfigUtil.getSystemProperty(API_HOST_KEY, DEFAULT_API_HOST);
    }

    public static String getApiScheme() {
        return ConfigUtil.getSystemProperty(API_SCHEME_KEY, DEFAULT_API_SCHEME);
    }

    public static String getApiPath() {
        return ConfigUtil.getSystemProperty(API_PATH_KEY, DEFAULT_API_PATH);
    }

    public static Map<String, String> getApiUrlParams() {
        return System.getProperties()
                .entrySet()
                .stream()
                .filter((entry) -> ((String)entry.getKey()).startsWith(API_URL_PARAM_PREFIX))
                .collect(HashMap::new,
                        (map, entry) -> map.put(((String)entry.getKey()).substring(API_URL_PARAM_PREFIX.length()), (String)entry.getValue()),
                        (map1, map2) -> map2.putAll(map1));
    }
}
