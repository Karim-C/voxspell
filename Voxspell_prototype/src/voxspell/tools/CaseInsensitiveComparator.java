package voxspell.tools;

import java.util.Comparator;

/**
 * Compares strings ignoring case.
 * 
 * @author Will Molloy
 */
public class CaseInsensitiveComparator implements Comparator<String> {
    public int compare(String s1, String s2) {
        return s1.toLowerCase().compareTo(s2.toLowerCase());
    }
}