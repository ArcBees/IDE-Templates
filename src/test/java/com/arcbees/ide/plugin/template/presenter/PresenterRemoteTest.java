package com.arcbees.ide.plugin.template.presenter;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.Assert;
import org.junit.Test;

import com.arcbees.ide.plugin.template.util.FetchProperties;
import com.jayway.restassured.RestAssured;

public class PresenterRemoteTest {
    private String propertiesUrlPath = "https://raw.github.com/ArcBees/IDE-Templates/1.0.0/" +
    		"src/main/resources/com/arcbees/ide/plugin/template/presenter/template.properties";

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
