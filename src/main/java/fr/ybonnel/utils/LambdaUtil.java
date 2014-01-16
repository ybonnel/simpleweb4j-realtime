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

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class LambdaUtil {

    @FunctionalInterface
    public static interface SupplierWithException<T> {
        T get() throws Exception;
    }

    @FunctionalInterface
    public static interface FunctionWithException<T, R> {
        R apply(T t) throws Exception;
    }

    @FunctionalInterface
    public static interface ConsumerWithException<T> {
        void accept(T t) throws Exception;
    }

    @FunctionalInterface
    public static interface RunnableWithException {
        void run() throws Exception;
    }

    public static <T> Supplier<T> uncheckSupplier(SupplierWithException<T> supplier) {
        return () -> {
            try {
                return supplier.get();
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        };
    }

    public static <T, R> Function<T, R> uncheckFunction(FunctionWithException<T, R> function) {
        return (param) -> {
            try {
                return function.apply(param);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        };
    }

    public static <T> Consumer<T> uncheckConsumer(ConsumerWithException<T> consumer) {
        return (param) -> {
            try {
                consumer.accept(param);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        };
    }

    public static Runnable uncheckRunnable(RunnableWithException runnable) {
        return () -> {
            try {
                runnable.run();
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        };
    }

}
