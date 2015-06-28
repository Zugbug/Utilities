/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tk.zugbug.javaUtils;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zugbug
 */
public class RightAlignedFormatStream extends PrintStream {

    int longestString = 0;

    public RightAlignedFormatStream(OutputStream out) {
        super(out);
    }

    @Override
    public void println(String string) {
        StringBuilder temp = new StringBuilder(string);
        int lengthDifference = string.length() - longestString;
        if (lengthDifference > 0) {
            for (int i = 0; i < lengthDifference; i++) {
                insertSpace();
            }
            longestString = temp.length();
        }
        if (lengthDifference < 0) {
            for (int i = 0; i < -lengthDifference; i++) {
                temp.insert(0, " ");
            }
        }
        sb.add(new StringBuffer(temp.append("\n")));
    }

    void insertSpace() {
        sb.stream().forEach((s) -> {
            s.insert(0, " ");
        });
    }
    List<StringBuffer> sb = new ArrayList<>();

    @Override
    public void println() {
        flush();
    }

    @Override
    public void flush() {
        super.flush();
        sb.stream().forEach(super::print);
    }

}
