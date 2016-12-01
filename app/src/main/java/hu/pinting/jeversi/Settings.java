package hu.pinting.jeversi;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import hu.pinting.jeversi.core.Cell;

public class Settings {
    private Cell player;
    private double difficulty;
    private int size;

    /**
     * Load settings from PreferenceManager.
     * @param context
     */
    public void load(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);

        player = Cell.fromNum(Integer.parseInt(settings.getString("player", "1")));
        difficulty = (double)Integer.parseInt(settings.getString("difficulty", "100")) / 100;
        size = Integer.parseInt(settings.getString("size", "8"));
    }

    /**
     * Get the player.
     * @return
     */
    public Cell getPlayer() {
        return player;
    }

    /**
     * Get the difficulty.
     * @return
     */
    public double getDifficulty() {
        return difficulty;
    }

    /**
     * Get the size.
     * @return
     */
    public int getSize() {
        return size;
    }
}
