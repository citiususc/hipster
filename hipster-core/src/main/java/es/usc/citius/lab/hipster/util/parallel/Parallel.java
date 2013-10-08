/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS), University of Santiago de Compostela (USC).
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package es.usc.citius.lab.hipster.util.parallel;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * <p/>
 * This class contains some useful classes and methods to parallelize code in an
 * easy and fluent way. Parallel class is self-contained to enable an easier
 * integration and reuse in different java projects.<br>
 * <p/>
 * Code example:<br>
 * <p/>
 * <pre>
 * new ForEach&lt;String, String&gt;(elements).apply(new Function&lt;String&gt;() {
 * 	public String apply(String element) {
 * 		System.out.println(element);
 *    }
 * }).values();
 * </pre>
 *
 * @author Pablo Rodríguez Mier
 * @version 13.01, 26 Jan 2013
 */
public final class Parallel {

    private Parallel() {
        throw new RuntimeException("Not instantiable");
    }

    /**
     * First-order function interface
     *
     * @param <E> Element to transform
     * @param <V> Result of the transformation
     * @author Pablo Rodríguez Mier
     */

    public static interface F<E, V> {
        /**
         * Apply a function over the element e.
         *
         * @param e Input element
         * @return transformation result
         */
        V apply(E e);
    }

    /**
     * Action class can be used to define a concurrent task that does not return
     * any value after processing the element.
     *
     * @param <E> Element processed within the action
     */
    public static abstract class Action<E> implements F<E, Void> {

        /**
         * This method is final and cannot be overridden. It applies the action
         * implemented by {@link Action#doAction(Object)}.
         */
        public final Void apply(E element) {
            doAction(element);
            return null;
        }

        /**
         * Defines the action that will be applied over the element. Every
         * action must implement this method.
         *
         * @param element element to process
         */
        public abstract void doAction(E element);
    }

    /**
     * This class provides some useful methods to handle the execution of a
     * collection of tasks.
     *
     * @param <V> value of the task
     */
    public static class TaskHandler<V> {
        private Collection<Future<V>> runningTasks = new LinkedList<Future<V>>();
        private ExecutorService executorService;

        public TaskHandler(ExecutorService executor,
                           Collection<Callable<V>> tasks) {

            this.executorService = executor;

            for (Callable<V> task : tasks) {
                runningTasks.add(executor.submit(task));
            }
        }

        /**
         * Get the current tasks (futures) that are being executed.
         *
         * @return Collection of futures
         * @see Future
         */
        public Collection<Future<V>> tasks() {
            return this.runningTasks;
        }

        /**
         * This function is equivalent to
         * {@link ExecutorService#awaitTermination(long, TimeUnit)}
         *
         * @see ExecutorService#awaitTermination(long, TimeUnit)
         */
        public boolean wait(long timeout, TimeUnit unit)
                throws InterruptedException {
            return this.executorService.awaitTermination(timeout, unit);
        }

        /**
         * Retrieves the result of the transformation of each element (the value
         * of each Future). This function blocks until all tasks are terminated.
         *
         * @return a collection with the results of the elements transformation
         * @throws InterruptedException
         * @throws ExecutionException
         */
        public Collection<V> values() throws InterruptedException,
                ExecutionException {
            Collection<V> results = new LinkedList<V>();
            for (Future<V> future : this.runningTasks) {
                results.add(future.get());
            }
            return results;
        }
    }

    /**
     * @param <E>
     * @param <V>
     */
    public static class ForEach<E, V> implements F<F<E, V>, TaskHandler<V>> {
        // Source elements
        private Iterable<E> elements;
        // Executor used to invoke concurrent tasks. By default it uses as many
        // threads
        // as processors available
        private ExecutorService executor = Executors.newFixedThreadPool(Runtime
                .getRuntime().availableProcessors());

        public ForEach(Iterable<E> elements) {
            this.elements = elements;
        }

        /**
         * Configure the number of available threads that will be used. Note
         * that this configuration has no effect if a custom executor
         * {@link ForEach#customExecutor(ExecutorService)} is provided.
         *
         * @param threads number of threads to use
         * @return a ForEach instance
         */
        public ForEach<E, V> withThreads(int threads) {
            this.executor = Executors.newFixedThreadPool(threads);
            return this;
        }

        /**
         * Set a custom executor service
         *
         * @param executor ExecutorService to use
         * @return the instance of ForEach configured with the new executor
         *         service.
         */
        public ForEach<E, V> customExecutor(ExecutorService executor) {
            this.executor = executor;
            return this;
        }

        /**
         * Encapsulates the ForEach instance into a Callable that retrieves a
         * TaskHandler with the invoked tasks. <br>
         * Example: <br>
         * <p/>
         * <pre>
         * Collection&lt;Double&gt; numbers = new Collection&lt;Double&gt;(...);
         * Callable&lt;TaskHandler&lt;V&gt;&gt; forEach = new ForEach&lt;Double, String&gt;(numbers)
         * 		.prepare(new F&lt;Double, String&gt;() {
         * 			String apply(Double e) {
         * 				return e.toString();
         *            }
         *        });
         * forEach.call().values();
         * </pre>
         *
         * @param f
         * @return
         */
        public Callable<TaskHandler<V>> prepare(final F<E, V> f) {
            return new Callable<Parallel.TaskHandler<V>>() {
                public TaskHandler<V> call() throws Exception {
                    return new ForEach<E, V>(elements).apply(f);
                }
            };
        }

        public TaskHandler<V> apply(F<E, V> f) {
            return new TaskHandler<V>(executor, map(elements, f));
        }

        private Collection<Callable<V>> map(Iterable<E> elements,
                                            final F<E, V> f) {
            // List of concurrent tasks
            List<Callable<V>> mappedTasks = new LinkedList<Callable<V>>();
            // Create a task for each element
            for (final E element : elements) {
                mappedTasks.add(new Callable<V>() {
                    public V call() throws Exception {
                        return f.apply(element);
                    }
                });
            }
            return mappedTasks;
        }
    }

    /**
     * InterruptedExceptions occurred during the execution will be thrown as
     * RuntimeExceptions. To handle these interruptions, use new For.Each
     * instead of this static method.
     *
     * @param elements
     * @param task
     */
    public static <A, V> Collection<V> ForEach(Iterable<A> elements,
                                               F<A, V> task) {
        try {
            return new ForEach<A, V>(elements).apply(task).values();
        } catch (Exception e) {
            throw new RuntimeException("ForEach method interrupted. "
                    + e.getMessage());
        }
    }

    /**
     * Perform a parallel iteration, similar to a for(i=from;i<to;i++).
     *
     * @param from   starting index
     * @param to     upper bound
     * @param action the action to perform in each iteration
     */
    public static void For(final long from, final long to,
                           final Action<Long> action) {

        ForEach(new Iterable<Long>() {
            public Iterator<Long> iterator() {
                return new Iterator<Long>() {
                    private long current = from;

                    public boolean hasNext() {
                        return current < to;
                    }

                    public Long next() {
                        return current++;
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        }, action);
    }

}
