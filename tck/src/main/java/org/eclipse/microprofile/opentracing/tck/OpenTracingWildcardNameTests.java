/*
 * Copyright (c) 2017 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
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

package org.eclipse.microprofile.opentracing.tck;

import io.opentracing.tag.Tags;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.Path;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * @author Pavol Loffay
 */
public class OpenTracingWildcardNameTests extends OpentracingClientBaseTests {

    public static class TestConfiguration implements ConfigSource {
        private Map<String, String> propMap = new HashMap<>();

        {
            propMap.put("mp.opentracing.operation-name-provider", "wildcard-path");
        }

        @Override
        public Map<String, String> getProperties() {
            return propMap;
        }

        @Override
        public String getValue(String s) {
            return propMap.get(s);
        }

        @Override
        public String getName() {
            return this.getClass().getName();
        }
    }

    @Deployment
    public static WebArchive createDeployment() {
        return OpenTracingBaseTests.createBasicDeployment()
            .addAsServiceProvider(ConfigSource.class, TestConfiguration.class);
    }

    /**
     * Get operation name depending on the {@code spanKind}.
     * @param spanKind The type of span.
     * @param httpMethod HTTP method
     * @param clazz resource class
     * @param javaMethod method name
     * @return operation name
     */
    @Override
    protected String getOperationName(String spanKind, String httpMethod, Class<?> clazz, String javaMethod) {
        if (spanKind.equals(Tags.SPAN_KIND_SERVER)) {
            StringBuilder operationName = new StringBuilder();
            Path classPath = clazz.getAnnotation(Path.class);
            if (classPath != null) {
                if (!classPath.value().startsWith("/")) {
                    operationName.append("/");
                }
                operationName.append(classPath.value());
            }
            if (!classPath.value().endsWith("/")) {
                operationName.append("/");
            }
            for (Method method: clazz.getMethods()) {
                Path methodPath = method.getAnnotation(Path.class);
                if (methodPath != null && methodPath.value().equals(javaMethod)) {
                    String methodPathStr = methodPath.value();
                    if (methodPathStr.startsWith("/")) {
                        methodPathStr.replaceFirst("/", "");
                    }
                    operationName.append(methodPathStr);
                }
            }
            System.out.println(operationName.toString());
            return operationName.toString();
        }
        return super.getOperationName(spanKind, httpMethod, clazz, javaMethod);
    }
}
