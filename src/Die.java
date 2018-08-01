import java.util.Map;
import java.util.Random;

public class Die {
    private int numSides;
    private Random rand;

    public Die(int numSides){
        this.numSides = numSides;
    }

    public int rollDie(){
        return rand.nextInt(numSides) + 1;
    }

}
