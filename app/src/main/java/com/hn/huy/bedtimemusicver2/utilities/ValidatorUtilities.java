package com.hn.huy.bedtimemusicver2.utilities;

import java.util.List;

/**
 * Created by huy on 01/02/2017.
 */

public class ValidatorUtilities {

    private ValidatorUtilities() {
    }

    public static boolean isEmpty(String string){
        if(null == string || string.length() == 0){
            return false;
        }
        return true;
    }

    public static boolean isEmptyForArray(Object[] array){
        if(null == array || array.length == 0){
            return false;
        }
        return true;
    }

    public static boolean isEmptyForList(List<?> list){
        if(null == list || list.size() == 0){
            return false;
        }
        return true;
    }

    public static boolean isNull(Object o){
        return null == o;
    }
}
