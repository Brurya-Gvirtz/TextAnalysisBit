package com.textanalysis.bit.util;

import java.util.Arrays;
import java.util.List;

public class TextUtils {
    
    public static final List<String> MOST_COMMON_FIRST_NAMES = Arrays.asList(
        "James", "John", "Robert", "Michael", "William", "David", "Richard", "Charles", 
        "Joseph", "Thomas", "Christopher", "Daniel", "Paul", "Mark", "Donald", "George", 
        "Kenneth", "Steven", "Edward", "Brian", "Ronald", "Anthony", "Kevin", "Jason", 
        "Matthew", "Gary", "Timothy", "Jose", "Larry", "Jeffrey", "Frank", "Scott", 
        "Eric", "Stephen", "Andrew", "Raymond", "Gregory", "Joshua", "Jerry", "Dennis", 
        "Walter", "Patrick", "Peter", "Harold", "Douglas", "Henry", "Carl", "Arthur", 
        "Ryan", "Roger"
    );
    
    /**
     * Cleans text by removing extra whitespace and normalizing line endings
     */
    public static String cleanText(String text) {
        if (text == null) {
            return "";
        }
        return text.replaceAll("\\r\\n|\\r", "\n").trim();
    }
    
    /**
     * Checks if a word is a valid search term
     */
    public static boolean isValidWord(String word) {
        return word != null && !word.trim().isEmpty() && word.matches("^[a-zA-Z]+$");
    }
}
