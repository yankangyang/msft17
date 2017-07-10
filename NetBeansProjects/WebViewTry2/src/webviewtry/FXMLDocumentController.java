/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webviewtry;

import java.io.FileWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;
import javafx.scene.image.ImageView;

/**
 *
 * @author Administrator
 */
public class FXMLDocumentController implements Initializable {
    @FXML private WebView webView;
    private WebEngine engine;
    
    // MATCH 1: Missing Part
    @FXML private TableView<MissingPart> tableViewM1;
    @FXML private TableColumn<MissingPart, Integer> ProductIDColumn;
    @FXML private TableColumn<MissingPart, String>  pNameColumn;
    @FXML private TableColumn<MissingPart, Integer> SupplierIDColumn;
    @FXML private TableColumn<MissingPart, String>  sNameColumn;
    @FXML private TableColumn<MissingPart, Float>   PriceColumn;
    @FXML private TableColumn<MissingPart, Integer> MinColumn;
    @FXML private TableColumn<MissingPart, Integer> MaxColumn;
    
    //MATCH 2: In-Person Meeting
    @FXML private TableView<InPersonMeeting> tableViewM2;
    @FXML private TableColumn<InPersonMeeting, String> ClientCol;
    @FXML private TableColumn<InPersonMeeting, String>  NameCol;
    @FXML private TableColumn<InPersonMeeting, String> SurnameCol;
    @FXML private TableColumn<InPersonMeeting, String>  TitleCol;
    @FXML private TableColumn<InPersonMeeting, String>   RegionCol;
    @FXML private TableColumn<InPersonMeeting, String> CountryCol;
    @FXML private TableColumn<InPersonMeeting, Integer> TerritoryIDCol;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        engine = webView.getEngine();

    // MATCH 1: Missing Part    
    ProductIDColumn.setCellValueFactory(new PropertyValueFactory<MissingPart, Integer>("ProductID"));
    pNameColumn.setCellValueFactory(new PropertyValueFactory<MissingPart, String>("productName"));
    SupplierIDColumn.setCellValueFactory(new PropertyValueFactory<MissingPart, Integer>("BusinessID"));
    sNameColumn.setCellValueFactory(new PropertyValueFactory<MissingPart, String>("supplierName"));
    PriceColumn.setCellValueFactory(new PropertyValueFactory<MissingPart, Float>("Price"));
    MinColumn.setCellValueFactory(new PropertyValueFactory<MissingPart, Integer>("Min"));
    MaxColumn.setCellValueFactory(new PropertyValueFactory<MissingPart, Integer>("Max"));
    tableViewM1.setItems(getDataM1());
    
    
    //MATCH 2: In-Person Meeting
    ClientCol.setCellValueFactory(new PropertyValueFactory<InPersonMeeting, String>("Client"));
    NameCol.setCellValueFactory(new PropertyValueFactory<InPersonMeeting, String>("Name"));
    SurnameCol.setCellValueFactory(new PropertyValueFactory<InPersonMeeting, String>("Surname"));
    TitleCol.setCellValueFactory(new PropertyValueFactory<InPersonMeeting, String>("Title"));
    RegionCol.setCellValueFactory(new PropertyValueFactory<InPersonMeeting, String>("Region"));
    CountryCol.setCellValueFactory(new PropertyValueFactory<InPersonMeeting, String>("Country"));
    TerritoryIDCol.setCellValueFactory(new PropertyValueFactory<InPersonMeeting, Integer>("TerritoryID"));
    tableViewM2.setItems(getDataM2());        
    }
    
    public ObservableList<MissingPart> getDataM1(){
        ObservableList<MissingPart> datalist = FXCollections.observableArrayList();
       
        try{
            // this is the SQL connection code
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://localhost:1433;databaseName=AdventureWorks2014;integratedSecurity=true";
            Connection con = DriverManager.getConnection(url);
            String query1 = " SELECT p.ProductID, p.Name AS 'pName', s.BusinessEntityID, s.Name AS 'sName', h.StandardPrice, h.MinOrderQty, h.MaxOrderQty\n" +
                "  FROM HasInventoryOf h, Supplier s, Product p \n" +
                "  WHERE MATCH (s-(h)->p)\n" +
                "  AND (p.ProductID = '351' OR p.ProductID = '321')";
            
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query1);
            
            MissingPart missingpart = null;
            while(rs.next()){
                missingpart = new MissingPart(rs.getInt("ProductID"), rs.getString("pName"), rs.getInt("BusinessEntityID"), rs.getString("sName"), rs.getFloat("StandardPrice"), rs.getInt("MinOrderQty"), rs.getInt("MaxOrderQty"));
                datalist.add(missingpart);
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }   
        return datalist;
    }
    
        public ObservableList<InPersonMeeting> getDataM2(){
        ObservableList<InPersonMeeting> datalist = FXCollections.observableArrayList();
       
        try{
            // this is the SQL connection code
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://localhost:1433;databaseName=AdventureWorks2014;integratedSecurity=true";
            Connection con = DriverManager.getConnection(url);
            String query1 = "  SELECT c.StoreName AS 'Client', e.FirstName, e.LastName, e.JobTitle AS 'Title', l.Name AS 'Region', l.CountryRegionCode AS 'Country', e.TerritoryID\n" +
                "  FROM dbo.Employee e, dbo.Location l, dbo.LocatedIn lin, dbo.Customer c, dbo.LocatedIn lin2\n" +
                "  WHERE MATCH (e-(lin)->l<-(lin2)-c)  \n" +
                "  AND (c.StoreName = 'Endurance Bikes' OR c.StoreName = 'All Cycle Shop')\n" +
                "  ORDER BY c.StoreName";
            
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query1);
            
            InPersonMeeting row = null;
            while(rs.next()){
                row = new InPersonMeeting(rs.getString("Client"), rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Title"), rs.getString("Region"), rs.getString("Country"), rs.getInt("TerritoryID"));
                datalist.add(row);
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }   
        return datalist;
    }
    
    public void btn1(ActionEvent event){
        //--------------------------------------------
            try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String sql_url = "jdbc:sqlserver://localhost:1433;databaseName=Restaurantdb;integratedSecurity=true";
            Connection con = DriverManager.getConnection(sql_url);
            
            //---------------------------------------------FILEWRITE SETUP
            FileWriter fileWriter = new FileWriter("c:\\Users\\Administrator\\Documents\\msft17\\NetBeansProjects\\WebViewTry\\src\\webviewtry\\sample2.json");
            JSONObject json = new JSONObject();
            JSONArray node_array = new JSONArray();
            JSONArray edge_array = new JSONArray();
            
            //------------------------------------------- QUERIES
            String query1 = "SELECT  c.name, c.stateName\n" +
                "FROM City c, Person p, livesIn lin\n" +
                "WHERE MATCH(p-(lin)->c)\n" +
                "GROUP BY c.name, c.stateName\n" +
                "HAVING count(p.name) > 1";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query1);
            
            while(rs.next()){
                // Get person name, add to JSON Object Node List
                String cityname = rs.getString("name");
                String statename = rs.getString("stateName");
                String cityandstate = cityname + statename;

                JSONObject item = new JSONObject();
                item.put("id", cityandstate);                
                item.put("group", 1);
                node_array.put(item);
                
                // Get ppl who live in this city 
                String query2 = String.format("SELECT p.name \n" +
                    "FROM City c, Person p, livesIn lin\n" +
                    "WHERE MATCH(p-(lin)->c)\n" +
                    "AND c.name = '%s' \n" +
                    "AND c.stateName = '%s'", cityname, statename);
                Statement st2 = con.createStatement();
                ResultSet rs2 = st2.executeQuery(query2);
                
                while(rs2.next()){
                    String person_node = rs2.getString("name");

                        // add node first:
                        JSONObject rest_item = new JSONObject();
                        rest_item.put("id", person_node);                
                        rest_item.put("group", 3);
                        node_array.put(rest_item);
                        
                        // then add edge connecting to it:
                        JSONObject rest_item2 = new JSONObject();
                        rest_item2.put("source", person_node);                
                        rest_item2.put("target", cityandstate);
                        rest_item2.put("value", 10);
                        edge_array.put(rest_item2);
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
        
        URL url = getClass().getResource("index.html");
        engine.load(url.toExternalForm());
    }

    public void btn3(ActionEvent event){
        
    try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String sql_url = "jdbc:sqlserver://localhost:1433;databaseName=Restaurantdb;integratedSecurity=true";
            Connection con = DriverManager.getConnection(sql_url);
            
            //---------------------------------------------FILEWRITE SETUP
            FileWriter fileWriter = new FileWriter("c:\\Users\\Administrator\\Documents\\msft17\\NetBeansProjects\\WebViewTry\\src\\webviewtry\\sample2.json");
            JSONObject json = new JSONObject();
            JSONArray node_array = new JSONArray();
            JSONArray edge_array = new JSONArray();
            
            //------------------------------------------- QUERIES
            String query1 = "SELECT  c.name, c.stateName\n" +
                "FROM City c, Person p, livesIn lin\n" +
                "WHERE MATCH(p-(lin)->c)\n" +
                "GROUP BY c.name, c.stateName\n" +
                "HAVING count(p.name) > 1";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query1);
            
            while(rs.next()){
                // Get person name, add to JSON Object Node List
                String cityname = rs.getString("name");
                String statename = rs.getString("stateName");
                String cityandstate = cityname + statename;

                JSONObject item = new JSONObject();
                item.put("id", cityandstate);                
                item.put("group", 1);
                node_array.put(item);
                
                // Get ppl who live in this city 
                String query2 = String.format("SELECT p.name \n" +
                    "FROM City c, Person p, livesIn lin\n" +
                    "WHERE MATCH(p-(lin)->c)\n" +
                    "AND c.name = '%s' \n" +
                    "AND c.stateName = '%s'", cityname, statename);
                Statement st2 = con.createStatement();
                ResultSet rs2 = st2.executeQuery(query2);
                
                while(rs2.next()){
                    String person_node = rs2.getString("name");

                        // add node first:
                        JSONObject rest_item = new JSONObject();
                        rest_item.put("id", person_node);                
                        rest_item.put("group", 3);
                        node_array.put(rest_item);
                        
                        // then add edge connecting to it:
                        JSONObject rest_item2 = new JSONObject();
                        rest_item2.put("source", person_node);                
                        rest_item2.put("target", cityandstate);
                        rest_item2.put("value", 10);
                        edge_array.put(rest_item2);
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

        // show the diagram
        URL url = getClass().getResource("index2.html");
        engine.load(url.toExternalForm());
      
    }
    public void btn4(ActionEvent event){
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
            //String[] ppl_list = new String[num_ppl];
            String[] rest_list = new String[num_rest];
            String[] city_list = new String[num_cities];
            
            //indicies for next available node
            //int ppl_ind = 0;
            int rest_ind = 0;
            int city_ind = 0;
         
            //---------------------------------------------FILEWRITE SETUP
            FileWriter fileWriter = new FileWriter("c:\\Users\\Administrator\\Documents\\msft17\\NetBeansProjects\\WebViewTry\\src\\webviewtry\\sample.json");
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
        
        
        // show the diagram
        URL url = getClass().getResource("index3.html");
        engine.load(url.toExternalForm());
    }
    
    public void btn5(ActionEvent event){
        URL url = getClass().getResource("nativejs.html");
        engine.load(url.toExternalForm());
    }
    
    public void btn6(ActionEvent event){
        URL url = getClass().getResource("D3Sticky.html");
        webView.getEngine().load(url.toExternalForm());
    }
    
    public void MATCH1DatatoJSON(ActionEvent event){
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String sql_url = "jdbc:sqlserver://localhost:1433;databaseName=AdventureWorks2014;integratedSecurity=true";
            Connection con = DriverManager.getConnection(sql_url);
            
            //---------------------------------------------FILEWRITE SETUP
            FileWriter fileWriter = new FileWriter("c:\\Users\\Administrator\\Documents\\msft17\\NetBeansProjects\\WebViewTry2\\src\\webviewtry\\match1.json");
            JSONObject json = new JSONObject();
            JSONArray node_array = new JSONArray();
            JSONArray edge_array = new JSONArray();
            
            //------------------------------------------- QUERIES
            String query1 = "  SELECT p.Name\n" +
                "  FROM HasInventoryOf h, Supplier s, Product p \n" +
                "  WHERE MATCH (s-(h)->p)\n" +
                "  AND (p.ProductID = '351' OR p.ProductID = '321')\n" +
                "  GROUP BY p.Name";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query1);
            int part_counter = 0;
            int supplier_counter = 0;
            
            while(rs.next()){
     
                // Get PART, add to JSON Object Node List
                String part_node = rs.getString("Name");

                JSONObject item = new JSONObject();
                item.put("name", part_node);                
                item.put("type", "part");
                node_array.put(item);
                
                supplier_counter++;
                
                // Get supplier to connect
                String query2 = String.format("  SELECT s.Name AS 'sName'\n" +
                "  FROM HasInventoryOf h, Supplier s, Product p \n" +
                "  WHERE MATCH (s-(h)->p)\n" +
                "  AND p.Name = '%s'\n" +
                "  GROUP BY s.Name", part_node);

                Statement st2 = con.createStatement();
                ResultSet rs2 = st2.executeQuery(query2);
                
                while(rs2.next()){
                    
                    
                    String supplier_node = rs2.getString("sName");
        
                        // add node first:
                        JSONObject supplier_item = new JSONObject();
                        supplier_item.put("name", supplier_node);                
                        supplier_item.put("type", "supplier");
                        node_array.put(supplier_item);
                        
                        // then add edge connecting to it:
                        JSONObject rest_item2 = new JSONObject();
                        rest_item2.put("source", supplier_counter);                
                        rest_item2.put("target", part_counter);
                        rest_item2.put("type", "hasInvOf");
                        edge_array.put(rest_item2);
                    
                    supplier_counter++;
                    }
                part_counter = supplier_counter;
                }
                
            json.put("nodes", node_array);
            json.put("links", edge_array);
            

            fileWriter.write(json.toString());
            fileWriter.close();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    
    public void MATCH2DatatoJSON(ActionEvent event){
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String sql_url = "jdbc:sqlserver://localhost:1433;databaseName=AdventureWorks2014;integratedSecurity=true";
            Connection con = DriverManager.getConnection(sql_url);
            
            //---------------------------------------------FILEWRITE SETUP
            FileWriter fileWriter = new FileWriter("c:\\Users\\Administrator\\Documents\\msft17\\NetBeansProjects\\WebViewTry2\\src\\webviewtry\\match2.json");
            JSONObject json = new JSONObject();
            JSONArray node_array = new JSONArray();
            JSONArray edge_array = new JSONArray();
            
            //------------------------------------------- QUERIES
            String query1 = " SELECT l.Name AS 'Region', l.CountryRegionCode AS 'Country'\n" +
                "  FROM dbo.Employee e, dbo.Location l, dbo.LocatedIn lin, dbo.Customer c, dbo.LocatedIn lin2\n" +
                "  WHERE MATCH (e-(lin)->l<-(lin2)-c)  \n" +
                "  AND (c.StoreName = 'Endurance Bikes' OR c.StoreName = 'All Cycle Shop')\n" +
                "  GROUP BY l.Name , l.CountryRegionCode";
            
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query1);
            int location_counter = 0;
            int dependent_counter = 0;
            
            while(rs.next()){
     
                // Get Location, add to JSON Object Node List
                String region = rs.getString("Region");
                String country = rs.getString("Country");
                String location_node = region + " " + country;

                JSONObject item = new JSONObject();
                item.put("name", location_node);                
                item.put("type", "location");
                node_array.put(item);
                
                dependent_counter++;
                
                // Add the Customer in concern to location
                String query2 = String.format(" SELECT c.StoreName\n" +
                "  FROM dbo.Employee e, dbo.Location l, dbo.LocatedIn lin, dbo.Customer c, dbo.LocatedIn lin2\n" +
                "  WHERE MATCH (e-(lin)->l<-(lin2)-c)  \n" +
                "  AND (c.StoreName = 'Endurance Bikes' OR c.StoreName = 'All Cycle Shop')" +
                "  AND l.Name = '%s'\n" +
                "  GROUP BY c.StoreName", region);

                Statement st2 = con.createStatement();
                ResultSet rs2 = st2.executeQuery(query2);
                
                while(rs2.next()){
                    
                    String customer_node = rs2.getString("StoreName");
        
                        // add node first:
                        JSONObject customer_item = new JSONObject();
                        customer_item.put("name", customer_node);                
                        customer_item.put("type", "customer");
                        node_array.put(customer_item);
                        
                        // then add edge connecting to it:
                        JSONObject rest_item2 = new JSONObject();
                        rest_item2.put("source", dependent_counter);                
                        rest_item2.put("target", location_counter);
                        rest_item2.put("type", "LocatedIn");
                        edge_array.put(rest_item2);
                    
                    dependent_counter++;
                    }
                
                // add the employees
                String query3 = String.format(" SELECT e.FirstName, e.LastName\n" +
                "  FROM dbo.Employee e, dbo.Location l, dbo.LocatedIn lin, dbo.Customer c, dbo.LocatedIn lin2\n" +
                "  WHERE MATCH (e-(lin)->l<-(lin2)-c)  \n" +
                "  AND (c.StoreName = 'Endurance Bikes' OR c.StoreName = 'All Cycle Shop')" +
                "  AND l.Name = '%s'\n", region);

                Statement st3 = con.createStatement();
                ResultSet rs3 = st3.executeQuery(query3);
                
                while(rs3.next()){
                   
                    String name = rs3.getString("FirstName") + " " + rs3.getString("LastName");
        
                        // add node first:
                        JSONObject supplier_item = new JSONObject();
                        supplier_item.put("name", name);                
                        supplier_item.put("type", "employee");
                        node_array.put(supplier_item);
                        
                        // then add edge connecting to it:
                        JSONObject rest_item2 = new JSONObject();
                        rest_item2.put("source", dependent_counter);                
                        rest_item2.put("target", location_counter);
                        rest_item2.put("type", "LocatedIn");
                        edge_array.put(rest_item2);
                    
                    dependent_counter++;
                    }
                
                location_counter = dependent_counter;
                }
            
                
            json.put("nodes", node_array);
            json.put("links", edge_array);
            

            fileWriter.write(json.toString());
            fileWriter.close();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    

}
