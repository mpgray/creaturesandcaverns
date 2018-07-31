public class Creature {
    private Attributes attributes;

    public Creature(){
    }
    public Attributes rat() {
        Attributes myAttributes = new Attributes("Plague Rat");
        myAttributes.CurrentHealth = 8;
        myAttributes.TotalHealth = 8;
        myAttributes.Defence = 8;
        myAttributes.Offence = -1;
        myAttributes.damageDie = 4;
        myAttributes.numDamageDie = 1;
        myAttributes.NameHealth = "Health";
        myAttributes.NameDefence = "Avoid";
        myAttributes.NameOffence = "Bit";
        return myAttributes;
    }
    public Attributes bear() {
        Attributes myAttributes = new Attributes("Brown Bear");
        myAttributes.CurrentHealth = 40;
        myAttributes.TotalHealth = 40;
        myAttributes.Defence = 10;
        myAttributes.Offence = 2;
        myAttributes.damageDie = 8;
        myAttributes.NameHealth = "Health";
        myAttributes.NameDefence = "Dodge";
        myAttributes.NameOffence = "Clawed";
        return myAttributes;
    }
    public Attributes dragon() {
        Attributes myAttributes = new Attributes("Black Dragon");
        myAttributes.TotalHealth = 100;
        myAttributes.CurrentHealth = 100;
        myAttributes.Defence = 12;
        myAttributes.Offence = 4;
        myAttributes.damageDie = 12;
        myAttributes.numDamageDie = 2;
        myAttributes.NameHealth = "Health";
        myAttributes.NameDefence = "Resist";
        myAttributes.NameOffence = "Burnt";
        return myAttributes;
    }
}
