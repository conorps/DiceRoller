package com.conorstephens.diceroller;

import android.arch.lifecycle.ViewModel;

public class DiceRollViewModel extends ViewModel {
    private int sides;
    public void init(int sides){
        this.sides = sides;
    }
}
