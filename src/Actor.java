import java.util.ArrayList;

public class Actor {

    private String name;
    private int maxHitPoints;
    private int currentHitPoints;
    private int armorClass;
    private ArrayList<Die> damageDice;
    private Die d20 = new Die(20);
    private int proficiencyBonus = 2;

    public Actor(String name, int hitPoints, int armorClass, int damageDie, int numDamageDice){
        this.name = name;
        this.maxHitPoints = hitPoints;
        this.currentHitPoints = hitPoints;
        this.armorClass = armorClass;
        for(int i = 0; i < numDamageDice; i++){
            damageDice.add(new Die(damageDie));
        }
    }

    public String getName(){
        return name;
    }

    public int getCurrentHitPoints(){
        return currentHitPoints;
    }

    public int getMaxHitPoints(){
        return maxHitPoints;
    }

    public int getArmorClass(){
        return armorClass;
    }

    public void setCurrentHitPoints(int currentHitPoints){
        this.currentHitPoints = currentHitPoints;
    }

    public int rollInitiative(){
        return d20.rollDie();
    }

    public int attackRoll(){
        return d20.rollDie() + proficiencyBonus;
    }

    public int rollDamageDice(){
        int totalDamage = 0;
        for(Die d : damageDice){
            totalDamage += d.rollDie();
        }
        return totalDamage;
    }



}
