package modules;

import java.util.Random;

public class Die {
    private int numSides;
    private Random rand = new Random();
    private int lastRoll;

    public Die(int numSides){
        this.numSides = numSides;
    }

    public int rollDie(){
        lastRoll = (rand.nextInt(numSides) + 1);
        return lastRoll;
    }

    public int getLastRoll(){
        return lastRoll;
    }

    public int getNumSides(){
        return numSides;
    }
}
