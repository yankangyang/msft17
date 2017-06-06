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
public class SearchList {

    /**
     *
     * @param match
     * @param list
     * @return
     */
    public static boolean ListSearch(String match, String[] list){
        for (int i = 0; i < list.length; i++){
            if (list[i].equals(match)){
                return true;
            }
        }
        return false;
    }
}
