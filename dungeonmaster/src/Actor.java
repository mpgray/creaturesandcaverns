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
    private int level;
    private boolean isDead;
    private int totalDamage;
    private boolean isPlayer;

    public Actor(String type, int level, int hitPoints, int armorClass, int damageDie, int numDamageDice, String attackName, String defenseName, int proficiencyBonus, int abilityModifier){
        this.type = type;
        this.level = level;
        this.maxHitPoints = hitPoints;
        this.currentHitPoints = hitPoints;
        this.armorClass = armorClass;
        this.attackName = attackName;
        this.defenseName = defenseName;
        this.proficiencyBonus = proficiencyBonus;
        this.abilityModifier = abilityModifier;
        this.attackDie = new Die(20);
        this.initiativeDie = new Die(20);
        this.damageDice = new ArrayList<>();
        for(int i = 0; i < numDamageDice; i++){
            damageDice.add(new Die(damageDie));
        }
        this.initiative = this.rollInitiative();
        this.isDead = false;
        this.isPlayer = false;

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

    public int getLevel(){
        return level;
    }

    public void setLevel(int level){
        this.level = level;
    }


    public int rollAttack(){
        return attackDie.rollDie() + proficiencyBonus + abilityModifier;
    }

    public int rollDamage(){
        totalDamage = 0;
        for(Die d : damageDice){
            totalDamage += d.rollDie();
        }
        totalDamage += abilityModifier;
        return totalDamage;
    }

    public void setInitiative(int initiative){
        this.initiative = initiative;
    }

    public ArrayList<Die> getDamageDice(){
        return damageDice;
    }

    public int getLastDamageTotal(){
        return totalDamage;
    }

    public Die getAttackDie(){
        return attackDie;
    }

    public boolean getIsDead(){
        return isDead;
    }

    public void setIsDead(boolean isDead){
        this.isDead = isDead;
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    public void setIsPlayer(boolean player) {
        isPlayer = player;
    }

    @Override
    public String toString(){
        return "Type: " + type + "<br /><\n"
                + "HP: " + currentHitPoints + "/" + maxHitPoints + "<br />\n"
                + defenseName + ": " + armorClass + "<br />\n"
                + attackName + ": " + getDamageDice().size() + "D" + getDamageDice().get(0).getNumSides() + "(" + proficiencyBonus +")<br />\n"
                + "Ability Modifier: " + abilityModifier + "<br />\n";
    }

    @Override
    public int compareTo(Actor other){
        return -Integer.valueOf(this.initiative).compareTo(other.initiative);
    }

}
