package Iterator;

import javafx.scene.canvas.Canvas;

public interface Iterator {
    boolean hasNext(int mode);
    Object next();
    Object moveForward(Canvas canvas);
    Object moveBackward(Canvas canvas);
    Object preview();
    boolean hasPrev(int mode);
    Object prev();
    int getCurrent();
    void addSlides(ImageSlide slide);
    public ImageSlide getCurrentSlide();
}