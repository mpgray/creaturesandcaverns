import java.util.HashMap;
import java.util.LinkedHashMap;

public class ActorPresets {

    public HashMap<String, Actor> playerPresets = new HashMap<>();
    public LinkedHashMap<String, Actor> creatures = new LinkedHashMap<>();

    private String type;
    private int level;
    private int hitPoints;
    private int armorClass;
    private int damageDie;
    private int numDamageDice;
    private int proficiencyBonus;
    private int abilityModifier;
    private String attackName;
    private String defenseName;

    public ActorPresets(){
        fighter();
        rogue();
        mage();
        drake();
        wyvern();
        basalisk();
        dragon();
    }

    private void fighter(){
        type = "Fighter";
        level = 1;
        hitPoints = 13;
        armorClass = 16;
        damageDie = 12;
        numDamageDice = 1;
        attackName = "Thrust";
        defenseName = "Armor";
        proficiencyBonus = 2;
        abilityModifier = 3;
        Actor fighter = new Actor(type, level, hitPoints, armorClass, damageDie, numDamageDice, attackName, defenseName, proficiencyBonus, abilityModifier);
        fighter.setIsPlayer(true);
        playerPresets.put(type, fighter);
    }

    private void rogue(){
        type = "Rogue";
        level = 1;
        hitPoints = 10;
        armorClass = 14;
        damageDie = 6;
        numDamageDice = 2;
        attackName = "Backstab";
        defenseName = "Dodge";
        proficiencyBonus = 2;
        abilityModifier = 3;
        Actor rogue = new Actor(type, level, hitPoints, armorClass, damageDie, numDamageDice, attackName, defenseName, proficiencyBonus, abilityModifier);
        rogue.setIsPlayer(true);
        playerPresets.put(type, rogue);
    }

    private void mage(){
        type = "Mage";
        level = 1;
        hitPoints = 8;
        armorClass = 12;
        damageDie = 4;
        numDamageDice = 3;
        attackName = "Magic Missile";
        defenseName = "Magic Armor";
        proficiencyBonus = 2;
        abilityModifier = 3;
        Actor mage = new Actor(type, level, hitPoints, armorClass, damageDie, numDamageDice, attackName, defenseName, proficiencyBonus, abilityModifier);
        mage.setIsPlayer(true);
        playerPresets.put(type, mage);
    }

    private void drake(){
        type = "Drake";
        level = 1;
        hitPoints = 26;
        armorClass = 11;
        damageDie = 4;
        numDamageDice = 1;
        attackName = "Bite";
        defenseName = "Natural Armor";
        proficiencyBonus = 5;
        abilityModifier = 3;
        Actor drake = new Actor(type, level, hitPoints, armorClass, damageDie, numDamageDice, attackName, defenseName, proficiencyBonus, abilityModifier);
        drake.setIsPlayer(false);
        creatures.put(type, drake);
    }

    private void wyvern(){
        type = "Wyvern";
        level = 1;
        hitPoints = 34;
        armorClass = 11;
        damageDie = 6;
        numDamageDice = 1;
        attackName = "Slash";
        defenseName = "Natural Armor";
        proficiencyBonus = 5;
        abilityModifier = 4;
        Actor wyvern = new Actor(type, level, hitPoints, armorClass, damageDie, numDamageDice, attackName, defenseName, proficiencyBonus, abilityModifier);
        wyvern.setIsPlayer(false);
        creatures.put(type, wyvern);
    }

    private void dragon(){
        type = "Dragon";
        level = 2;
        hitPoints = 50;
        armorClass = 14;
        damageDie = 6;
        numDamageDice = 2;
        attackName = "Fire Breath";
        defenseName = "Scale Armor";
        proficiencyBonus = 5;
        abilityModifier = 3;
        Actor dragon = new Actor(type, level, hitPoints, armorClass, damageDie, numDamageDice, attackName, defenseName, proficiencyBonus, abilityModifier);
        dragon.setIsPlayer(false);
        creatures.put(type, dragon);
    }

    private void basalisk(){
        type = "Basalisk";
        level = 1;
        hitPoints = 37;
        armorClass = 11;
        damageDie = 8;
        numDamageDice = 1;
        attackName = "Bite";
        defenseName = "Natural Armor";
        proficiencyBonus = 5;
        abilityModifier = 3;
        Actor basalisk = new Actor(type, level, hitPoints, armorClass, damageDie, numDamageDice, attackName, defenseName, proficiencyBonus, abilityModifier);
        basalisk.setIsPlayer(false);
        creatures.put(type, basalisk);
    }

}


