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
class Restaurant {
    // declare variables are private 
    private final int rating;
    private final String name;
    
    // public function calls
    public Restaurant(String name, int rating){
        this.rating = rating;
        this.name = name;
       
    }
    
    public int getrating(){
        return rating;
    }
    
    public String getname(){
        return name;
    }
}
