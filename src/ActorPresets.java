import java.util.HashMap;

public class ActorPresets {

    public HashMap<String, Actor> playerPresets = new HashMap<>();
    public HashMap<String, Actor> creatures = new HashMap<>();

    private String type;
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
        hitPoints = 13;
        armorClass = 16;
        damageDie = 12;
        numDamageDice = 1;
        attackName = "Thrust";
        defenseName = "Armor";
        proficiencyBonus = 2;
        abilityModifier = 3;
        Actor fighter = new Actor(type, hitPoints, armorClass, damageDie, numDamageDice, attackName, defenseName, proficiencyBonus, abilityModifier);
        playerPresets.put(type, fighter);
    }

    private void rogue(){
        type = "Rogue";
        hitPoints = 10;
        armorClass = 14;
        damageDie = 6;
        numDamageDice = 2;
        attackName = "Backstab";
        defenseName = "Dodge";
        proficiencyBonus = 2;
        abilityModifier = 3;
        Actor rogue = new Actor(type, hitPoints, armorClass, damageDie, numDamageDice, attackName, defenseName, proficiencyBonus, abilityModifier);
        playerPresets.put(type, rogue);
    }

    private void mage(){
        type = "Mage";
        hitPoints = 8;
        armorClass = 12;
        damageDie = 4;
        numDamageDice = 3;
        attackName = "Magic Missile";
        defenseName = "Magic Armor";
        proficiencyBonus = 2;
        abilityModifier = 3;
        Actor mage = new Actor(type, hitPoints, armorClass, damageDie, numDamageDice, attackName, defenseName, proficiencyBonus, abilityModifier);
        playerPresets.put(type, mage);
    }

    private void lion(){
        type = "Lion";
        hitPoints = 26;
        armorClass = 12;
        damageDie = 8;
        numDamageDice = 1;
        attackName = "Bite";
        defenseName = "Natural Armor";
        proficiencyBonus = 5;
        abilityModifier = 3;
        Actor lion = new Actor(type, hitPoints, armorClass, damageDie, numDamageDice, attackName, defenseName, proficiencyBonus, abilityModifier);
        creatures.put(type, lion);
    }

    private void bear(){
        type = "Bear";
        hitPoints = 34;
        armorClass = 11;
        damageDie = 6;
        numDamageDice = 2;
        attackName = "Slash";
        defenseName = "Natural Armor";
        proficiencyBonus = 5;
        abilityModifier = 4;
        Actor bear = new Actor(type, hitPoints, armorClass, damageDie, numDamageDice, attackName, defenseName, proficiencyBonus, abilityModifier);
        creatures.put(type, bear);
    }

    private void dragon(){
        type = "Dragon";
        hitPoints = 50;
        armorClass = 14;
        damageDie = 6;
        numDamageDice = 3;
        attackName = "Fire Breath";
        defenseName = "Scale Armor";
        proficiencyBonus = 5;
        abilityModifier = 3;
        Actor dragon = new Actor(type, hitPoints, armorClass, damageDie, numDamageDice, attackName, defenseName, proficiencyBonus, abilityModifier);
        creatures.put(type, dragon);
    }

    private void wolf(){
        type = "Wolf";
        hitPoints = 37;
        armorClass = 14;
        damageDie = 6;
        numDamageDice = 2;
        attackName = "Bite";
        defenseName = "Natural Armor";
        proficiencyBonus = 5;
        abilityModifier = 3;
        Actor wolf = new Actor(type, hitPoints, armorClass, damageDie, numDamageDice, attackName, defenseName, proficiencyBonus, abilityModifier);
        creatures.put(type, wolf);
    }

}


