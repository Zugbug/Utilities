/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 *
 * @author zugbug
 */
public class Main {

    public static void main(String[] args) {
        File file = new File("helo.db");
        String contents = utils.EasyFile.read(file);
        System.err.println(contents);
        try {
            utils.EasyFile.write(file, "Hello world");
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
        }
        
    }

}
