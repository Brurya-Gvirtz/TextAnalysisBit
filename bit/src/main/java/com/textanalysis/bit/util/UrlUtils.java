package com.textanalysis.bit.util;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlUtils {
    
    /**
     * Validates if a URL string is properly formatted
     */
    public static boolean isValidUrl(String urlString) {
        try {
            new URL(urlString);
            return urlString.startsWith("http://") || urlString.startsWith("https://");
        } catch (MalformedURLException e) {
            return false;
        }
    }
    
    /**
     * Sanitizes URL by removing potentially harmful characters
     */
    public static String sanitizeUrl(String url) {
        if (url == null) {
            return "";
        }
        return url.trim().replaceAll("[\\s<>\"{}|\\\\^`\\[\\]]", "");
    }
}
