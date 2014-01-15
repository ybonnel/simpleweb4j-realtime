/*
 * Copyright 2013- Yan Bonnel
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.ybonnel;


import fr.ybonnel.modele.Event;
import fr.ybonnel.simpleweb4j.handlers.Response;
import fr.ybonnel.simpleweb4j.handlers.eventsource.ReactiveStream;
import fr.ybonnel.utils.FutureUtil;

import java.util.stream.Stream;

import static fr.ybonnel.simpleweb4j.SimpleWeb4j.*;
import static fr.ybonnel.utils.FutureUtil.waitAndReturn;
import static fr.ybonnel.utils.LambdaUtil.uncheck;

public class Main {


    private static int getPort() {
        // Cloudbees
        String cloudbeesPort = System.getProperty("app.port");
        if (cloudbeesPort != null) {
            return Integer.parseInt(cloudbeesPort);
        }

        // Default port;
        return 9999;
    }


    public static void main(String[] args) {

        setPort(getPort());

        setPublicResourcesPath("/fr/ybonnel/public");

        get("/stream", (param, routeParams) -> new Response<ReactiveStream<Event>>(
                handler -> getEvents()
                        .forEach(uncheck(handler::next))));

        get("/stream/:city", (param, routeParams) -> new Response<ReactiveStream<Event>>(
                handler -> getEvents()
                        .filter(event -> routeParams.getParam("city").equals(event.getCity()))
                        .forEach(uncheck(handler::next))));
        start();
    }

    private static Stream<Event> getEvents() {
        return Stream.generate(
                () -> waitAndReturn(Event::new))
                .map(FutureUtil::<Event>getContentOfFuture);
    }
}
