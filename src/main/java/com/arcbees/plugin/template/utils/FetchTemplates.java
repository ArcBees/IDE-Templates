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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FetchTemplates {
    private ExecutorService executor;
    private Map<String, FetchTemplate> pathsToFetch;
    private String rootPath;

    public FetchTemplates() {
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public void addPath(String path) {
        if (pathsToFetch == null) {
            pathsToFetch = new HashMap<>();
        }

        pathsToFetch.put(path, null);
    }

    public Map<String, FetchTemplate> getPathsToFetch() {
        return pathsToFetch;
    }

    public void run() {
        if (pathsToFetch == null || pathsToFetch.isEmpty()) {
            return;
        }

        executor = Executors.newFixedThreadPool(pathsToFetch.keySet().size());

        startThreads();

        executor.shutdown();
        // Wait until all threads are finish
        while (!executor.isTerminated()) {
        }
    }

    private void startThreads() {
        Set<String> paths = pathsToFetch.keySet();
        for (String path : paths) {
            initThread(path);
        }
    }

    private void initThread(String path) {
        FetchTemplate fetch = new FetchTemplate(rootPath, path);
        pathsToFetch.put(path, fetch);
        executor.execute(fetch);
    }
}
