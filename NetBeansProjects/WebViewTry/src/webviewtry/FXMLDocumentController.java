/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webviewtry;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.swing.JOptionPane;



/**
 *
 * @author Administrator
 */
public class FXMLDocumentController implements Initializable {
    @FXML private WebView webView;
    private WebEngine engine;
    @FXML
    private Label label;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        engine = webView.getEngine();
    }

    public void btn1(ActionEvent event){
        engine.load("https://www.google.com");
    }

    public void btn3(ActionEvent event){
        engine.loadContent("<html><body><h1>Hello World</h1><body></html>");
    }
    public void btn4(ActionEvent event){
        URL url = getClass().getResource("index3.html");
        engine.load(url.toExternalForm());
    }
    
    public void btn5(ActionEvent event){
        URL url = getClass().getResource("nativejs.html");
        engine.load(url.toExternalForm());
    }

}
