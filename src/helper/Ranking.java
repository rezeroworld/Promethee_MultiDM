package helper;

import javafx.beans.property.*;

public class Ranking {
    private final SimpleIntegerProperty rangement;
    private final SimpleStringProperty action;
    private final SimpleFloatProperty score;

    public Ranking(Integer rangement, String action, Float score) {
        this.rangement = new SimpleIntegerProperty(rangement);
        this.action = new SimpleStringProperty(action);
        this.score = new SimpleFloatProperty(score);
    }

    public int getRangement() {
        return rangement.get();
    }

    public String getAction() {
        return action.get();
    }

    public float getScore() {
        return score.get();
    }
}
