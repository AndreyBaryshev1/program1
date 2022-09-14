package Iterator;

import javafx.scene.canvas.Canvas;


import java.util.ArrayList;


public class SlidesIterator implements Iterator {
    private int current=0;
    ImageSlide bi;
    public ArrayList<ImageSlide> Slides;
    public SlidesIterator(ArrayList<ImageSlide> slides)
    {
        Slides=slides;
    }
    @Override
    public void addSlides(ImageSlide slide)
    {
        Slides.add(slide);
    }
    @Override
    public boolean hasNext(int mode) {
        if (current < Slides.size()) {
            ImageSlide selected = Slides.get(current);
                try {
                    bi = selected;
                    return true;

                } catch (Exception ex) {
                    System.err.println("Не удалось загрузить картинку! ");
                    ex.printStackTrace();
                    return false;
                }
        }
        else if(mode == 0) preview();
        return true;
    }
    @Override
    public boolean hasPrev(int mode) {
        if (current > -1) {
            ImageSlide selected = Slides.get(current);
            try {
                bi = selected;
                return true;

            } catch (Exception ex) {
                System.err.println("Не удалось загрузить картинку! ");
                ex.printStackTrace();
                return false;
            }
        }
        else if(mode == 0) preview();
        return true;
    }

    @Override
    public Object next() {
        current++;
        if(this.hasNext(0)){
            return bi;
        }
        return null;
    }
    @Override
    public Object prev() {
        current--;
        if(this.hasPrev(0)){
            return bi;
        }
        return null;
    }

    @Override
    public Object moveForward(Canvas canvas)
    {
        current++;
        if(this.hasNext(1))
        {
            ImageSlide temp = new ImageSlide.SlideBuilder(canvas).setCanvas(bi.getGraphic()).setImg(bi.getImage()).setTexts(bi.getTexts()).build();
            replace(Slides.get(current-1),Slides.get(current));
            replace(temp,Slides.get(current-1));
            return bi;
        }
        return null;
    }

    private void replace(ImageSlide orig, ImageSlide dest)
    {
        dest.setCanvas(orig.getGraphic());
        dest.setImg(orig.getImage());
        dest.setTexts(orig.getTexts());
    }

    @Override
    public Object moveBackward(Canvas canvas)
    {
        current--;
        if(this.hasPrev(1))
        {
            ImageSlide temp = new ImageSlide.SlideBuilder(canvas).setCanvas(bi.getGraphic()).setImg(bi.getImage()).setTexts(bi.getTexts()).build();
            replace(Slides.get(current+1),Slides.get(current));
            replace(temp,Slides.get(current+1));
            return bi;
        }
        return null;
    }
    @Override
    public Object preview() {
        current=0;
        if(!this.hasNext(0)) {
            return bi;
        }
        return null;
    }
    @Override
    public int getCurrent()
    {
        return current;
    }
    @Override
    public ImageSlide getCurrentSlide()
    {
        return Slides.get(current);
    }
}
