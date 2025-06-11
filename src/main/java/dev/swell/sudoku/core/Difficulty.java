package dev.swell.sudoku.core;

public enum Difficulty {
    VERY_EASY("Sério!?", 10),
    EASY("Bebê Reborn", 25),
    MEDIUM("Annabelle", 40),
    HARD("Chucky", 50),
    XTREME("Supernatural", 55); //max 55

    private final String title;
    private final int level;

    Difficulty(String name, int level) {
        title = name;
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public int getLevel() {
        return level;
    }
}
