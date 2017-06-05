package LoginTables;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
class User {
    private int sno;
    private String name, address, gender, skills, subject;
    
    public User(int sno, String name, String address, String gender, String skills, String subject){
        this.sno = sno;
        this.address = address;
        this.name = name;
        this.gender = gender; 
        this.skills = skills; 
        this.subject = subject;
    }
    
    public int getsno(){
        return sno;
    }
    
    public String getname(){
        return name;
    }
    
    public String getaddress(){
        return address;
    }
    
    public String getgender(){
        return gender;
    }
    
    public String getskills(){
        return skills;
    }
    
    public String getsubject(){
        return subject;
    }
    
}
