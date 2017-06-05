/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphtest;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import com.fxgraph.graph.CellType;
import com.fxgraph.graph.Graph;
import com.fxgraph.graph.Model;
import com.fxgraph.layout.base.Layout;
import com.fxgraph.layout.random.RandomLayout;
import com.fxgraph.graph.SetColor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class GraphMain extends Application {
  
    Graph graph = new Graph();
    
    @Override
    public void start(Stage primaryStage) {

        BorderPane root = new BorderPane();

        graph = new Graph();

        root.setCenter(graph.getScrollPane());

        Scene scene = new Scene(root, 1024, 768);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        
        primaryStage.setScene(scene);
        primaryStage.show();
       
            //------------------------------------------------    
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://localhost:1433;databaseName=Restaurantdb;integratedSecurity=true";
            Connection con = DriverManager.getConnection(url);
        
            String query1 = "SELECT Person.name FROM Person";
            
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query1);
           
            Model model = graph.getModel();
            graph.beginUpdate();
            
            // add Person node
            while(rs.next()){
                String person_node = rs.getString("name");
                //String pnodecolor = "RED";
                model.addCell(person_node, CellType.TRIANGLE, SetColor.RED);
                
                // likes restaurant 
                String query2 = String.format("SELECT r.name, r.city\n" + 
                    "FROM Person p, likes l , restaurant r\n" +
                    "WHERE MATCH (p-(l)->r)\n" +
                    "AND p.name = '%s'\n", person_node);
                Statement st2 = con.createStatement();
                ResultSet rs2 = st2.executeQuery(query2);
                
                while(rs2.next()){
                   String restaurant_node = rs2.getString("name")+ rs2.getString("city");
                   //String nodecolor = "DODGERBLUE";
                   model.addCell(restaurant_node, CellType.RECTANGLE, SetColor.DODGERBLUE);
                   model.addEdge(person_node, restaurant_node);                   
                }
                
                // lives in city
                String query3 = String.format("SELECT c.name, c.stateName\n" + 
                    "FROM City c, Person p, livesIn lin\n" +
                    "WHERE MATCH (p-(lin)->c)\n" +
                    "AND p.name = '%s'\n", person_node);
                Statement st3 = con.createStatement();
                ResultSet rs3 = st3.executeQuery(query3);
                
                while(rs3.next()){
                   String city_node= rs3.getString("name")+ rs3.getString("stateName");
                   //String nodecolor = "PURPLE";
                   model.addCell(city_node, CellType.RECTANGLE, SetColor.PURPLE);
                   model.addEdge(person_node, city_node);                   
                }
                
  
               //model.addEdge("Joe", "Diana"); 
            }
            graph.endUpdate();
            
            con.close();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    //-------------------------------------------------
        //addGraphComponents();

        Layout layout = new RandomLayout(graph);
        layout.execute();

    }
/*
    private void addGraphComponents() {

        Model model = graph.getModel();

        graph.beginUpdate();

        model.addCell("Cell A", CellType.RECTANGLE);
        model.addCell("Cell B", CellType.RECTANGLE);
        model.addCell("Cell C", CellType.RECTANGLE);
        model.addCell("Cell D", CellType.TRIANGLE);
        model.addCell("Cell E", CellType.TRIANGLE);
        model.addCell("Cell F", CellType.RECTANGLE);
        model.addCell("Cell G", CellType.RECTANGLE);

        model.addEdge("Cell A", "Cell B");
        model.addEdge("Cell A", "Cell C");
        model.addEdge("Cell B", "Cell C");
        model.addEdge("Cell C", "Cell D");
        model.addEdge("Cell B", "Cell E");
        model.addEdge("Cell D", "Cell F");
        model.addEdge("Cell D", "Cell G");

        graph.endUpdate();

    }
*/
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
