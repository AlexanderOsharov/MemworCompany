package com.shurik.memwor_24.pumpwimo.models;

// квест
public class Quest {

    private String questName; // название квеста
    private int questDifficultyLevel; // уровень сложности кветста
    private int questImage; // картинка квеста

    // Constructor
    public Quest(String questName, int questDifficultyLevel, int questImage) {
        this.questName = questName;
        this.questDifficultyLevel = questDifficultyLevel;
        this.questImage = questImage;
    }

    public String getQuestName() {
        return questName;
    }

    public int getQuestDifficultyLevel() {
        return questDifficultyLevel;
    }

    public int getQuestImage() {
        return questImage;
    }

    public void setQuestName(String questName) {
        this.questName = questName;
    }

    public void setQuestDifficultyLevel(int questDifficultyLevel) {
        this.questDifficultyLevel = questDifficultyLevel;
    }

    public void setQuestImage(int questImage) {
        this.questImage = questImage;
    }
}
