/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphtest;


/**
 *
 * @author Administrator
 */
import java.util.Objects;
public class SearchList {

    /**
     *
     * @param match
     * @param list
     * @return
     */
    public static boolean ListSearch(String match, String[] list){
        //System.out.printf(match);
        for (int i = 0; i < list.length; i++){
            //System.out.printf("%s\n", list[i]);
            if (Objects.equals(list[i], match)){
                return true;
            }           
        }
        return false;
    }
}
