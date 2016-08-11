/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author zugbug
 */
public class Tokeniser {

    public static void main(String[] args) {
        String things = "<html style=\"bold\" type=\"rough\" and red> </html>";
        List t = Tokeniser.tokenise(things);
        System.err.println(t);

    }
    public static List<String> tokenise(String src){
        return tokenise(src, ' ').stream().map(Object::toString).collect(Collectors.toList());
    }
    public static List<StringBuilder> tokenise(String src, char del) {
        if (del == 0) {
            return Arrays.asList(new StringBuilder(src));
        }
        List<StringBuilder> ret = new ArrayList<>();
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < src.toCharArray().length; i++) {
            char c = src.toCharArray()[i];
            if (c == '"') {
                int skip = src.substring(i + 1).indexOf('"') + 1;
                buf.append(src.substring(i + 1, i + skip));
                i += skip;
            } else if (c == del) {
                ret.add(buf);
                buf = new StringBuilder();
            } else {
                buf.append(c);
            }
        }
        if (buf.length() != 0) {
            ret.add(buf);
        }
        return ret;
    }

}
