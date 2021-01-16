package org.openjfx.model;

public class TestScore {

    private int testID;
    private int traineeID;
    private int score;

    /**
     * Constructor
     * @param tID
     * @param trainID
     * @param scr
     */
    public TestScore(int tID, int trainID, int scr){

        testID = tID;
        traineeID = trainID;
        score = scr;

    }

    //Getters
    public int getTestID() {
        return testID;
    }

    public int getTraineeID() {
        return traineeID;
    }

    public int getScore() {
        return score;
    }

    //Setters
    public void setTestID(int testID) {
        this.testID = testID;
    }

    public void setTraineeID(int traineeID) {
        this.traineeID = traineeID;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
