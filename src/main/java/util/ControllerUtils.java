package util;

import java.util.Map;
import java.util.Set;

/**
 * Created by YG-MAC on 2016. 12. 15..
 */
public class ControllerUtils {
    public static boolean isValidParameters(Map<String, String> requestParameter, Set<String> parameters){
        for(String parameter : parameters){
            if(!requestParameter.containsKey(parameter)){
                return false;
            }
        }
        return true;
    }
}
