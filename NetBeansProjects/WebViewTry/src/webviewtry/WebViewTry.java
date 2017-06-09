/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webviewtry;

import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.Object;

/**
 *
 * @author Administrator
 */
public class WebViewTry extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
        
        
        //-----------------------------------
            
        //URL url = getClass().getResource("index2.html");
        //engine.load(url.toExternalForm());
        //System.out.println("Button Recognized\n");
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String sql_url = "jdbc:sqlserver://localhost:1433;databaseName=Restaurantdb;integratedSecurity=true";
            Connection con = DriverManager.getConnection(sql_url);
            
            //-------------------------------------- LIST SETUP
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
            
            String query_cities = "SELECT count(City.name) AS 'city' FROM City";
            Statement st_cities = con.createStatement();
            ResultSet rs_cities = st_cities.executeQuery(query_cities);
            if(!rs_cities.next()){
                System.out.printf("Result cities set empty\n");
            }
            int num_cities = rs_cities.getInt("city");
            
            //create arrays for each with size specified in query
            String[] ppl_list = new String[num_ppl];
            String[] rest_list = new String[num_rest];
            String[] city_list = new String[num_cities];
            
            //indicies for next available node
            int ppl_ind = 0;
            int rest_ind = 0;
            int city_ind = 0;
         
            //---------------------------------------------FILEWRITE SETUP
            FileWriter fileWriter = new FileWriter("c:\\Users\\Administrator\\Documents\\NetBeansProjects\\WebViewTry\\src\\webviewtry\\sample.json");
            JSONObject json = new JSONObject();
            JSONArray node_array = new JSONArray();
            JSONArray edge_array = new JSONArray();
            
            //------------------------------------------- QUERIES
            String query1 = "SELECT Person.name FROM Person";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query1);
            
            while(rs.next()){
                // Get person name, add to JSON Object Node List
                String person_node = rs.getString("name");

                JSONObject item = new JSONObject();
                item.put("id", person_node);                
                item.put("group", 1);
                node_array.put(item);
                
                // Get the restaurants that this person likes
                String query2 = String.format("SELECT r.name, r.city\n" + 
                    "FROM Person p, likes l , restaurant r\n" +
                    "WHERE MATCH (p-(l)->r)\n" +
                    "AND p.name = '%s'\n", person_node);
                Statement st2 = con.createStatement();
                ResultSet rs2 = st2.executeQuery(query2);
                
                while(rs2.next()){
                    String restaurant_node = rs2.getString("name")+ " " + rs2.getString("city");
                    // if restaurant is already in list, just add a link
                    if((rest_ind != 0) && SearchList.ListSearch(restaurant_node, rest_list)){
                        
                        //edge package:
                        JSONObject rest_item = new JSONObject();
                        rest_item.put("source", person_node);                
                        rest_item.put("target", restaurant_node);
                        rest_item.put("value", 10);
                        edge_array.put(rest_item);
                    }
                    // otherwise add a link and a node
                    else {
                        // add to array:
                        rest_list[rest_ind] = restaurant_node;
                        rest_ind++;
                        // add node first:
                        JSONObject rest_item = new JSONObject();
                        rest_item.put("id", restaurant_node);                
                        rest_item.put("group", 2);
                        node_array.put(rest_item);
                        
                        // then add edge connecting to it:
                        JSONObject rest_item2 = new JSONObject();
                        rest_item2.put("source", person_node);                
                        rest_item2.put("target", restaurant_node);
                        rest_item2.put("value", 10);
                        edge_array.put(rest_item2);
                        
                   }
                }
                
                String query3 = String.format("SELECT c.name, c.stateName\n" + 
                    "FROM City c, Person p, livesIn lin\n" +
                    "WHERE MATCH (p-(lin)->c)\n" +
                    "AND p.name = '%s'\n", person_node);
                Statement st3 = con.createStatement();
                ResultSet rs3 = st3.executeQuery(query3);
                
                while(rs3.next()){
                    String city_node= rs3.getString("name")+ rs3.getString("stateName");
                    if((city_ind != 0) && SearchList.ListSearch(city_node, city_list)){
                        
                        //edge package:
                        JSONObject rest_item = new JSONObject();
                        rest_item.put("source", person_node);                
                        rest_item.put("target", city_node);
                        rest_item.put("value", 10);
                        edge_array.put(rest_item); 
                    }
                    else {
                        
                        // add to array:
                        city_list[city_ind] = city_node;
                        city_ind++;
                        
                        // add node first:
                        JSONObject rest_item = new JSONObject();
                        rest_item.put("id", city_node);                
                        rest_item.put("group", 3);
                        node_array.put(rest_item);
                        
                        // then add edge connecting to it:
                        JSONObject rest_item2 = new JSONObject();
                        rest_item2.put("source", person_node);                
                        rest_item2.put("target", city_node);
                        rest_item2.put("value", 10);
                        edge_array.put(rest_item2);      
                    }
                }
                
                json.put("nodes", node_array);
                json.put("links", edge_array);
            }

            fileWriter.write(json.toString());
            fileWriter.close();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
   
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
