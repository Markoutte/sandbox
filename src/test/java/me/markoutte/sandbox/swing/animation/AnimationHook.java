package me.markoutte.sandbox.swing.animation;

/**
 * Animation hook is used for blocking current thread, while animation is not finished
 *
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2015-07-17
 */
public interface AnimationHook {
    /**
     * Blocking current thread while animation in progress
     *
     * @throws InterruptedException
     */
    void get() throws InterruptedException;

    /**
     * Stops animation if it not required anymore
     */
    void stop();
}
