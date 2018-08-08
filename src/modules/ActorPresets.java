package modules;

import modules.Actor;

import java.util.HashMap;

public class ActorPresets {

    public HashMap<String, Actor> playerPresets = new HashMap<>();
    public HashMap<String, Actor> creatures = new HashMap<>();

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
        lion();
        bear();
        wolf();
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
        fighter.setPlayer(true);
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
        rogue.setPlayer(true);
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
        mage.setPlayer(true);
        playerPresets.put(type, mage);
    }

    private void lion(){
        type = "Lion";
        level = 1;
        hitPoints = 26;
        armorClass = 12;
        damageDie = 8;
        numDamageDice = 1;
        attackName = "Bite";
        defenseName = "Natural Armor";
        proficiencyBonus = 5;
        abilityModifier = 3;
        Actor lion = new Actor(type, level, hitPoints, armorClass, damageDie, numDamageDice, attackName, defenseName, proficiencyBonus, abilityModifier);
        lion.setPlayer(false);
        creatures.put(type, lion);
    }

    private void bear(){
        type = "Bear";
        level = 1;
        hitPoints = 34;
        armorClass = 11;
        damageDie = 6;
        numDamageDice = 2;
        attackName = "Slash";
        defenseName = "Natural Armor";
        proficiencyBonus = 5;
        abilityModifier = 4;
        Actor bear = new Actor(type, level, hitPoints, armorClass, damageDie, numDamageDice, attackName, defenseName, proficiencyBonus, abilityModifier);
        bear.setPlayer(false);
        creatures.put(type, bear);
    }

    private void dragon(){
        type = "Dragon";
        level = 2;
        hitPoints = 50;
        armorClass = 14;
        damageDie = 6;
        numDamageDice = 3;
        attackName = "Fire Breath";
        defenseName = "Scale Armor";
        proficiencyBonus = 5;
        abilityModifier = 3;
        Actor dragon = new Actor(type, level, hitPoints, armorClass, damageDie, numDamageDice, attackName, defenseName, proficiencyBonus, abilityModifier);
        dragon.setPlayer(false);
        creatures.put(type, dragon);
    }

    private void wolf(){
        type = "Wolf";
        level = 1;
        hitPoints = 37;
        armorClass = 14;
        damageDie = 6;
        numDamageDice = 2;
        attackName = "Bite";
        defenseName = "Natural Armor";
        proficiencyBonus = 5;
        abilityModifier = 3;
        Actor wolf = new Actor(type, level, hitPoints, armorClass, damageDie, numDamageDice, attackName, defenseName, proficiencyBonus, abilityModifier);
        wolf.setPlayer(false);
        creatures.put(type, wolf);
    }

}


