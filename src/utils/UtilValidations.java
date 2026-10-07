package utils;

/**
 *
 * @author brysonfl
 */
public class UtilValidations {

    public static boolean validateBoard(int row, int col) {
        return row >= 0 && row <= 7 && col >= 0 && col <= 7;
    }
    
}
