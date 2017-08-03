/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webviewtry;


/**
 *
 * @author Administrator
 */
import java.util.Objects;
public class SearchIDList {

    /**
     *
     * @param match
     * @param list
     * @return
     */
    public static int ListSearch(int match, int[] list){
        //System.out.printf(match);
        for (int i = 0; i < list.length; i++){
            //System.out.printf("%s\n", list[i]);
            if (Objects.equals(list[i], match)){
                return i;
            }           
        }
        return -1;
    }
}
