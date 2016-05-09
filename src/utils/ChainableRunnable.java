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

    public static ChainableRunnable build(Runnable... rs) {
        ChainableRunnable ret = empty();
        for (Runnable r : rs) {
            ret = ret.andThen(r);
        }
        return ret;
    }

    default ChainableRunnable andThen(Runnable r) {
        if (r == null) {
            return this::run;
        }
        return () -> {
            this.run();
            r.run();
        };
    }
}
