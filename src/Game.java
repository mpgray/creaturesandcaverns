import java.util.Random;

public class Game {

    private Random rand = new Random();

    private int rollDie(int numSides){
        return rand.nextInt(numSides) + 1;
    }
    private int rollDie(int numSides, int numDice){
        int total = 0;
        for(int i = 0; i < numDice; i++){
            total += rand.nextInt(numSides) + 1;
        }
        return total;
    }

    public String combat(Attributes attacker, Attributes defender){
        int attack = attacker.Offence + this.rollDie(20);
        int damage = 0;
        String success = " misses ";
        if (attack >= defender.Defence){
            damage = rollDie(attacker.damageDie,attacker.numDamageDie);
            defender.CurrentHealth -= damage;
            success = attacker.NameOffence;
        }
        return "A " + attacker.Name +
                " attacks for " + attack + " and " + success + " a " + defender.Name + "'s " + defender.NameDefence + " doing " + damage + " damage.";
    }

    public boolean isDead(Attributes combatant){
        if (combatant.CurrentHealth < 1){
            return true;
        }
        return false;

    }

}
