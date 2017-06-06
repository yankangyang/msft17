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

            //get the # of people, cities, and restaurants in the database
            String query_ppl = "SELECT count(Person.name) AS 'ppl' FROM Person";
            Statement st_ppl = con.createStatement();
            ResultSet rs_ppl = st_ppl.executeQuery(query_ppl);
            if(!rs_ppl.next()){
                System.out.printf("Result rsppl set empty\n");
            }
            int num_ppl = rs_ppl.getInt("ppl");
            //System.out.printf("NUM PPL: %d%n\n", num_ppl);

            String query_rest = "SELECT count(Restaurant.name) AS 'rest' FROM Restaurant";
            Statement st_rest = con.createStatement();
            ResultSet rs_rest = st_rest.executeQuery(query_rest);
            if(!rs_rest.next()){
                System.out.printf("Result rsrest set empty\n");
            }
            int num_rest = rs_rest.getInt("rest");
            //System.out.printf("NUM REST: %d%n\n", num_rest);
            
            String query_cities = "SELECT count(City.name) AS 'city' FROM City";
            Statement st_cities = con.createStatement();
            ResultSet rs_cities = st_cities.executeQuery(query_cities);
            if(!rs_cities.next()){
                System.out.printf("Result cities set empty\n");
            }
            int num_cities = rs_cities.getInt("city");
            //System.out.printf("NUM CITIES: %d%n\n", num_cities);
            
            //create arrays for each with size specified in query
            String[] ppl_list = new String[num_ppl];
            String[] rest_list = new String[num_rest];
            String[] city_list = new String[num_cities];
            
            //indicies for next available node
            int ppl_ind = 0;
            int rest_ind = 0;
            int city_ind = 0;
            
            Model model = graph.getModel();
            graph.beginUpdate();
            
            // add Person node
            while(rs.next()){
                String person_node = rs.getString("name");
                System.out.printf(person_node);
                
                ppl_list[ppl_ind] = person_node;
                ppl_ind++;
                
                model.addCell(person_node, CellType.TRIANGLE, SetColor.RED);
                
                // likes restaurant 
                String query2 = String.format("SELECT r.name, r.city\n" + 
                    "FROM Person p, likes l , restaurant r\n" +
                    "WHERE MATCH (p-(l)->r)\n" +
                    "AND p.name = '%s'\n", person_node);
                Statement st2 = con.createStatement();
                ResultSet rs2 = st2.executeQuery(query2);
                
                while(rs2.next()){
                    String restaurant_node = rs2.getString("name")+ " " + rs2.getString("city");
                    //System.out.printf("Restaurant Adding: " + restaurant_node + " \n");
                    if((rest_ind != 0) && SearchList.ListSearch(restaurant_node, rest_list)){
                        //System.out.printf("Matched Existing\n");
                        model.addEdge(person_node, restaurant_node);   
                    }
                    else {
                        //System.out.printf("New Node\n");
                        rest_list[rest_ind] = restaurant_node;
                        rest_ind++;
                   
                        model.addCell(restaurant_node, CellType.RECTANGLE, SetColor.DODGERBLUE);
                        model.addEdge(person_node, restaurant_node);  
                   }
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
                    if((city_ind != 0) && SearchList.ListSearch(city_node, city_list)){
                        model.addEdge(person_node, city_node);   
                    }
                    else {
                        city_list[city_ind] = city_node;
                        city_ind++;
                   
                        model.addCell(city_node, CellType.RECTANGLE, SetColor.PURPLE);
                        model.addEdge(person_node, city_node);         
                    }
                }
            }
            
            //model.addEdge("Joe", "Diana");
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
