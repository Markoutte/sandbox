package me.markoutte.sandbox.minis;

import org.apache.commons.cli.*;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This application creates an image with scaled copies of some another image in specific folder.
 */
public class LayoutPhotos {

    public BufferedImage[] load(File folder) throws IOException {
        List<BufferedImage> images = new ArrayList<>();
        File[] files = folder.listFiles();
        if (files == null) {
            return new BufferedImage[0];
        }
        for (File file : files) {
            BufferedImage image = ImageIO.read(file);
            if (image != null) {
                images.add(image);
            }
        }
        return images.toArray(new BufferedImage[0]);
    }

    public void save(BufferedImage image, File folder, String format) throws IOException {
        String preferredName = "layout-photo";
        File file = new File(folder, String.format("%s.%s", preferredName, format));
        for (int i = 1; file.exists(); i++) {
            file = new File(folder, String.format("%s-%d.%s", preferredName, i, format));
        }
        ImageIO.write(image, format, file);
    }

    /**
     * Layout several images into one with custom width and height for specific scale.
     *
     * @param images Array of source images to layout
     * @param width The width of target image
     * @param height The height of target image
     * @param scale The scale of source images
     * @return A new image
     */
    public BufferedImage layout(BufferedImage[] images, int width, int height, int scale) {
        BufferedImage result = new BufferedImage(width, height, scale);
        Graphics2D graphics = (Graphics2D) result.getGraphics().create();
        graphics.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC );

        int hShift = width / scale;
        int vShift = height / scale;

        for (int i = 0; i < images.length || i < (scale << 1); i++) {
            int dx1 = i % scale * hShift;
            int dy1 = i / scale * vShift;
            int dx2 = dx1 + hShift;
            int dy2 = dy1 + vShift;
            graphics.drawImage(images[i], dx1, dy1, dx2, dy2, 0, 0, images[i].getWidth(), images[i].getHeight(), null);
        }

        return result;
    }

    public Options options() {
        Options options = new Options();
        options.addOption(Option.builder().argName("width").longOpt("width").hasArg().optionalArg(true).type(Integer.class).desc("Width of new image").build());
        options.addOption(Option.builder().argName("height").longOpt("height").hasArg().optionalArg(true).type(Integer.class).desc("Height of new image").build());
        options.addOption(Option.builder().argName("scale").longOpt("scale").hasArg().optionalArg(true).type(Integer.class).desc("Scale of origin images").build());
        options.addOption(Option.builder("o").argName("format").longOpt("output").hasArg().optionalArg(true).type(Integer.class).desc("Format of new image").build());
        return options;
    }

    public static void main(String[] args) throws IOException {
        LayoutPhotos app = new LayoutPhotos();

        Options options = app.options();
        HelpFormatter help = new HelpFormatter();
        CommandLine cmd;
        try {
            cmd = new DefaultParser().parse(options, args);
        } catch (ParseException e) {
            help.printHelp("laypho <folder> <options>", options);
            return;
        }
        if (args.length == 0) {
            help.printHelp("laypho <folder> <options>", options);
            return;
        }

        File source = new File(args[0]);
        if (!source.exists()) {
            help.printHelp("laypho <folder> <options>", options);
            return;
        }
        Integer width;
        Integer height;
        Integer scale;
        String format;
        try {
            width = Integer.parseInt(cmd.getOptionValue("width", "3264"));
            height = Integer.parseInt(cmd.getOptionValue("height", "2448"));
            scale = Integer.parseInt(cmd.getOptionValue("scale", "4"));
            format = cmd.getOptionValue("format", "jpg");
        } catch (NumberFormatException e) {
            help.printHelp("laypho <folder> <options>", options);
            return;
        }

        app.save(app.layout(app.load(source), width, height, scale), source, format);
    }

}
