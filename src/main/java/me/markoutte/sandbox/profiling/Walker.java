package me.markoutte.sandbox.profiling;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Walker {

    public static final int THREADS = Runtime.getRuntime().availableProcessors();

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        Path path = Paths.get(args[0]);

        System.out.println("Statistic for " + path);

        var forked = walk(path, p -> {
            Queue<Path> paths = new ConcurrentLinkedQueue<>();
            final ForkJoinPool fjp = new ForkJoinPool(THREADS);
            fjp.invoke(new FileWalker(path, paths::add));
            return paths;
        }, "ForkJoinPool");

        var executed = walk(path, p -> {
            Queue<Path> paths = new ConcurrentLinkedQueue<>();
            ExecutorFileWalker walker = new ExecutorFileWalker(p);
            walker.start(paths::add);
            return paths;
        }, "Exec. Service");

        var streamed = walk(path, p -> Files.walk(p).parallel().collect(Collectors.toList()), "Streams Par.");

//        streamed.stream().sorted().forEach(System.out::println);
    }

    private static <T extends Collection<Path>> T walk(Path path, Traverse<T> traverse, String name) {
        long start = System.currentTimeMillis();
        T result;
        try {
            result = traverse.walk(path);
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        long stop = System.currentTimeMillis();
        System.out.println(name + ":\tfound " + result.size() + " paths in " + (stop - start) + " ms");
        return result;
    }

    private static class ExecutorFileWalker {
        private final Path dir;
        private final ExecutorService service = Executors.newWorkStealingPool(THREADS);
        private final AtomicInteger lock = new AtomicInteger(0);

        private ExecutorFileWalker(Path dir) {
            this.dir = dir;
        }

        public void start(Consumer<Path> consumer) {
            submit(() -> run(dir, consumer));
            while (lock.get() != 0) {
                LockSupport.parkNanos(TimeUnit.MICROSECONDS.toNanos(100));
            }
            service.shutdown();
        }

        private void run(Path dir, Consumer<Path> consumer) {
            consumer.accept(dir);
            list(dir, p -> submit(() -> run(p, consumer)));
        }

        private void submit(Runnable runnable) {
            lock.incrementAndGet();
            service.submit(() -> {
                runnable.run();
                lock.decrementAndGet();
            });
        }
    }

    private static class FileWalker extends RecursiveAction {

        private final Path path;
        private final Consumer<Path> consumer;

        FileWalker(Path path, Consumer<Path> consumer) {
            Objects.requireNonNull(path);
            this.path = path;
            this.consumer = consumer;
        }

        @Override
        protected void compute() {
            consumer.accept(path);
            List<FileWalker> tasks = new ArrayList<>();
            list(path, p -> tasks.add(new FileWalker(p, consumer)));
            if (!tasks.isEmpty()) invokeAll(tasks);
        }
    }

    private static final LinkOption[] OPTIONS = new LinkOption[]{LinkOption.NOFOLLOW_LINKS};

    private static void list(Path dir, Consumer<Path> children) {
        try {
            BasicFileAttributes attributes = Files.readAttributes(dir, BasicFileAttributes.class, OPTIONS);
            if (!attributes.isDirectory()) return;
            try (DirectoryStream<Path> paths = Files.newDirectoryStream(dir)) {
                paths.forEach(children);
            }
        } catch (IOException ignore) { }
    }

    @FunctionalInterface
    private interface Traverse<T extends Collection<Path>> {
        T walk(Path path) throws IOException, ExecutionException, InterruptedException;
    }
}
