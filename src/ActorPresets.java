import java.util.HashMap;

public class ActorPresets {

    public HashMap<String, Actor> playerPresets = new HashMap<>();
    public HashMap<String, Actor> creatures = new HashMap<>();

    private String type;
    private int hitPoints;
    private int armorClass;
    private int damageDie;
    private int numDamageDice;
    private String attackName;
    private String defenseName;

    public ActorPresets(){
        fighter();
        rogue();
        mage();
        rat();
        bear();
        dragon();
    }

    private void fighter(){
        type = "Fighter";
        hitPoints = 50;
        armorClass = 18;
        damageDie = 12;
        numDamageDice = 1;
        attackName = "Thrust";
        defenseName = "Armor";
        Actor fighter = new Actor(type, hitPoints, armorClass, damageDie, numDamageDice, attackName, defenseName);
        playerPresets.put(type, fighter);
    }

    private void rogue(){
        type = "Rogue";
        hitPoints = 40;
        armorClass = 15;
        damageDie = 8;
        numDamageDice = 2;
        attackName = "Backstab";
        defenseName = "Dodge";
        Actor rogue = new Actor(type, hitPoints, armorClass, damageDie, numDamageDice, attackName, defenseName);
        playerPresets.put(type, rogue);
    }

    private void mage(){
        type = "Mage";
        hitPoints = 30;
        armorClass = 12;
        damageDie = 6;
        numDamageDice = 3;
        attackName = "Magic Missile";
        defenseName = "Magic Armor";
        Actor mage = new Actor(type, hitPoints, armorClass, damageDie, numDamageDice, attackName, defenseName);
        playerPresets.put(type, mage);
    }

    private void rat(){
        type = "Rat";
        hitPoints = 8;
        armorClass = 8;
        damageDie = 4;
        numDamageDice = 1;
        attackName = "Bite";
        defenseName = "Dodge";
        Actor rat = new Actor(type, hitPoints, armorClass, damageDie, numDamageDice, attackName, defenseName);
        creatures.put(type, rat);
    }

    private void bear(){
        type = "Bear";
        hitPoints = 40;
        armorClass = 10;
        damageDie = 8;
        numDamageDice = 1;
        attackName = "Slash";
        defenseName = "Fur Armor";
        Actor bear = new Actor(type, hitPoints, armorClass, damageDie, numDamageDice, attackName, defenseName);
        creatures.put(type, bear);
    }

    private void dragon(){
        type = "Dragon";
        hitPoints = 100;
        armorClass = 12;
        damageDie = 12;
        numDamageDice = 2;
        attackName = "Fire Breath";
        defenseName = "Scale Armor";
        Actor dragon = new Actor(type, hitPoints, armorClass, damageDie, numDamageDice, attackName, defenseName);
        creatures.put(type, dragon);
    }

}


