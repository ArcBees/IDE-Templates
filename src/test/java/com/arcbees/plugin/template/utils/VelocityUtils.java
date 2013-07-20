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

package com.arcbees.plugin.template.utils;

import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.AvalonLogChute;
import org.apache.velocity.runtime.resource.ResourceCacheImpl;
import org.apache.velocity.runtime.resource.ResourceManagerImpl;
import org.apache.velocity.runtime.resource.loader.URLResourceLoader;

public class VelocityUtils {
	public static VelocityEngine createRemoveVelocityEngine(String remoteBaseUrl) throws Exception {
		Properties properties = new Properties();
		properties.setProperty(RuntimeConstants.RESOURCE_LOADER, "url");
		properties.setProperty("url.resource.loader.class", URLResourceLoader.class.getName());
		properties.setProperty("url.resource.loader.timeout", "5000");
		properties.setProperty("url.resource.loader.root", remoteBaseUrl);
		properties.setProperty(RuntimeConstants.RESOURCE_MANAGER_CLASS, ResourceManagerImpl.class.getName());
		properties.setProperty(RuntimeConstants.RESOURCE_MANAGER_CACHE_CLASS, ResourceCacheImpl.class.getName());
		
		VelocityEngine velocityEngine;
		try {			
			velocityEngine = new VelocityEngine();
			velocityEngine.init(properties);
		} catch (Exception e) {
			throw e;
		}
        
        return velocityEngine;
	}
}
