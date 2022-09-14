package com.pp.program1;

import Iterator.ImageSlide;
import Iterator.Slides;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.Objects;

public class OuterFiles {
    public static void saveImagePNG(Pane pane, File file) {
        try {
            WritableImage writableImage = new WritableImage((int) pane.getWidth(), (int) pane.getHeight());
            pane.snapshot(null, writableImage);
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
            ImageIO.write(renderedImage, "png", file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void saveFile(File file, Slides collection, int Anim, double speedAnim) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream outStream = new ObjectOutputStream(fos);
            outStream.writeObject(collection);
            for (ImageSlide slide : collection.getSlides()) {
                saveImage(slide.getImage().getImage(), outStream);
                saveCanvas(slide.getGraphic(), outStream);
            }
            outStream.writeInt(Anim);
            outStream.writeDouble(speedAnim);
            outStream.flush();
            outStream.close();
            System.out.println("Сохранено!");
        } catch (Exception e) {
            System.out.println("Ошибка:" + e.getMessage());
        }
    }

    private static Image getImage(ObjectInputStream inputStream) throws IOException {
        int width = inputStream.readInt();
        int height = inputStream.readInt();
        if (width != 0 && height != 0) {
            WritableImage img = new WritableImage(width, height);
            int[][] data = new int[width][height];
            for (int i = 0; i < width; i++)
                for (int j = 0; j < height; j++)
                    data[i][j] = inputStream.readInt();
            PixelWriter w = img.getPixelWriter();
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    w.setArgb(i, j, data[i][j]);
                }
            }
            return img;
        }

        return null;
    }
    private static Canvas getCanvas(ObjectInputStream inputStream) throws IOException {
        int width = inputStream.readInt();
        int height = inputStream.readInt();
        if (width != 0 && height != 0) {
            WritableImage img = new WritableImage(width, height);
            int[][] data = new int[width][height];
            for (int i = 0; i < width; i++)
                for (int j = 0; j < height; j++)
                    data[i][j] = inputStream.readInt();
            PixelWriter w = img.getPixelWriter();
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    w.setArgb(i, j, data[i][j]);
                }
            }
            Canvas canvas = new Canvas(width, height);
            canvas.getGraphicsContext2D().drawImage(img, 0, 0);
            return canvas;
        }
        return null;
    }

    private static void saveCanvas(Canvas canvas, ObjectOutputStream outStream) throws IOException {
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        WritableImage image = canvas.snapshot(params, null);

        int width = ((int) image.getWidth());
        int height = ((int) image.getHeight());

        int[][] data = new int[width][height];

        PixelReader r = image.getPixelReader();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                data[i][j] = r.getArgb(i, j);
            }
        }
        outStream.writeInt(width);
        outStream.writeInt(height);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                outStream.writeInt(data[i][j]);
    }

    private static void saveImage(Image image, ObjectOutputStream outStream) throws IOException {
        if(image!=null) {
            int width = ((int) image.getWidth());
            int height = ((int) image.getHeight());

            int[][] data = new int[width][height];

            PixelReader r = image.getPixelReader();
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    data[i][j] = r.getArgb(i, j);
                }
            }
            outStream.writeInt(width);
            outStream.writeInt(height);
            for (int i = 0; i < width; i++)
                for (int j = 0; j < height; j++)
                    outStream.writeInt(data[i][j]);
        }
        else {
            outStream.writeInt(0);
            outStream.writeInt(0);
        }
    }

    public static Slides loadFile(File file, Slides collection, ToggleGroup speeds, ToggleGroup anim) {
        int mode;
        double speed;
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream inputStream = new ObjectInputStream(fis);
            collection = (Slides) inputStream.readObject();
            for (ImageSlide slide : collection.getSlides()) {
                slide.setImg(new BackgroundImage(Objects.requireNonNull(getImage(inputStream)),
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.DEFAULT,
                        new BackgroundSize(1.0, 1.0, true, true, false, false)));
                slide.setCanvas(Objects.requireNonNull(getCanvas(inputStream)));
            }
            mode = inputStream.readInt();
            switch (mode) {
                case 0:
                    anim.selectToggle(anim.getToggles().get(0));
                    break;
                case 1:
                    anim.selectToggle(anim.getToggles().get(1));
                    break;
                case 2:
                    anim.selectToggle(anim.getToggles().get(2));
                    break;
            }
            speed=inputStream.readDouble();
            if(speed==100) speeds.selectToggle(speeds.getToggles().get(0));
            else speeds.selectToggle(speeds.getToggles().get(1));
            inputStream.close();
            return collection;
        } catch (Exception e) {
            System.out.println("Ошибка:" + e.getMessage());
        }
        return collection;
    }
}
