/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package d3test;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 *
 * @author Administrator
 */
public class FXMLDocumentController implements Initializable {
    

    @FXML private WebView webView;
    private WebEngine engine;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        engine = webView.getEngine();
    }    
    
    public void btn5(ActionEvent event){
        URL url = getClass().getResource("D3Sticky.html");
        engine.load(url.toExternalForm());
    }
    
    public void btn1(ActionEvent event){
        URL url = getClass().getResource("match2.html");
        engine.load(url.toExternalForm());
    }
    
}
