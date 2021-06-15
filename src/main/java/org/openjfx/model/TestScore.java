package org.openjfx.model;

/**
 * Represents a test's score. Holds the corresponding trainee, test, and score.
 */
public class TestScore {

    private int testID;
    private int traineeID;
    private double score;

    /**
     * Constructor
     * @param tID
     * @param trainID
     * @param scr
     */
    public TestScore(int tID, int trainID, double scr){

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

    public double getScore() {
        return score;
    }

    //Setters
    public void setTestID(int testID) {
        this.testID = testID;
    }

    public void setTraineeID(int traineeID) {
        this.traineeID = traineeID;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
