package com.pulamsi.photomanager.utils;

import android.content.ContentUris;
import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * @author chiron
 */
public class StringUtils {

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }


    public static long parseLong(Uri uri, long defaultValue) {
        try {
            return ContentUris.parseId(uri);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static long parseLong(String str) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return 0;
        }

    }

    public static int parseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }

    }

    public static String escapeSql(String str) {
        if (str == null) {
            return null;
        }
        String repl = "'";
        String with = "''";
        int max = -1;

        StringBuffer buf = new StringBuffer(str.length());
        int start = 0, end;
        while ((end = str.indexOf(repl, start)) != -1) {
            buf.append(str.substring(start, end)).append(with);
            start = end + repl.length();

            if (--max == 0) {
                break;
            }
        }
        buf.append(str.substring(start));
        return buf.toString();
    }

    public static String escapeSqlToLowerCase(String str) {
        String ret = escapeSql(str);
        return TextUtils.isEmpty(ret) ? ret : ret.toLowerCase();
    }

    /**
     * <p>
     * Case insensitive check if a String starts with a specified prefix.
     * </p>
     * <p/>
     * <p>
     * <code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered to be equal. The comparison is case
     * insensitive.
     * </p>
     * <p/>
     * <pre>
     * StringUtils.startsWithIgnoreCase(null, null)      = true
     * StringUtils.startsWithIgnoreCase(null, "abc")     = false
     * StringUtils.startsWithIgnoreCase("abcdef", null)  = false
     * StringUtils.startsWithIgnoreCase("abcdef", "abc") = true
     * StringUtils.startsWithIgnoreCase("ABCDEF", "abc") = true
     * </pre>
     *
     * @param str    the String to check, may be null
     * @param prefix the prefix to find, may be null
     * @return <code>true</code> if the String starts with the prefix, case
     * insensitive, or both <code>null</code>
     * @see String#startsWith(String)
     * @since 2.4
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        return startsWith(str, prefix, true);
    }

    /**
     * <p>
     * Check if a String starts with a specified prefix (optionally case
     * insensitive).
     * </p>
     *
     * @param str        the String to check, may be null
     * @param prefix     the prefix to find, may be null
     * @param ignoreCase inidicates whether the compare should ignore case (case
     *                   insensitive) or not.
     * @return <code>true</code> if the String starts with the prefix or both
     * <code>null</code>
     * @see String#startsWith(String)
     */
    private static boolean startsWith(String str, String prefix, boolean ignoreCase) {
        if (str == null || prefix == null) {
            return (str == null && prefix == null);
        }
        if (prefix.length() > str.length()) {
            return false;
        }
        return str.regionMatches(ignoreCase, 0, prefix, 0, prefix.length());
    }

    /**
     * <p>
     * Replaces a String with another String inside a larger String, once.
     * </p>
     * <p/>
     * <p>
     * A <code>null</code> reference passed to this method is a no-op.
     * </p>
     * <p/>
     * <pre>
     * StringUtils.replaceOnce(null, *, *)        = null
     * StringUtils.replaceOnce("", *, *)          = ""
     * StringUtils.replaceOnce("any", null, *)    = "any"
     * StringUtils.replaceOnce("any", *, null)    = "any"
     * StringUtils.replaceOnce("any", "", *)      = "any"
     * StringUtils.replaceOnce("aba", "a", null)  = "aba"
     * StringUtils.replaceOnce("aba", "a", "")    = "ba"
     * StringUtils.replaceOnce("aba", "a", "z")   = "zba"
     * </pre>
     *
     * @param text         text to search and replace in, may be null
     * @param searchString the String to search for, may be null
     * @param replacement  the String to replace with, may be null
     * @return the text with any replacements processed, <code>null</code> if
     * null String input
     * @see #replace(String text, String searchString, String replacement, int
     * max)
     */
    public static String replaceOnce(String text, String searchString, String replacement) {
        return replace(text, searchString, replacement, 1);
    }

    /**
     * <p>
     * Replaces a String with another String inside a larger String, for the
     * first <code>max</code> values of the search String.
     * </p>
     * <p/>
     * <p>
     * A <code>null</code> reference passed to this method is a no-op.
     * </p>
     * <p/>
     * <pre>
     * StringUtils.replace(null, *, *, *)         = null
     * StringUtils.replace("", *, *, *)           = ""
     * StringUtils.replace("any", null, *, *)     = "any"
     * StringUtils.replace("any", *, null, *)     = "any"
     * StringUtils.replace("any", "", *, *)       = "any"
     * StringUtils.replace("any", *, *, 0)        = "any"
     * StringUtils.replace("abaa", "a", null, -1) = "abaa"
     * StringUtils.replace("abaa", "a", "", -1)   = "b"
     * StringUtils.replace("abaa", "a", "z", 0)   = "abaa"
     * StringUtils.replace("abaa", "a", "z", 1)   = "zbaa"
     * StringUtils.replace("abaa", "a", "z", 2)   = "zbza"
     * StringUtils.replace("abaa", "a", "z", -1)  = "zbzz"
     * </pre>
     *
     * @param text         text to search and replace in, may be null
     * @param searchString the String to search for, may be null
     * @param replacement  the String to replace it with, may be null
     * @param max          maximum number of values to replace, or <code>-1</code> if no
     *                     maximum
     * @return the text with any replacements processed, <code>null</code> if
     * null String input
     */
    public static String replace(String text, String searchString, String replacement, int max) {
        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(searchString) || replacement == null
                || max == 0) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == -1) {
            return text;
        }
        int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = (increase < 0 ? 0 : increase);
        increase *= (max < 0 ? 16 : (max > 64 ? 64 : max));
        StringBuilder buf = new StringBuilder(text.length() + increase);
        while (end != -1) {
            buf.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }

    public static boolean containIgnoreCase(String str1, String str2) {
        if (TextUtils.isEmpty(str1) || TextUtils.isEmpty(str2)) {
            return false;
        }
        str1 = str1.toLowerCase();
        str2 = str2.toLowerCase();
        return str1.contains(str2);
    }

    public static String arrayToString(ArrayList<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        int index = 0;
        for (String str : list) {
            if (index > 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(str);
            index++;
        }
        return stringBuilder.toString();
    }

}
