package com.fxgraph.cells;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import com.fxgraph.graph.Cell;
import com.fxgraph.graph.SetColor;

public class TriangleCell extends Cell {

    public TriangleCell(String id, SetColor nodeColor) {
        super(id);

        double width = 50;
        double height = 50;

        Polygon view = new Polygon( width / 2, 0, width, height, 0, height);
        
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