import java.util.HashMap;

public class ActorPresets {

    public static HashMap<String, Actor> library;
    private static String name;
    private static int hitPoints;
    private static int armorClass;
    private static int damageDie;
    private static int numDamageDice;

    private ActorPresets(){
    }

    private static void fighter(){
        name = "Fighter";
        hitPoints = 50;
        armorClass = 18;
        damageDie = 12;
        numDamageDice = 1;
        Actor fighter = new Actor(name, hitPoints, armorClass, damageDie, numDamageDice);
        library.put(name, fighter);
    }

    private static void rogue(){
        name = "Rogue";
        hitPoints = 40;
        armorClass = 15;
        damageDie = 8;
        numDamageDice = 2;
        Actor rogue = new Actor(name, hitPoints, armorClass, damageDie, numDamageDice);
        library.put(name, rogue);
    }

    private static void mage(){
        name = "Mage";
        hitPoints = 30;
        armorClass = 12;
        damageDie = 6;
        numDamageDice = 3;
        Actor mage = new Actor(name, hitPoints, armorClass, damageDie, numDamageDice);
    }

    private static void rat(){
        name = "Rat";
        hitPoints = 8;
        armorClass = 8;
        damageDie = 4;
        numDamageDice = 1;
        Actor rat = new Actor(name, hitPoints, armorClass, damageDie, numDamageDice);
        library.put(name, rat);
    }

    private static void bear(){
        name = "Bear";
        hitPoints = 40;
        armorClass = 10;
        damageDie = 8;
        numDamageDice = 1;
        Actor bear = new Actor(name, hitPoints, armorClass, damageDie, numDamageDice);
        library.put(name, bear);
    }

    private static void dragon(){
        name = "Dragon";
        hitPoints = 100;
        armorClass = 12;
        damageDie = 12;
        numDamageDice = 2;
        Actor dragon = new Actor(name, hitPoints, armorClass, damageDie, numDamageDice);
        library.put(name, dragon);
    }

}


