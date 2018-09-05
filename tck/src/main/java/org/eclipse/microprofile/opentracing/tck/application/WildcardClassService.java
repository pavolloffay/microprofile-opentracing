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

package org.eclipse.microprofile.opentracing.tck.application;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * @author Pavol Loffay
 */
@Path(WildcardClassService.SERVICE_PATH)
public class WildcardClassService {
    public static final String SERVICE_PATH = "/wildcard/{id}/foo";
    public static final String METHOD_PATH = "/getFoo/{name}";

    @GET
    @Path(METHOD_PATH)
    public Response getFoo(@PathParam("id") String id, @PathParam("name") String name) {
        return Response.ok().build();
    }
}
