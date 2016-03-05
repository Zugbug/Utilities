package utils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author zugbug
 */
public interface ChainableRunnable extends Runnable {

    public static ChainableRunnable empty() {
        return () -> {
        };
    }

    ;

    default ChainableRunnable andThen(Runnable r) {
        return () -> {
            this.run();
            r.run();
        };
    }
}
