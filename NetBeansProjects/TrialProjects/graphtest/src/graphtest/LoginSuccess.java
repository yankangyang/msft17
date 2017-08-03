package graphtest;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class LoginSuccess extends javax.swing.JFrame {

    /**
     * Creates new form LoginSuccess
     */
    String gender; 
    //private Object jTable_Display_User;
   // private Object jTable_Female_Users;
    public LoginSuccess() {
        initComponents();
        show_user();
    }
    public ArrayList<User> userList(){
        ArrayList<User> usersList = new ArrayList<>();
       
        try{
            // this is the SQL connection code
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://localhost:1433;databaseName=trialdb;integratedSecurity=true";
            Connection con = DriverManager.getConnection(url);
            String query1 = "SELECT * FROM testusers";
            
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query1);
            
            User user;
            while(rs.next()){
                user = new User(rs.getInt("sno"), rs.getString("name"), rs.getString("address"), rs.getString("gender"), rs.getString("skills"), rs.getString("subject"));
                usersList.add(user);
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }   
        return usersList;
    }
    
        public ArrayList<User> femaleUserList(){
        ArrayList<User> femaleUsersList = new ArrayList<>();
       
        try{
            // this is the SQL connection code
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://localhost:1433;databaseName=trialdb;integratedSecurity=true";
            Connection con = DriverManager.getConnection(url);
            String query1 = "SELECT * FROM testusers WHERE testusers.gender = 'Female' AND testusers.skills LIKE '%Python%'";
            
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query1);
            
            User user;
            while(rs.next()){
                user = new User(rs.getInt("sno"), rs.getString("name"), rs.getString("address"), rs.getString("gender"), rs.getString("skills"), rs.getString("subject"));
                femaleUsersList.add(user);
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }   
        return femaleUsersList;
    }
        
        public ArrayList<Restaurant> julieRR(){
        ArrayList<Restaurant> julieRestList = new ArrayList<>();
       
        try{
            // this is the SQL connection code
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://localhost:1433;databaseName=sampledb;integratedSecurity=true";
            Connection con = DriverManager.getConnection(url);
            String query1 = "SELECT Restaurant.name, likes.rating\n" +
                "FROM Person, likes, Restaurant\n" +
                "WHERE MATCH (Person-(likes)->Restaurant)\n" +
                "AND Person.name = 'Julie'";
            
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query1);
            
            Restaurant demo;
            while(rs.next()){
                demo = new Restaurant(rs.getString("name"), rs.getInt("rating"));
                julieRestList.add(demo);
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }   
        return julieRestList;
    }
    
    public void show_user(){
        ArrayList<User> list = userList();
        
        DefaultTableModel model = (DefaultTableModel)jTable_Display_Users.getModel();     
        Object[] col = new Object[6];
        for(int i = 0; i < list.size(); i++){
            col[0] = list.get(i).getsno();
            col[1] = list.get(i).getname();
            col[2] = list.get(i).getaddress();
            col[3] = list.get(i).getgender();
            col[4] = list.get(i).getskills();
            col[5] = list.get(i).getsubject();
            
            model.addRow(col);
        }
        
        ArrayList<User> flist = femaleUserList();
        
        DefaultTableModel fmodel = (DefaultTableModel)jTable_Female_Users.getModel();     
        Object[] fcol = new Object[6];
        for(int i = 0; i < flist.size(); i++){
            fcol[0] = flist.get(i).getsno();
            fcol[1] = flist.get(i).getname();
            fcol[2] = flist.get(i).getaddress();
            fcol[3] = flist.get(i).getgender();
            fcol[4] = flist.get(i).getskills();
            fcol[5] = flist.get(i).getsubject();
            
            fmodel.addRow(fcol);
        }
        
        ArrayList<Restaurant> julielist = julieRR();
        
        DefaultTableModel juliemodel = (DefaultTableModel)jTable_julie_favrest.getModel();     
        Object[] juliecol = new Object[2];
        for(int i = 0; i < julielist.size(); i++){
            juliecol[0] = julielist.get(i).getname();
            juliecol[1] = julielist.get(i).getrating();

            juliemodel.addRow(juliecol);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        name = new javax.swing.JTextField();
        address = new javax.swing.JTextField();
        male = new javax.swing.JRadioButton();
        female = new javax.swing.JRadioButton();
        subject = new javax.swing.JComboBox();
        coreJava = new javax.swing.JCheckBox();
        corePython = new javax.swing.JCheckBox();
        coreC = new javax.swing.JCheckBox();
        SaveBttn = new javax.swing.JButton();
        ResetBttn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_Display_Users = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable_Female_Users = new javax.swing.JTable();
        visualizebttn = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable_julie_favrest = new javax.swing.JTable();
        graphdblabel = new java.awt.Label();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Name");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Address");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Gender");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Skills");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setText("Subject");

        name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameActionPerformed(evt);
            }
        });

        buttonGroup1.add(male);
        male.setText("Male");

        buttonGroup1.add(female);
        female.setText("Female");
        female.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                femaleActionPerformed(evt);
            }
        });

        subject.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Computer Science", "Electrical Engineering", "Social Studies", "Economics", "Government" }));

        coreJava.setText("Java");

        corePython.setText("Python");

        coreC.setText("C/C++/C#");
        coreC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                coreCActionPerformed(evt);
            }
        });

        SaveBttn.setText("Save");
        SaveBttn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveBttnActionPerformed(evt);
            }
        });

        ResetBttn.setText("Reset");
        ResetBttn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResetBttnActionPerformed(evt);
            }
        });

        jTable_Display_Users.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sno", "Name", "Address", "Gender", "Skills", "Subject"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable_Display_Users);

        jTable_Female_Users.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sno", "Name", "Address", "Gender", "Skills", "Subject"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable_Female_Users);

        visualizebttn.setText("Visualize");
        visualizebttn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visualizebttnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(SaveBttn)
                        .addGap(163, 163, 163)
                        .addComponent(ResetBttn))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(175, 175, 175)
                                .addComponent(female)
                                .addGap(81, 81, 81))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(83, 83, 83)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(address, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(79, 79, 79)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(subject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(coreJava)
                                        .addGap(18, 18, 18)
                                        .addComponent(corePython)
                                        .addGap(18, 18, 18)
                                        .addComponent(coreC))
                                    .addComponent(visualizebttn)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(76, 76, 76)
                        .addComponent(male)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 496, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(address, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(male)
                            .addComponent(female)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(coreJava)
                            .addComponent(corePython)
                            .addComponent(coreC))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(subject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ResetBttn)
                    .addComponent(SaveBttn))
                .addGap(18, 18, 18)
                .addComponent(visualizebttn)
                .addGap(133, 133, 133))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTable_julie_favrest.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "name", "rating"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTable_julie_favrest);

        graphdblabel.setText("Graph DB Query Demo");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(graphdblabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(graphdblabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        graphdblabel.getAccessibleContext().setAccessibleName("label1");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ResetBttnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ResetBttnActionPerformed
        name.setText("");
        address.setText("");
        buttonGroup1.clearSelection();
        coreJava.setSelected(false);
        corePython.setSelected(false);
        coreC.setSelected(false);
        subject.setSelectedIndex(0);
    }//GEN-LAST:event_ResetBttnActionPerformed

    private void SaveBttnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveBttnActionPerformed
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://localhost:1433;databaseName=trialdb;integratedSecurity=true";
            Connection con = DriverManager.getConnection(url);
            String query = "INSERT INTO testusers(name, address, gender, skills, subject)values(?,?,?,?,?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, name.getText());
            pst.setString(2, address.getText());
            if(male.isSelected()){
                gender="Male";
            }
            if(female.isSelected()){
                gender="Female";
            }
            pst.setString(3, gender);

            String skills = "";
            if (coreJava.isSelected()){
                skills += coreJava.getText()+" ";
            }

            if (corePython.isSelected()){
                skills += corePython.getText()+" ";
            }
            // get text takes the text from the checkbox and adds to SQL lib
            if (coreC.isSelected()){
                skills += coreC.getText()+" ";
            }
            pst.setString(4, skills);

            String course;
            course = subject.getSelectedItem().toString();
            pst.setString(5, course);
            
            // execute update
            pst.executeUpdate();
            
            DefaultTableModel model = (DefaultTableModel)jTable_Display_Users.getModel();  
            DefaultTableModel fmodel = (DefaultTableModel)jTable_Female_Users.getModel(); 
            
            model.setRowCount(0); // empties data
            fmodel.setRowCount(0); // empties data
            show_user(); // reshow data
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_SaveBttnActionPerformed

    private void coreCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_coreCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_coreCActionPerformed

    private void femaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_femaleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_femaleActionPerformed

    private void nameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameActionPerformed

    private void visualizebttnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visualizebttnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_visualizebttnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LoginSuccess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginSuccess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginSuccess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginSuccess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginSuccess().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ResetBttn;
    private javax.swing.JButton SaveBttn;
    private javax.swing.JTextField address;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox coreC;
    private javax.swing.JCheckBox coreJava;
    private javax.swing.JCheckBox corePython;
    private javax.swing.JRadioButton female;
    private java.awt.Label graphdblabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable_Display_Users;
    private javax.swing.JTable jTable_Female_Users;
    private javax.swing.JTable jTable_julie_favrest;
    private javax.swing.JRadioButton male;
    private javax.swing.JTextField name;
    private javax.swing.JComboBox subject;
    private javax.swing.JButton visualizebttn;
    // End of variables declaration//GEN-END:variables

}
