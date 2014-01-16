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
package fr.ybonnel.utils;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;

import static fr.ybonnel.utils.LambdaUtil.uncheckRunnable;
import static fr.ybonnel.utils.LambdaUtil.uncheckSupplier;

public class FutureUtil {

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    private static Random random = new Random();

    private static void waitSomeTime() {
        uncheckRunnable(() -> Thread.sleep(500 + random.nextInt(500))).run();
    }

    public static <T> T getContentOfFuture(Future<T> future) {
        return uncheckSupplier((LambdaUtil.SupplierWithException<T>) future::get).get();
    }

    public static <T> Future<T> waitAndReturn(Supplier<T> supplier) {
        return executorService.submit(() -> {
            waitSomeTime();
            return supplier.get();
        });
    }
}
