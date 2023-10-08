package com.driver.misc;

public class Solutions {

    public static String smallestLexicographically(String str1 , String str2){
        return (str1.compareTo(str2) < 0) ? str1 : str2;
    }

}
