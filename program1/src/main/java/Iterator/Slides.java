package Iterator;

import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.ArrayList;

public class Slides implements Aggregate, Serializable {
    private final ArrayList<ImageSlide> Slides;
    transient private Image bi;

    public Slides(ArrayList<ImageSlide> slides) {
        Slides = slides;
    }
    public void addSlide(ImageSlide slide)
    {
        Slides.add(slide);
    }

    public ArrayList<ImageSlide> getSlides() {
        return Slides;
    }

    @Override
    public Iterator getIterator() {
        return new SlidesIterator(Slides);
    }
}