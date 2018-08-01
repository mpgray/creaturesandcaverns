import java.util.ArrayList;

public class Actor implements Comparable<Actor>{

    private String type;
    private int maxHitPoints;
    private int currentHitPoints;
    private int armorClass;
    private int initiative;
    private String attackName;
    private String defenseName;
    private ArrayList<Die> damageDice = new ArrayList<>();
    private Die d20 = new Die(20);
    private int proficiencyBonus = 2;

    public Actor(String type, int hitPoints, int armorClass, int damageDie, int numDamageDice, String attackName, String defenseName){
        this.type = type;
        this.maxHitPoints = hitPoints;
        this.currentHitPoints = hitPoints;
        this.armorClass = armorClass;
        this.attackName = attackName;
        this.defenseName = defenseName;
        for(int i = 0; i < numDamageDice; i++){
            damageDice.add(new Die(damageDie));
        }
        initiative = this.rollInitiative();
    }

    public String getType(){
        return type;
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

    public String getAttackName(){
        return attackName;
    }

    public String getDefenseName(){
        return defenseName;
    }

    public int rollInitiative(){
        return d20.rollDie();
    }

    public int getInitiative(){
        return initiative;
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

    @Override
    public int compareTo(Actor other){
        return -Integer.valueOf(this.initiative).compareTo(other.initiative);
    }


}
