package me.markoutte.sandbox.swing.monsters;

import org.jetbrains.annotations.NotNull;

import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.util.Random;
import java.util.function.Function;

public final class SpriteImage32<T> {

    public final static int SIZE = 32;
    private final static Random RANDOM = new Random();

    final @NotNull T source;
    final double x, y;
    final int height, width;

    private SpriteImage32(@NotNull T source, double x, double y, int height, int width) {
        this.source = source;
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }

    public Point calcPoint(long monotonic, int canvasWidth, int canvasHeight, float density) {
        var w = canvasWidth + width * density;
        var h = canvasHeight + height * density;
        return new Point(
                (int) Math.round((x * w + monotonic) % w - width * density),
                (int) Math.round((y * h + monotonic) % h - height * density)
        );
    }

    public static <T> SpriteImage32<T> catchOne(T bunny) {
        return new SpriteImage32<>(
                bunny,
                RANDOM.nextDouble(),
                RANDOM.nextDouble(),
                SIZE,
                SIZE
        );
    }

    public static <T> T createOne(String base64, Function<byte[], T> block) {
        var data = DatatypeConverter.parseBase64Binary(base64.split(",")[1]);
        return block.apply(data);
    }
}
