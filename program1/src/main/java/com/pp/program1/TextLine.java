package com.pp.program1;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
import java.util.ArrayList;


public class TextLine extends Group implements Serializable {

    transient public ArrayList<Dragger> listeners = new ArrayList<>();

    transient protected TextField blockText;
    public String text;
    protected double width;
    protected double height;
    transient protected Point2D point;
    public double pX;
    public double pY;
    private double mouseAnchorX;
    private double mouseAnchorY;

    public TextLine(Point2D point, String text)
    {
        this.text=text;
        pX=point.getX();
        pY=point.getY();
        setTranslateX(point.getX());
        setTranslateY(point.getY());

        blockText = new TextField(text);
        blockText.setOnKeyTyped(e -> this.text=blockText.getText());
        blockText.setOnKeyReleased(e -> this.text=blockText.getText());
        blockText.setStyle("-fx-focus-color: -fx-control-inner-background ; -fx-border-color: transparent; -fx-background-color:transparent;  -fx-faint-focus-color: -fx-control-inner-background ;");
        blockText.setAlignment(Pos.BASELINE_CENTER);

        this.point = point;
        pX = this.point.getX();
        pY = this.point.getY();
        this.width = computePrefWidth(-1);
        this.height = computePrefHeight(-1);


        getChildren().add(blockText);

        setOnMousePressed(mouseEvent -> {
            mouseAnchorX = mouseEvent.getX();
            mouseAnchorY = mouseEvent.getY();
        });

        setOnMouseDragged(mouseEvent -> {
            setTranslateX(Math.max(getTranslateX() + mouseEvent.getX() - mouseAnchorX, 0));
            setTranslateY(Math.max(getTranslateY() + mouseEvent.getY() - mouseAnchorY, 0));

            this.point = new Point2D(getTranslateX(), getTranslateY());
            pX=this.point.getX();
            pY=this.point.getY();
            for (Dragger listener : listeners) {
                listener.onDrag();
            }

        });
    }

    public void draw()
    {
        float offset = 10f;
        blockText.applyCss();
        blockText.layout();

        getChildren().clear();

        double textWidth = blockText.prefWidth(-1);
        double textHeight = blockText.prefHeight(-1);

        Rectangle rect = new Rectangle(
                0,
                0,
                textWidth + offset,
                textHeight + offset);

        point = new Point2D(getTranslateX(), getTranslateY());
        width = rect.prefWidth(-1);
        height = rect.prefHeight(-1);

        rect.setFill(Color.TRANSPARENT);

        getChildren().add(rect);
        getChildren().add(blockText);

        blockText.setTranslateX(offset * 0.5f);
        blockText.setTranslateY(offset * 0.5f);
    }

}
