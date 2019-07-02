/*
 *
 *  * Copyright 2019 Johns Hopkins University
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.dataconservancy.pass.loader.nihms;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class NihmsHarvesterConfigTest {

    /**
     * Insures that the {@link NihmsHarvesterConfig#getApiUrlParams()} properly processes the system properties prefixed by {@code nihmsetl.api.url.param.}
     */
    @Test
    public void urlParameters() {
        Map<String, String> expectedParams = new HashMap<String, String>() {
            {
                put("param1", "value1");
                put("param2", "value2");
                put("param3", "value3");
            }
        };

        expectedParams.forEach((key, value) -> {
            key = NihmsHarvesterConfig.API_URL_PARAM_PREFIX + key;
            System.setProperty(key, value);
        });

        Map<String, String> actualParams = NihmsHarvesterConfig.getApiUrlParams();

        assertEquals(expectedParams, actualParams);
    }
}