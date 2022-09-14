package com.pp.program1;

import Iterator.ImageSlide;
import Iterator.Slides;
import Iterator.Iterator;
import javafx.animation.*;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    public Slides imgs;
    public Iterator iter_main;
    public Canvas graphics;
    public MenuBar menuBar;
    public ToggleGroup animation;
    public RadioMenuItem moveAnim;
    public RadioMenuItem darkAnim;
    public RadioMenuItem fastAnim;
    public RadioMenuItem slowAnim;
    public ToggleGroup animationspeed;
    public RadioMenuItem painter;

    GraphicsContext gc;
    public Label num;
    ImageSlide SlideCurr;
    public AnchorPane slide;

    public double speed;

    public void SaveSlide(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png files (*.png)", "*.png"));
        File file = fileChooser.showSaveDialog(null);
        if(file != null){
            OuterFiles.saveImagePNG(slide,file);
        }
    }
    public void addText()
    {
        TextLine text = new TextLine(new Point2D(30,30),"текст");
        SlideCurr.addText(text);
        slide.getChildren().add(text);
        text.draw();
    }

    public void saveAll(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите папку для сохранения...");
        fileChooser.setInitialFileName("slides");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файлы списка слайдов", "*.slds"));
        File file = fileChooser.showSaveDialog(slide.getScene().getWindow());

        if (file != null) {

            OuterFiles.saveFile(file,imgs,animation.getToggles().indexOf(animation.getSelectedToggle()),speed);
        }
    }

    public void changeSpeed()
    {
        if (fastAnim.isSelected())
            speed=100.0;
        else
            speed=3500.0;
    }
    public void openFile()
    {
         FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выбрать файл со слайдами");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Список слайдов", "*.slds");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(null);
        if (file!=null) {
            imgs = OuterFiles.loadFile(file, imgs, animationspeed,animation);
            changeSpeed();
            for(ImageSlide sld: imgs.getSlides())
                for (int i=0;i< sld.getTexts().size();i++) {
                    sld.getTexts().set(i,new TextLine(new Point2D(sld.getTexts().get(i).pX, sld.getTexts().get(i).pY),sld.getTexts().get(i).text));
                }
            ImageSlide sld = SlideCurr;
            SlideCurr = imgs.getSlides().get(0);
            iter_main = imgs.getIterator();
            num.setText(String.valueOf(iter_main.getCurrent()+1));
            delTexts(sld);
            setSlide(SlideCurr);
        }
    }
    public void addSlide()
    {
        imgs.addSlide(new ImageSlide.SlideBuilder(graphics).build());
    }

    public void duplicateSlide()
    {
        imgs.addSlide(new ImageSlide.SlideBuilder(graphics).setTexts(SlideCurr.getTexts()).setImg(SlideCurr.getImage()).setCanvas(SlideCurr.getGraphic()).build());
    }
    public void next()
    {
        if(darkAnim.isSelected())
            darkAnimSettings();
        else if (moveAnim.isSelected())
            moveAnimSetting();
        ImageSlide sld = SlideCurr;
        SlideCurr = (ImageSlide) iter_main.next();
        num.setText(String.valueOf(iter_main.getCurrent()+1));
        delTexts(sld);
        setSlide(SlideCurr);
    }
    public void darkAnimSettings()
    {
        slide.toBack();
        RotateTransition rotateTransition = new RotateTransition();
        rotateTransition.setDuration(Duration.millis(speed));
        rotateTransition.setFromAngle(0);
        rotateTransition.setNode(slide);
        rotateTransition.setByAngle(360);
        rotateTransition.setAutoReverse(true);
        rotateTransition.play();
        slide.toFront();
    }

    public void moveAnimSetting()
    {
        FadeTransition ft2 = new FadeTransition(Duration.millis(speed), slide);
        ft2.setFromValue(0);
        ft2.setToValue(1);
        ft2.play();
    }

    public void prev()
    {
        ImageSlide sld = SlideCurr;
        SlideCurr = (ImageSlide) iter_main.prev();
        num.setText(String.valueOf(iter_main.getCurrent()+1));
        delTexts(sld);
        setSlide(SlideCurr);
    }

    public void delTexts(ImageSlide ImageSlide)
    {
        for(TextLine text: ImageSlide.getTexts()) {
            slide.getChildren().remove(text);
        }
    }
    public void setSlide(ImageSlide img)
    {
        if(img.getImage()!=null)
            slide.setBackground(new Background(img.getImage()));
        else slide.setBackground(null);
        graphics.getGraphicsContext2D().clearRect(0,0,graphics.getWidth(),graphics.getHeight());
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        WritableImage image = img.getGraphic().snapshot(params, null);
        graphics.getGraphicsContext2D().drawImage(image, 0, 0);
        for(TextLine text: img.getTexts()) {
            slide.getChildren().add(text);
            text.draw();
        }
    }
    public void moveForward()
    {
        SlideCurr = (ImageSlide) iter_main.moveForward(graphics);
        num.setText(String.valueOf(iter_main.getCurrent()+1));
        delTexts(SlideCurr);
        setSlide(SlideCurr);
    }
    public void moveBackward()
    {
        SlideCurr = (ImageSlide) iter_main.moveBackward(graphics);
        num.setText(String.valueOf(iter_main.getCurrent()+1));
        delTexts(SlideCurr);
        setSlide(SlideCurr);
    }
    public void clearSlide()
    {
        SlideCurr.getGraphic().getGraphicsContext2D().clearRect(0,0,graphics.getWidth(), graphics.getHeight());
        SlideCurr.setImg(null);
        slide.setBackground(null);
        while(SlideCurr.getTexts().size()>0) {
            slide.getChildren().remove(SlideCurr.getTexts().get(0));
            SlideCurr.getTexts().remove(0);
        }
        graphics.getGraphicsContext2D().clearRect(0,0,graphics.getWidth(), graphics.getHeight());
    }

    public void setImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выбор изображения");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Изображение", "*.jpg", "*.png", "*.gif", "*.bmp");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(null);
        if(file!=null) {
            setBackgroundImage(new Image("file:"+file.getAbsolutePath(), slide.getWidth(), slide.getHeight(), false, true));
        }
    }

    public void setBackgroundImage(Image img)
    {
        BackgroundImage image;
        image = new BackgroundImage(img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0, 1.0, true, true, false, false));
        slide.setBackground(new Background(image));
        SlideCurr.setImg(image);
    }
    public void draw(double x, double y)
    {
        gc.setFill(Color.BLACK);
        if(painter.isSelected())
            gc.fillOval(x - 5, y - 5, 10, 10);
        else
            gc.clearRect(x,y,10,10);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imgs = new Slides(new ArrayList<>());
        SlideCurr = new ImageSlide.SlideBuilder(graphics).build();
        imgs.addSlide(SlideCurr);
        iter_main = imgs.getIterator();
        gc = graphics.getGraphicsContext2D();
        graphics.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                e -> draw(e.getSceneX(),e.getSceneY() - menuBar.getHeight()));
        graphics.addEventHandler(MouseEvent.MOUSE_RELEASED,
                e -> SlideCurr.setCanvas(graphics));
    }
}