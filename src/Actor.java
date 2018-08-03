import java.util.ArrayList;

public class Actor implements Comparable<Actor>{

    private String type;
    private int maxHitPoints;
    private int currentHitPoints;
    private int armorClass;
    private int initiative;
    private String attackName;
    private String defenseName;
    private ArrayList<Die> damageDice;
    private Die attackDie;
    private Die initiativeDie;
    private int proficiencyBonus;
    private int abilityModifier;

    public Actor(String type, int hitPoints, int armorClass, int damageDie, int numDamageDice, String attackName, String defenseName){
        this.type = type;
        this.maxHitPoints = hitPoints;
        this.currentHitPoints = hitPoints;
        this.armorClass = armorClass;
        this.attackName = attackName;
        this.defenseName = defenseName;
        this.proficiencyBonus = 2;
        this.abilityModifier = 3;
        this.attackDie = new Die(20);
        this.initiativeDie = new Die(20);
        this.damageDice = new ArrayList<>();
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

    public String getAttackName(){
        return attackName;
    }

    public String getDefenseName(){
        return defenseName;
    }

    public void setCurrentHitPoints(int currentHitPoints){
        this.currentHitPoints = currentHitPoints;
    }

    public int getProficiencyBonus(){
        return proficiencyBonus;
    }

    public void setProficiencyBonus(int proficiencyBonus){
        this.proficiencyBonus = proficiencyBonus;
    }

    public int getAbilityModifier(){
        return abilityModifier;
    }

    public int rollInitiative(){
        return initiativeDie.rollDie();
    }

    public int getInitiative(){
        return initiative;
    }

    public void setAbilityModifier(int abilityModifier){
        this.abilityModifier = abilityModifier;
    }


    public int rollAttack(){
        return attackDie.rollDie() + proficiencyBonus;
    }

    public int rollDamage(){
        int totalDamage = 0;
        for(Die d : damageDice){
            totalDamage += d.rollDie();
        }
        return totalDamage + proficiencyBonus + abilityModifier;
    }

    public ArrayList<Die> getDamageDice(){
        return damageDice;
    }

    public Die getAttackDie(){
        return attackDie;
    }

    @Override
    public String toString(){
        return "Type: " + type + "\n"
                + "HP: " + currentHitPoints + "/" + maxHitPoints + "\n"
                + "Armor Class: " + armorClass + "\n"
                + "Attack Type: " + attackName + "\n"
                + "Defense Type: " + defenseName + "\n"
                + "Proficiency Bonus: " + proficiencyBonus + "\n"
                + "Ability Modifier: " + abilityModifier + "\n";
    }

    @Override
    public int compareTo(Actor other){
        return -Integer.valueOf(this.initiative).compareTo(other.initiative);
    }


}
