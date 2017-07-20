/*
Check Array Index for existing node number
 */
package webviewtry;


/**
 *
 * @author Administrator
 */
public class ArrayCheck {

    /**
     *
     * @param index
     * @param list
     * @return
     */
    public static int CheckArray(int index, int[] list){
            if (list[index] > 0){
                return list[index];
            }         
            else {
                return -1;
            }
    }
}
