/*
 * This application serves as a demonstration of end-to-end development of 
 * visualizations from SQL Graph database to D3.js using Java/JavaFXML as a 
 * server-side client. 
 */
package webviewtry;

import java.io.FileWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;

// Declare Webviews and Table (UI of Java Applicaiton)
public class FXMLDocumentController implements Initializable {
    //WEB ENGINE:
    @FXML private WebView webViewIPM;
    @FXML private WebView webViewPR;
    @FXML private WebView webViewBOM;
    private WebEngine engineIPM;
    private WebEngine enginePR;
    private WebEngine engineBOM;
    
    //TABLE:
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
        // initialize webviews for visualizing graph db in Java application
        engineIPM = webViewIPM.getEngine();
        enginePR = webViewPR.getEngine();
        engineBOM = webViewBOM.getEngine();
    
        // initialize table columns for In-Person Meeting scenario (live-table demonstration)
        ClientCol.setCellValueFactory(new PropertyValueFactory<InPersonMeeting, String>("Client"));
        NameCol.setCellValueFactory(new PropertyValueFactory<InPersonMeeting, String>("Name"));
        SurnameCol.setCellValueFactory(new PropertyValueFactory<InPersonMeeting, String>("Surname"));
        TitleCol.setCellValueFactory(new PropertyValueFactory<InPersonMeeting, String>("Title"));
        RegionCol.setCellValueFactory(new PropertyValueFactory<InPersonMeeting, String>("Region"));
        CountryCol.setCellValueFactory(new PropertyValueFactory<InPersonMeeting, String>("Country"));
        TerritoryIDCol.setCellValueFactory(new PropertyValueFactory<InPersonMeeting, Integer>("TerritoryID"));
        tableViewM2.setItems(getDataM2());        
    }
    
    // In-Person Meeting Table: Connect to SQL Server and get data
    public ObservableList<InPersonMeeting> getDataM2(){
        ObservableList<InPersonMeeting> datalist = FXCollections.observableArrayList();
       
        try{
            // Use JDBC driver for Java-SQL Server Interfacing
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            // Declare connection string using TCP/IP POrt 1433
            String url = "jdbc:sqlserver://localhost:1433;databaseName=AdventureWorks2014;integratedSecurity=true";
            // Connect
            Connection con = DriverManager.getConnection(url);
            // Declare SQL Query
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
    
    public void LaunchIPM(ActionEvent event){
        URL url = getClass().getResource("Graph_IPM.html");
        engineIPM.load(url.toExternalForm());
    }
    
    public void LaunchPR(ActionEvent event){
        URL url = getClass().getResource("Graph_PR.html");
        enginePR.load(url.toExternalForm());
    }
        
    public void LaunchBOM(ActionEvent event){
        URL url = getClass().getResource("Graph_BOM.html");
        engineBOM.load(url.toExternalForm());
    }
    
    public void MATCHMeetingtoJSON(ActionEvent event){
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
    public void MATCHParttoJSON(ActionEvent event){
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String sql_url = "jdbc:sqlserver://localhost:1433;databaseName=AdventureWorks2014;integratedSecurity=true";
            Connection con = DriverManager.getConnection(sql_url);
            
            //---------------------------------------------FILEWRITE SETUP
            FileWriter fileWriter = new FileWriter("c:\\Users\\Administrator\\Documents\\msft17\\NetBeansProjects\\WebViewTry2\\src\\webviewtry\\match3.json");
            JSONObject json = new JSONObject();
            JSONArray node_array = new JSONArray();
            JSONArray edge_array = new JSONArray();
            
            //------------------------------------------- QUERIES
            
            // LOCATION = 4 SINGLE NODE: 
            JSONObject location = new JSONObject();
            location.put("name", "Southwest US");                
            location.put("type", "location");
            node_array.put(location);
            
            String query1 = "   SELECT s.BusinessEntityID, s.Name\n" +
                "  FROM dbo.Supplier s, dbo.SuppliedFrom sfrom, dbo.Product p\n" +
                "  WHERE MATCH (p-(sfrom)->s)\n" +
                "  AND s.TerritoryID = 4\n" +
                "  AND sfrom.ShipDate BETWEEN '2013-08-13 00:00:00.000' AND '2013-08-22 00:00:00.000'\n" +
                "  GROUP BY s.Name, s.BusinessEntityID";
            
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query1);
            int location_counter = 0;
            int supplier_counter = 0;
            int part_counter = 0;
            int assembly1_counter = 0;
            int assembly2_counter = 0;
            
            // ARRAYS
            int[] product_list = new int[5000];
            int[] edge_list = new int[5000];
            int list_ind = 0;
            
            while(rs.next()){
                // Add Suppliers

                String supplier_node = rs.getString("Name");
                
                supplier_counter++;
                //(
                part_counter++;
                assembly1_counter++;
                assembly2_counter++;
                //)
                JSONObject item = new JSONObject();
                item.put("name", supplier_node);                
                item.put("type", "supplier");
                node_array.put(item);
                
                // then add edge connecting to it:
                JSONObject rest_item2 = new JSONObject();
                rest_item2.put("source", supplier_counter);                
                rest_item2.put("target", location_counter);
                rest_item2.put("type", "LocatedIn");
                edge_array.put(rest_item2);
                
                System.out.printf("Supplier added: %s -> %d\n", supplier_node, supplier_counter);
                    
                // Add the associated products for each supplier
                String query2 = String.format("  SELECT p.ProductID, p.Name, s.BusinessEntityID, s.Name AS 'sName'\n" +
                    "  FROM dbo.Supplier s, dbo.SuppliedFrom sfrom, dbo.Product p\n" +
                    "  WHERE MATCH (p-(sfrom)->s)\n" +
                    "  AND s.TerritoryID = 4\n" +
                    "  AND sfrom.ShipDate BETWEEN '2013-08-13 00:00:00.000' AND '2013-08-22 00:00:00.000'\n" +
                    "  AND p.Name NOT LIKE '%%nut%%' \n" +
                    "  AND p.Name NOT LIKE '%%bolt%%'\n" +
                    "  AND p.Name NOT LIKE '%%lock%%'\n" +
                    "  AND s.Name = '%s'\n" +
                    "  GROUP BY p.ProductID, p.Name, s.BusinessEntityID, s.Name", supplier_node);

                Statement st2 = con.createStatement();
                ResultSet rs2 = st2.executeQuery(query2);
                
                while(rs2.next()){
                    
                    part_counter++;
                    //(
                    assembly1_counter++;
                    assembly2_counter++;
                    //)
                    String part_node = rs2.getString("Name");
                    int assembly_id = rs2.getInt("ProductID");
                    
                    // add node first:
                    JSONObject part_item = new JSONObject();
                    part_item.put("name", part_node);                
                    part_item.put("type", "part");
                    node_array.put(part_item);

                    // then add edge connecting to it:
                    JSONObject part_edge = new JSONObject();
                    part_edge.put("source", part_counter);                
                    part_edge.put("target", supplier_counter);
                    part_edge.put("type", "SuppliedFrom");
                    edge_array.put(part_edge);
                    
                    System.out.printf("   Part added: %s -> %d\n", part_node, part_counter);
                    
                    if (assembly_id == 938 || assembly_id == 939){
                        // level 2 product, just add once
                        
                        // Add the associated products for each supplier
                        String query3 = String.format(" SELECT p2.ProductID, p2.Name\n" +
                            " FROM dbo.Product p1, dbo.Product p2, dbo.IsPartOf partOf\n" +
                            " WHERE MATCH (p1-(partOf)->p2)\n" +
                            " AND p1.ProductID = '%d'\n" +
                            " ORDER BY p2.ProductID, p2.Name", assembly_id);
                        
                        Statement st3 = con.createStatement();
                        ResultSet rs3 = st3.executeQuery(query3);
                        
                        while(rs3.next()){
                            
                            String assembly1_node = rs3.getString("Name");
                            int assembly1_pid = rs3.getInt("ProductID");
                            
                            int nodeCheck = SearchIDList.ListSearch(assembly1_pid, product_list);
                            // if the node is already in the array, only add the edge
                            /*
                            if ((list_ind != 0) && (nodeCheck >= 0)){
                                System.out.printf("   DETECTED DUPLICATE %d, %d\n", assembly1_pid, edge_list[nodeCheck]);
                                // then add edge connecting to it: from edge_list[nodeCheck]
                                JSONObject assembly1_edge = new JSONObject();
                                assembly1_edge.put("source", assembly1_counter);                
                                assembly1_edge.put("target", edge_list[nodeCheck]);
                                assembly1_edge.put("type", "IsPartOf");
                                edge_array.put(assembly1_edge);
                            }
                            else{
                            */
                                assembly1_counter++;
                            
                                //(
                                assembly2_counter++;
                                //)
                                // add node first:
                                JSONObject assembly1_item = new JSONObject();
                                assembly1_item.put("name", assembly1_node);                
                                assembly1_item.put("type", "L1Assembly");
                                node_array.put(assembly1_item);

                                // then add edge connecting to it:
                                JSONObject assembly1_edge = new JSONObject();
                                assembly1_edge.put("source", part_counter);                
                                assembly1_edge.put("target", assembly1_counter);
                                assembly1_edge.put("type", "IsPartOf");
                                edge_array.put(assembly1_edge);
                                
                                product_list[list_ind] = assembly1_pid;
                                edge_list[list_ind] = assembly1_counter;
                                
                                list_ind++;
                                
                                System.out.printf("      Assembly1 added: %s -> %d, %d\n", assembly1_node, assembly1_counter, assembly1_pid);
                            //}
                            
                        }
                        
                    }
                    
                    else {
                        
                        // Add the associated products for each supplier
                        String query4 = String.format("   SELECT p2.ProductID, p2.Name\n" +
                            " FROM dbo.Product p1, dbo.Product p2, dbo.IsPartOf partOf1\n" +
                            " WHERE MATCH (p1-(partOf1)->p2)\n" +
                            " AND p1.ProductID = '%d'\n" +
                            " ORDER BY p2.ProductID, p2.Name", assembly_id);
                        
                        Statement st4 = con.createStatement();
                        ResultSet rs4 = st4.executeQuery(query4);
                        
                        while(rs4.next()){
                            String assembly2_node = rs4.getString("Name");
                            int L1_assembly_id = rs4.getInt("ProductID");
                            
                            int nodeCheck2 = SearchIDList.ListSearch(L1_assembly_id, product_list);
                            
                            /*if ((list_ind != 0) && (nodeCheck2 >= 0)){
                                System.out.printf("   DETECTED DUPLICATE %d, %d\n", L1_assembly_id, edge_list[nodeCheck2]);
                                // then add edge connecting to it: from edge_list[nodeCheck]
                                JSONObject assembly2_edge = new JSONObject();
                                assembly2_edge.put("source", assembly1_counter);                
                                assembly2_edge.put("target", edge_list[nodeCheck2]);
                                assembly2_edge.put("type", "IsPartOf");
                                edge_array.put(assembly2_edge);
                            }
                            else { */
                                //System.out.printf("  in else\n");
                                assembly1_counter++;
                                //(
                                assembly2_counter++;
                                //)

                                // add node first:
                                JSONObject assembly2_item = new JSONObject();
                                assembly2_item.put("name", assembly2_node);                
                                assembly2_item.put("type", "L2Assembly");
                                node_array.put(assembly2_item);

                                // then add edge connecting to it:
                                JSONObject assembly2_edge = new JSONObject();
                                assembly2_edge.put("source", part_counter);                
                                assembly2_edge.put("target", assembly1_counter);
                                assembly2_edge.put("type", "IsPartOf");
                                edge_array.put(assembly2_edge);
                                
                                product_list[list_ind] = L1_assembly_id;
                                edge_list[list_ind] = assembly1_counter;
                                
                                list_ind++;

                                System.out.printf("      Assembly2(else) added: %s -> %d, %d\n", assembly2_node, assembly1_counter, L1_assembly_id);

                                String query5 = String.format("   SELECT p2.ProductID, p2.Name\n" +
                                " FROM dbo.Product p1, dbo.Product p2, dbo.IsPartOf partOf1\n" +
                                " WHERE MATCH (p1-(partOf1)->p2)\n" +
                                " AND p1.ProductID = '%d'\n" +
                                " ORDER BY p2.ProductID, p2.Name", L1_assembly_id);

                                Statement st5 = con.createStatement();
                                ResultSet rs5 = st5.executeQuery(query5);

                                while(rs5.next()){
                                    
                                    String assembly3_node = rs5.getString("Name");
                                    int L2_assembly_id = rs5.getInt("ProductID");
                                    /*
                                    int nodeCheck3 = SearchIDList.ListSearch(L2_assembly_id, product_list);
                                    
                                    if ((list_ind != 0) && (nodeCheck3 >= 0)){
                                        System.out.printf("   DETECTED DUPLICATE %d, %d\n", L2_assembly_id, edge_list[nodeCheck3]);
                                        // then add edge connecting to it: from edge_list[nodeCheck]
                                        JSONObject assembly3_edge = new JSONObject();
                                        assembly3_edge.put("source", assembly2_counter);                
                                        assembly3_edge.put("target", edge_list[nodeCheck3]);
                                        assembly3_edge.put("type", "IsPartOf");
                                        edge_array.put(assembly3_edge);
                                    }
                                    else{
                                        */
                                        assembly2_counter++;
                                        // add node first:
                                        JSONObject assembly3_item = new JSONObject();
                                        assembly3_item.put("name", assembly3_node);                
                                        assembly3_item.put("type", "L1Assembly");
                                        node_array.put(assembly3_item);

                                        // then add edge connecting to it:
                                        JSONObject assembly3_edge = new JSONObject();
                                        assembly3_edge.put("source", assembly2_counter);                
                                        assembly3_edge.put("target", assembly1_counter);
                                        assembly3_edge.put("type", "IsPartOf");
                                        edge_array.put(assembly3_edge);
                                        
                                        product_list[list_ind] = L2_assembly_id;
                                        edge_list[list_ind] = assembly2_counter;
                                
                                        list_ind++;

                                        System.out.printf("         Assembly3(else) added: %s -> %d, %d\n", assembly3_node, assembly2_counter, L2_assembly_id);
                                    //}
                                }
                            
                            //} //end else
                            assembly1_counter = assembly2_counter;
                            
                        }
                        
                    }
                    
                    part_counter = assembly1_counter;
                }
                
                supplier_counter = part_counter;
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
    public void MATCHCusttoJSON(ActionEvent event){
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String sql_url = "jdbc:sqlserver://localhost:1433;databaseName=AdventureWorks2014;integratedSecurity=true";
            Connection con = DriverManager.getConnection(sql_url);
            
            //---------------------------------------------FILEWRITE SETUP
            FileWriter fileWriter = new FileWriter("c:\\Users\\Administrator\\Documents\\msft17\\NetBeansProjects\\WebViewTry2\\src\\webviewtry\\match3.json");
            JSONObject json = new JSONObject();
            JSONArray node_array = new JSONArray();
            JSONArray edge_array = new JSONArray();
            
            //------------------------------------------- QUERIES
            
            // LOCATION = 4 SINGLE NODE: 
            JSONObject location = new JSONObject();
            location.put("name", "Southwest US");                
            location.put("type", "location");
            node_array.put(location);
            
            String query1 = "   SELECT s.BusinessEntityID, s.Name\n" +
                "  FROM dbo.Supplier s, dbo.SuppliedFrom sfrom, dbo.Product p\n" +
                "  WHERE MATCH (p-(sfrom)->s)\n" +
                "  AND s.TerritoryID = 4\n" +
                "  AND sfrom.ShipDate BETWEEN '2013-08-13 00:00:00.000' AND '2013-08-22 00:00:00.000'\n" +
                "  GROUP BY s.Name, s.BusinessEntityID";
            
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query1);
            int location_counter = 0;
            int supplier_counter = 0;
            int part_counter = 0;
            int assembly1_counter = 0;
            int assembly2_counter = 0;
            
            // ARRAYS
            int[] product_list = new int[5000];
            int[] edge_list = new int[5000];
            int list_ind = 0;
            
            while(rs.next()){
                // Add Suppliers

                String supplier_node = rs.getString("Name");
                
                supplier_counter++;
                //(
                part_counter++;
                assembly1_counter++;
                assembly2_counter++;
                //)
                JSONObject item = new JSONObject();
                item.put("name", supplier_node);                
                item.put("type", "supplier");
                node_array.put(item);
                
                // then add edge connecting to it:
                JSONObject rest_item2 = new JSONObject();
                rest_item2.put("source", supplier_counter);                
                rest_item2.put("target", location_counter);
                rest_item2.put("type", "LocatedIn");
                edge_array.put(rest_item2);
                
                System.out.printf("Supplier added: %s -> %d\n", supplier_node, supplier_counter);
                    
                // Add the associated products for each supplier
                String query2 = String.format("  SELECT p.ProductID, p.Name, s.BusinessEntityID, s.Name AS 'sName'\n" +
                    "  FROM dbo.Supplier s, dbo.SuppliedFrom sfrom, dbo.Product p\n" +
                    "  WHERE MATCH (p-(sfrom)->s)\n" +
                    "  AND s.TerritoryID = 4\n" +
                    "  AND sfrom.ShipDate BETWEEN '2013-08-13 00:00:00.000' AND '2013-08-22 00:00:00.000'\n" +
                    "  AND p.Name NOT LIKE '%%nut%%' \n" +
                    "  AND p.Name NOT LIKE '%%bolt%%'\n" +
                    "  AND p.Name NOT LIKE '%%lock%%'\n" +
                    "  AND s.Name = '%s'\n" +
                    "  GROUP BY p.ProductID, p.Name, s.BusinessEntityID, s.Name", supplier_node);

                Statement st2 = con.createStatement();
                ResultSet rs2 = st2.executeQuery(query2);
                
                while(rs2.next()){
                    
                    part_counter++;
                    //(
                    assembly1_counter++;
                    assembly2_counter++;
                    //)
                    String part_node = rs2.getString("Name");
                    int assembly_id = rs2.getInt("ProductID");
                    
                    // add node first:
                    JSONObject part_item = new JSONObject();
                    part_item.put("name", part_node);                
                    part_item.put("type", "part");
                    node_array.put(part_item);

                    // then add edge connecting to it:
                    JSONObject part_edge = new JSONObject();
                    part_edge.put("source", part_counter);                
                    part_edge.put("target", supplier_counter);
                    part_edge.put("type", "SuppliedFrom");
                    edge_array.put(part_edge);
                    
                    System.out.printf("   Part added: %s -> %d\n", part_node, part_counter);
                    
                    if (assembly_id == 938 || assembly_id == 939){
                        // level 2 product, just add once
                        
                        // Add the associated products for each supplier
                        String query3 = String.format(" SELECT p2.ProductID, p2.Name\n" +
                            " FROM dbo.Product p1, dbo.Product p2, dbo.IsPartOf partOf\n" +
                            " WHERE MATCH (p1-(partOf)->p2)\n" +
                            " AND p1.ProductID = '%d'\n" +
                            " ORDER BY p2.ProductID, p2.Name", assembly_id);
                        
                        Statement st3 = con.createStatement();
                        ResultSet rs3 = st3.executeQuery(query3);
                        
                        while(rs3.next()){
                            
                            String assembly1_node = rs3.getString("Name");
                            int assembly1_pid = rs3.getInt("ProductID");
                            
                            int nodeCheck = SearchIDList.ListSearch(assembly1_pid, product_list);
                            // if the node is already in the array, only add the edge
                            if ((list_ind != 0) && (nodeCheck >= 0)){
                                System.out.printf("   DETECTED DUPLICATE %d, %d\n", assembly1_pid, edge_list[nodeCheck]);
                                // then add edge connecting to it: from edge_list[nodeCheck]
                                JSONObject assembly1_edge = new JSONObject();
                                assembly1_edge.put("source", assembly1_counter);                
                                assembly1_edge.put("target", edge_list[nodeCheck]);
                                assembly1_edge.put("type", "IsPartOf");
                                edge_array.put(assembly1_edge);
                            }
                            else{
                                assembly1_counter++;
                            
                                //(
                                assembly2_counter++;
                                //)
                                // add node first:
                                JSONObject assembly1_item = new JSONObject();
                                assembly1_item.put("name", assembly1_node);                
                                assembly1_item.put("type", "L1Assembly");
                                node_array.put(assembly1_item);

                                // then add edge connecting to it:
                                JSONObject assembly1_edge = new JSONObject();
                                assembly1_edge.put("source", part_counter);                
                                assembly1_edge.put("target", assembly1_counter);
                                assembly1_edge.put("type", "IsPartOf");
                                edge_array.put(assembly1_edge);
                                
                                product_list[list_ind] = assembly1_pid;
                                edge_list[list_ind] = assembly1_counter;
                                
                                list_ind++;
                                
                                System.out.printf("      Assembly1 added: %s -> %d, %d\n", assembly1_node, assembly1_counter, assembly1_pid);
                            }
                            
                        }
                        
                    }
                    
                    else {
                        
                        // Add the associated products for each supplier
                        String query4 = String.format("   SELECT p2.ProductID, p2.Name\n" +
                            " FROM dbo.Product p1, dbo.Product p2, dbo.IsPartOf partOf1\n" +
                            " WHERE MATCH (p1-(partOf1)->p2)\n" +
                            " AND p1.ProductID = '%d'\n" +
                            " ORDER BY p2.ProductID, p2.Name", assembly_id);
                        
                        Statement st4 = con.createStatement();
                        ResultSet rs4 = st4.executeQuery(query4);
                        
                        while(rs4.next()){
                            String assembly2_node = rs4.getString("Name");
                            int L1_assembly_id = rs4.getInt("ProductID");
                            
                            int nodeCheck2 = SearchIDList.ListSearch(L1_assembly_id, product_list);
                            
                            if ((list_ind != 0) && (nodeCheck2 >= 0)){
                                System.out.printf("   DETECTED DUPLICATE %d, %d\n", L1_assembly_id, edge_list[nodeCheck2]);

                                // then add edge connecting to it: from edge_list[nodeCheck]
                                JSONObject assembly2_edge = new JSONObject();
                                assembly2_edge.put("source", assembly1_counter);                
                                assembly2_edge.put("target", edge_list[nodeCheck2]);
                                assembly2_edge.put("type", "IsPartOf");
                                edge_array.put(assembly2_edge);
                            }
                            else {
                                //System.out.printf("  in else\n");
                                assembly1_counter++;
                                //(
                                assembly2_counter++;
                                //)

                                // add node first:
                                JSONObject assembly2_item = new JSONObject();
                                assembly2_item.put("name", assembly2_node);                
                                assembly2_item.put("type", "L2Assembly");
                                node_array.put(assembly2_item);

                                // then add edge connecting to it:
                                JSONObject assembly2_edge = new JSONObject();
                                assembly2_edge.put("source", part_counter);                
                                assembly2_edge.put("target", assembly1_counter);
                                assembly2_edge.put("type", "IsPartOf");
                                edge_array.put(assembly2_edge);
                                
                                product_list[list_ind] = L1_assembly_id;
                                edge_list[list_ind] = assembly1_counter;
                                
                                list_ind++;

                                System.out.printf("      Assembly2(else) added: %s -> %d, %d\n", assembly2_node, assembly1_counter, L1_assembly_id);

                                String query5 = String.format("   SELECT p2.ProductID, p2.Name\n" +
                                " FROM dbo.Product p1, dbo.Product p2, dbo.IsPartOf partOf1\n" +
                                " WHERE MATCH (p1-(partOf1)->p2)\n" +
                                " AND p1.ProductID = '%d'\n" +
                                " ORDER BY p2.ProductID, p2.Name", L1_assembly_id);

                                Statement st5 = con.createStatement();
                                ResultSet rs5 = st5.executeQuery(query5);

                                while(rs5.next()){
                                    
                                    String assembly3_node = rs5.getString("Name");
                                    int L2_assembly_id = rs5.getInt("ProductID");
                                    
                                    int nodeCheck3 = SearchIDList.ListSearch(L2_assembly_id, product_list);
                                    
                                    if ((list_ind != 0) && (nodeCheck3 >= 0)){
                                        System.out.printf("   DETECTED DUPLICATE %d, %d\n", L2_assembly_id, edge_list[nodeCheck3]);

                                        // then add edge connecting to it: from edge_list[nodeCheck]
                                        JSONObject assembly3_edge = new JSONObject();
                                        assembly3_edge.put("source", assembly2_counter);                
                                        assembly3_edge.put("target", edge_list[nodeCheck3]);
                                        assembly3_edge.put("type", "IsPartOf");
                                        edge_array.put(assembly3_edge);

                                    }
                                    else{
                                        
                                        assembly2_counter++;
                                        // add node first:
                                        JSONObject assembly3_item = new JSONObject();
                                        assembly3_item.put("name", assembly3_node);                
                                        assembly3_item.put("type", "L1Assembly");
                                        node_array.put(assembly3_item);

                                        // then add edge connecting to it:
                                        JSONObject assembly3_edge = new JSONObject();
                                        assembly3_edge.put("source", assembly2_counter);                
                                        assembly3_edge.put("target", assembly1_counter);
                                        assembly3_edge.put("type", "IsPartOf");
                                        edge_array.put(assembly3_edge);
                                        
                                        product_list[list_ind] = L2_assembly_id;
                                        edge_list[list_ind] = assembly2_counter;
                                
                                        list_ind++;

                                        System.out.printf("         Assembly3(else) added: %s -> %d, %d\n", assembly3_node, assembly2_counter, L2_assembly_id);
                                    }
                                }
                            
                            } //end else
                            assembly1_counter = assembly2_counter;
                            
                        }
                        
                    }
                    
                    part_counter = assembly1_counter;
                }
                
                supplier_counter = part_counter;
            }
            
 
            String querycust = "  SELECT c.[CustomerID], c.[PersonID], c.[StoreID], c.[TerritoryID], c.[StoreName], c.[Title], c.[First], c.[Middle], c.[Last], c.[Suffix], p.ProductID, p.Name, bb.ShipDate\n" +
            " FROM dbo.Customer c, dbo.BoughtBy bb, dbo.Product p\n" +
            " WHERE MATCH (p-(bb)->c)\n" +
            " AND (p.ProductID BETWEEN 749 AND 801\n" +
            "	OR p.ProductID BETWEEN 953 AND 993\n" +
            "	OR p.ProductID BETWEEN 997 AND 999)\n" +
            "AND bb.ShipDate BETWEEN '2013-08-13 00:00:00.000' AND '2013-08-31 00:00:00.000'\n" +
            "ORDER BY bb.ShipDate";
            
            Statement st6 = con.createStatement();
            ResultSet rs6 = st6.executeQuery(querycust);
            
            while(rs6.next()){
                        
                String cust_node = null;
                
                if (rs6.getString("StoreName") == "NULL"){
                    cust_node = rs6.getString("StoreName");
                }
                else {
                    cust_node = rs6.getString("First") + " " + rs6.getString("Last");
                }
                
                int cust_id = rs6.getInt("ProductID");
                
                supplier_counter++;
                
                //insert node
                JSONObject cust_item = new JSONObject();
                cust_item.put("name", cust_node);                
                cust_item.put("type", "customer");
                node_array.put(cust_item);
                
                int nodeCheckcust = SearchIDList.ListSearch(cust_id, product_list);
                                    
                if (nodeCheckcust >= 0){

                    // then add edge connecting to it: from edge_list[nodeCheck]
                    JSONObject assembly3_edge = new JSONObject();
                    assembly3_edge.put("source", supplier_counter);                
                    assembly3_edge.put("target", edge_list[nodeCheckcust]);
                    assembly3_edge.put("type", "Bought");
                    edge_array.put(assembly3_edge);

                }
                
            
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
    
    public void RecurParttoJSON(ActionEvent event){
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String sql_url = "jdbc:sqlserver://localhost:1433;databaseName=AdventureWorks2014;integratedSecurity=true";
            Connection con = DriverManager.getConnection(sql_url);
            
            //---------------------------------------------FILEWRITE SETUP
            FileWriter fileWriter = new FileWriter("c:\\Users\\Administrator\\Documents\\msft17\\NetBeansProjects\\WebViewTry2\\src\\webviewtry\\recursionSmall.json");
            JSONObject json = new JSONObject();
            JSONArray node_array = new JSONArray();
            JSONArray edge_array = new JSONArray();
            
            //------------------------------------------- QUERIES
            
            String query1 = "	DECLARE @childval AS int;\n" +
                        "	SET @childval = 486;\n" +
                        "	-- Recursively find product assemblies.\n" +
                        "	WITH BOM (ProductId, Name, ProductNumber, parentid, parentname)\n" +
                        "	AS\n" +
                        "	(\n" +
                        "		-- first the anchor\n" +
                        "		SELECT p0.ProductID, p0.Name, p0.ProductNumber, \n" +
                        "			p1.ProductId parentid, p1.Name parentname\n" +
                        "		FROM Product p0, IsPartOf ipo, Product p1\n" +
                        "		WHERE MATCH(p0-(ipo)->p1)\n" +
                        "		AND p0.ProductID = @childval\n" +
                        "		-- then the 'recursion'\n" +
                        "		UNION ALL\n" +
                        "		SELECT p2.ProductID, p2.Name, p2.ProductNumber, \n" +
                        "			p3.ProductId parentid, p3.Name parentname\n" +
                        "		FROM BOM, IsPartOf ipo1, Product p2, Product p3\n" +
                        "		WHERE MATCH(p2-(ipo1)->p3)\n" +
                        "		AND BOM.parentid = p2.ProductId\n" +
                        "	  )\n" +
                        "	SELECT ProductId, Name, ProductNumber, parentid, parentname\n" +
                        "	FROM BOM\n" +
                        "	GROUP BY ProductID, Name, ProductNumber, parentID, parentname";
            
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query1);
            
            // 1 ARRAY to keep track of the nodes added O(1) insertion and lookup
            int[] nodelist = new int[1000];
            
            // COUNTER to keep track of inserted node #
            int node_counter = 0;
            
            while(rs.next()){

                int childID = rs.getInt("ProductID");
                String childName = rs.getString("Name");
                
                int parentID = rs.getInt("parentid");
                String parentName = rs.getString("parentname");
                
                // check if childnode and parentnode are stored in the array
                int array_status_child = ArrayCheck.CheckArray(childID, nodelist);
                int array_status_parent = ArrayCheck.CheckArray(parentID, nodelist);
                
                // Condition 1: both C and P exist
                if ((array_status_child > 0) && (array_status_parent > 0)){
                    // insert edge between C and P
                    JSONObject bothedge = new JSONObject();
                    bothedge.put("source", array_status_child);                
                    bothedge.put("target", array_status_parent);
                    bothedge.put("type", "IsPartOf");
                    edge_array.put(bothedge);
                }
                // Condition 2: C is a new node, P exists
                else if ((array_status_child < 0) && (array_status_parent > 0)){
                    // add C as a node
                    JSONObject item = new JSONObject();
                    item.put("name", childName);  
                    item.put("id", childID);
                    item.put("type", "part");
                    node_array.put(item);
                    
                    // add the node to our array and increment counter
                    nodelist[childID] = node_counter;
                    node_counter++;
                    
                    // then add edge between 
                    // insert edge between C and P
                    JSONObject edge = new JSONObject();
                    edge.put("source", nodelist[childID]);                
                    edge.put("target", array_status_parent);
                    edge.put("type", "IsPartOf");
                    edge_array.put(edge);
                }
                // Condition 3: C exists, P is a new node
                else if ((array_status_child > 0) && (array_status_parent < 0)){
                    // add P as a node
                    JSONObject item = new JSONObject();
                    item.put("name", parentName);  
                    item.put("id", parentID);
                    item.put("type", "part");
                    node_array.put(item);
                    
                    // add the node to our array and increment counter
                    nodelist[parentID] = node_counter;
                    node_counter++;
                    
                    // then add edge between 
                    // insert edge between C and P
                    JSONObject edge = new JSONObject();
                    edge.put("source", array_status_child);                
                    edge.put("target", nodelist[parentID]);
                    edge.put("type", "IsPartOf");
                    edge_array.put(edge);
                }
                
                // Condition 4: Neither C or P exist yet
                else if ((array_status_child < 0) && (array_status_parent < 0)){
                    // add C as a node
                    JSONObject node = new JSONObject();
                    node.put("name", childName);  
                    node.put("id", childID);
                    node.put("type", "part");
                    node_array.put(node);
                    
                    // add the node to our array and increment counter
                    nodelist[childID] = node_counter;
                    node_counter++;
                    
                    // add P as a node
                    JSONObject item = new JSONObject();
                    item.put("name", parentName);  
                    item.put("id", parentID);
                    item.put("type", "part");
                    node_array.put(item);
                    
                    // add the node to our array and increment counter
                    nodelist[parentID] = node_counter;
                    node_counter++;
                    
                    // then add edge connecting to it:
                    JSONObject edge = new JSONObject();
                    edge.put("source", nodelist[childID]);                
                    edge.put("target", nodelist[parentID]);
                    edge.put("type", "IsPartOf");
                    edge_array.put(edge);
                }
                
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
    public void RecurFulltoJSON(ActionEvent event){
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String sql_url = "jdbc:sqlserver://localhost:1433;databaseName=AdventureWorks2014;integratedSecurity=true";
            Connection con = DriverManager.getConnection(sql_url);
            
            //---------------------------------------------FILEWRITE SETUP
            FileWriter fileWriter = new FileWriter("c:\\Users\\Administrator\\Documents\\msft17\\NetBeansProjects\\WebViewTry2\\src\\webviewtry\\recursion.json");
            JSONObject json = new JSONObject();
            JSONArray node_array = new JSONArray();
            JSONArray edge_array = new JSONArray();
            
            //------------------------------------------- QUERIES
            
            String query1 = "	-- Recursively find product assemblies.\n" +
            "	WITH BOM (ProductId, Name, ProductNumber, parentid, parentname)\n" +
            "	AS\n" +
            "	(\n" +
            "		-- first the anchor\n" +
            "		SELECT p0.ProductID, p0.Name, p0.ProductNumber, \n" +
            "			p1.ProductId parentid, p1.Name parentname\n" +
            "		FROM Product p0, IsPartOf ipo, Product p1\n" +
            "		WHERE MATCH(p0-(ipo)->p1)\n" +
            "		-- then the 'recursion'\n" +
            "		UNION ALL\n" +
            "		SELECT p2.ProductID, p2.Name, p2.ProductNumber, \n" +
            "			p3.ProductId parentid, p3.Name parentname\n" +
            "		FROM BOM, IsPartOf ipo1, Product p2, Product p3\n" +
            "		WHERE MATCH(p2-(ipo1)->p3)\n" +
            "		AND BOM.parentid = p2.ProductId\n" +
            "	  )\n" +
            "	SELECT ProductId, Name, ProductNumber, parentid, parentname\n" +
            "	FROM BOM\n" +
            "   GROUP BY ProductID, Name, ProductNumber, parentID, parentname\n";
            
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query1);
            
            // 1 ARRAY to keep track of the nodes added O(1) insertion and lookup
            int[] nodelist = new int[1000];
            
            // COUNTER to keep track of inserted node #
            int node_counter = 0;
            
            while(rs.next()){

                int childID = rs.getInt("ProductID");
                String childName = rs.getString("Name");
                
                int parentID = rs.getInt("parentid");
                String parentName = rs.getString("parentname");
                
                // check if childnode and parentnode are stored in the array
                int array_status_child = ArrayCheck.CheckArray(childID, nodelist);
                int array_status_parent = ArrayCheck.CheckArray(parentID, nodelist);
                
                // Condition 1: both C and P exist
                if ((array_status_child > 0) && (array_status_parent > 0)){
                    // insert edge between C and P
                    JSONObject bothedge = new JSONObject();
                    bothedge.put("source", array_status_child);                
                    bothedge.put("target", array_status_parent);
                    bothedge.put("type", "IsPartOf");
                    edge_array.put(bothedge);
                }
                // Condition 2: C is a new node, P exists
                else if ((array_status_child < 0) && (array_status_parent > 0)){
                    // add C as a node
                    JSONObject item = new JSONObject();
                    item.put("name", childName);  
                    item.put("id", childID);
                    item.put("type", "part");
                    node_array.put(item);
                    
                    // add the node to our array and increment counter
                    nodelist[childID] = node_counter;
                    node_counter++;
                    
                    // then add edge between 
                    // insert edge between C and P
                    JSONObject edge = new JSONObject();
                    edge.put("source", nodelist[childID]);                
                    edge.put("target", array_status_parent);
                    edge.put("type", "IsPartOf");
                    edge_array.put(edge);
                }
                // Condition 3: C exists, P is a new node
                else if ((array_status_child > 0) && (array_status_parent < 0)){
                    // add P as a node
                    JSONObject item = new JSONObject();
                    item.put("name", parentName);  
                    item.put("id", parentID);
                    item.put("type", "part");
                    node_array.put(item);
                    
                    // add the node to our array and increment counter
                    nodelist[parentID] = node_counter;
                    node_counter++;
                    
                    // then add edge between 
                    // insert edge between C and P
                    JSONObject edge = new JSONObject();
                    edge.put("source", array_status_child);                
                    edge.put("target", nodelist[parentID]);
                    edge.put("type", "IsPartOf");
                    edge_array.put(edge);
                }
                
                // Condition 4: Neither C or P exist yet
                else if ((array_status_child < 0) && (array_status_parent < 0)){
                    // add C as a node
                    JSONObject node = new JSONObject();
                    node.put("name", childName);  
                    node.put("id", childID);
                    node.put("type", "part");
                    node_array.put(node);
                    
                    // add the node to our array and increment counter
                    nodelist[childID] = node_counter;
                    node_counter++;
                    
                    // add P as a node
                    JSONObject item = new JSONObject();
                    item.put("name", parentName);  
                    item.put("id", parentID);
                    item.put("type", "part");
                    node_array.put(item);
                    
                    // add the node to our array and increment counter
                    nodelist[parentID] = node_counter;
                    node_counter++;
                    
                    // then add edge connecting to it:
                    JSONObject edge = new JSONObject();
                    edge.put("source", nodelist[childID]);                
                    edge.put("target", nodelist[parentID]);
                    edge.put("type", "IsPartOf");
                    edge_array.put(edge);
                }
                
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
    //    public void MATCH1DatatoJSON(ActionEvent event){
//        try{
//            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//            String sql_url = "jdbc:sqlserver://localhost:1433;databaseName=AdventureWorks2014;integratedSecurity=true";
//            Connection con = DriverManager.getConnection(sql_url);
//            
//            //---------------------------------------------FILEWRITE SETUP
//            FileWriter fileWriter = new FileWriter("c:\\Users\\Administrator\\Documents\\msft17\\NetBeansProjects\\WebViewTry2\\src\\webviewtry\\match1.json");
//            JSONObject json = new JSONObject();
//            JSONArray node_array = new JSONArray();
//            JSONArray edge_array = new JSONArray();
//            
//            //------------------------------------------- QUERIES
//            String query1 = "  SELECT p.Name\n" +
//                "  FROM HasInventoryOf h, Supplier s, Product p \n" +
//                "  WHERE MATCH (s-(h)->p)\n" +
//                "  AND (p.ProductID = '351' OR p.ProductID = '321')\n" +
//                "  GROUP BY p.Name";
//            Statement st = con.createStatement();
//            ResultSet rs = st.executeQuery(query1);
//            int part_counter = 0;
//            int supplier_counter = 0;
//            
//            while(rs.next()){
//     
//                // Get PART, add to JSON Object Node List
//                String part_node = rs.getString("Name");
//
//                JSONObject item = new JSONObject();
//                item.put("name", part_node);                
//                item.put("type", "part");
//                node_array.put(item);
//                
//                supplier_counter++;
//                
//                // Get supplier to connect
//                String query2 = String.format("  SELECT s.Name AS 'sName'\n" +
//                "  FROM HasInventoryOf h, Supplier s, Product p \n" +
//                "  WHERE MATCH (s-(h)->p)\n" +
//                "  AND p.Name = '%s'\n" +
//                "  GROUP BY s.Name", part_node);
//
//                Statement st2 = con.createStatement();
//                ResultSet rs2 = st2.executeQuery(query2);
//                
//                while(rs2.next()){
//                    
//                    
//                    String supplier_node = rs2.getString("sName");
//        
//                        // add node first:
//                        JSONObject supplier_item = new JSONObject();
//                        supplier_item.put("name", supplier_node);                
//                        supplier_item.put("type", "supplier");
//                        node_array.put(supplier_item);
//                        
//                        // then add edge connecting to it:
//                        JSONObject rest_item2 = new JSONObject();
//                        rest_item2.put("source", supplier_counter);                
//                        rest_item2.put("target", part_counter);
//                        rest_item2.put("type", "hasInvOf");
//                        edge_array.put(rest_item2);
//                    
//                    supplier_counter++;
//                    }
//                part_counter = supplier_counter;
//                }
//                
//            json.put("nodes", node_array);
//            json.put("links", edge_array);
//            
//
//            fileWriter.write(json.toString());
//            fileWriter.close();
//        }
//        catch(Exception e){
//            JOptionPane.showMessageDialog(null, e);
//        }
//        
//    }


//    public ObservableList<MissingPart> getDataM1(){
//        ObservableList<MissingPart> datalist = FXCollections.observableArrayList();
//       
//        try{
//            // this is the SQL connection code
//            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//            String url = "jdbc:sqlserver://localhost:1433;databaseName=AdventureWorks2014;integratedSecurity=true";
//            Connection con = DriverManager.getConnection(url);
//            String query1 = " SELECT p.ProductID, p.Name AS 'pName', s.BusinessEntityID, s.Name AS 'sName', h.StandardPrice, h.MinOrderQty, h.MaxOrderQty\n" +
//                "  FROM HasInventoryOf h, Supplier s, Product p \n" +
//                "  WHERE MATCH (s-(h)->p)\n" +
//                "  AND (p.ProductID = '351' OR p.ProductID = '321')";
//            
//            Statement st = con.createStatement();
//            ResultSet rs = st.executeQuery(query1);
//            
//            MissingPart missingpart = null;
//            while(rs.next()){
//                missingpart = new MissingPart(rs.getInt("ProductID"), rs.getString("pName"), rs.getInt("BusinessEntityID"), rs.getString("sName"), rs.getFloat("StandardPrice"), rs.getInt("MinOrderQty"), rs.getInt("MaxOrderQty"));
//                datalist.add(missingpart);
//            }
//        }
//        catch(Exception e){
//            JOptionPane.showMessageDialog(null, e);
//        }   
//        return datalist;
//    }

