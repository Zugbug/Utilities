package utils;

/**
 * ChainableRunnable provides pseudo-"syntactic sugar" for chanining multiple
 * runnables. There's a way to construct them functionally and parametrically.
 *
 * @author zugbug
 */
public interface ChainableRunnable extends Runnable {

    /**
     * Empty Chainable, to be used as a starting point for a chain.
     *
     * @return a runnable that does nothing
     */
    public static ChainableRunnable empty() {
        return () -> {
        };
    }

    /**
     * Builder method, provided with an array of runnables produces a single
     * chainable. First element in the array will be executed first.
     *
     * @param rs an array of runnables
     * @return a single runnable
     */
    public static ChainableRunnable build(Runnable... rs) {
        ChainableRunnable ret = empty();
        for (Runnable r : rs) {
            ret = ret.andThen(r);
        }
        return ret;
    }

    /**
     * Appends the instructions of the target runnable to this one.
     *
     * @param r the next instruction
     * @return a new Runnable where first this object's instructions are
     * executes and then the target one's
     */
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
