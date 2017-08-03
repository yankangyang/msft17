package com.fxgraph.cells;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import com.fxgraph.graph.Cell;
import com.fxgraph.graph.SetColor;

public class RectangleCell extends Cell {

    public RectangleCell(String id, SetColor nodeColor) {
        super(id);

        Rectangle view = new Rectangle(50,50);
        
        switch (nodeColor) {
            case RED:
                view.setStroke(Color.RED);
                view.setFill(Color.RED);
                setView(view); 
                break;
            case PURPLE:
                view.setStroke(Color.PURPLE);
                view.setFill(Color.PURPLE);
                setView(view); 
                break;
            case DODGERBLUE:
                view.setStroke(Color.DODGERBLUE);
                view.setFill(Color.DODGERBLUE);
                setView(view); 
                break;
            default:
            throw new UnsupportedOperationException("Unsupported Color:" + nodeColor);
        }    
                 
    }
    
}