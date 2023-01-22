package com.example.projectapp;

import java.util.Random;

public class QLearning {
    //Stress states
    private int states;
    //Counter measures to recommend
    private int actions;
    //q-values
    private double[][] qvalues;
    //discount factor
    private double discountFactor = 0.95;
    //learning rate
    private double learningRate = 0.25;
    //probability for exploration
    private double epsilon = 0.2;

    //constructor
    public QLearning(int states, int actions){
        this.states = states;
        this.actions = actions;
        qvalues = new double[states][];
        for ( int i = 0; i < states; i++ ){
            qvalues[i] = new double[actions];
        }
    }

    //gets the next action (exploration vs using qvalues)
    public int getAction(int state){
        int nextAction = -1;
        Random r = new Random();
        if(r.nextDouble() < epsilon){
            nextAction = r.nextInt(actions);
        }
        else{
            double max = qvalues[state][0];
            for(int i=1; i<actions; i++){
                if(max<qvalues[state][i]){
                    max=qvalues[state][i];
                    nextAction = i;
                }
            }
        }
        return nextAction;

    }

    //Update occurs after an action is taken
    public void updateState(int previousState, int action, double reward, int nextState){
        double[] nextActionValues = qvalues[nextState];
        double max = nextActionValues[0];
        for(int i=1; i<actions; i++){
            if(max<nextActionValues[i]) max=nextActionValues[i];
        }
        qvalues[previousState][action] += learningRate * (reward + (discountFactor * max));
    }
}
