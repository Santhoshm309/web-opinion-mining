package op;


import cc.mallet.types.Sequence;

import java.util.HashMap;

public class GreetingResponse {

    int plot;

    int mood;
    int chars;
    int other;
    int bgs;


    public GreetingResponse(int plot, int mood, int chars, int bgs, int other){
        this.plot = plot;
        this.mood = mood;

        this.bgs = bgs;
        this.other = other;
        this.chars = chars;
    }


    public int getPlot() {
        return plot;
    }



    public int getMood() {
        return mood;
    }


    public int getChars() {
        return chars;
    }
    public int getOther() {
        return other;
    }


    public int getBgs() {
        return bgs;
    }



}
