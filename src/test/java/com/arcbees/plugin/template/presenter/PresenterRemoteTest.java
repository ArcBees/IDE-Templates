/**
 * Copyright 2013 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.arcbees.plugin.template.presenter;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.Assert;
import org.junit.Test;

import com.arcbees.plugin.template.util.FetchProperties;
import com.jayway.restassured.RestAssured;

public class PresenterRemoteTest {
    private String propertiesUrlPath = "https://raw.github.com/ArcBees/IDE-Templates/1.0.0/" +
    		"src/main/resources/com/arcbees/plugin/template/presenter/template.properties";

    @Test
    public void testRemoteGet() {
       String propertiesFile = RestAssured.get(propertiesUrlPath).andReturn().asString();
       System.out.println(propertiesFile);
       Assert.assertTrue(!propertiesFile.isEmpty());
    }
    
    @Test
    public void testRemotePropertiesObject() throws MalformedURLException, ConfigurationException {
       FetchProperties properties = new FetchProperties(propertiesUrlPath);
       properties.fetch();
       Assert.assertTrue(properties.getFiles().size() > 4);
    }
    
    @Test(expected = ConfigurationException.class)
    public void testRemotePropertiesObjectFail() throws MalformedURLException, ConfigurationException {
       URL url = new URL(propertiesUrlPath);
       Configuration config = new PropertiesConfiguration(url + "fail");
    }
}
