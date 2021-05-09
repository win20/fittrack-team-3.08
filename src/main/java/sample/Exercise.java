package sample;

public class Exercise {

    private String exerciseType;
    private int exerciseDuration;


    Exercise(){
    }

    Exercise(String exerciseType, int exerciseDuration){
        this.exerciseType = exerciseType;
        this.exerciseDuration = exerciseDuration;

    }
    public int getExerciseDuration() {
        return exerciseDuration;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public void setExerciseDuration(int exerciseDuration) {
        this.exerciseDuration = exerciseDuration;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }
}
