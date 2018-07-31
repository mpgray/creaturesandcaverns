public class Player {

    private Attributes attributes;
    public String UserName;

    public Player(String userName){
        UserName = userName;
    }
    public Attributes fighter() {
        Attributes myAttributes = new Attributes("Fighter");
        myAttributes.TotalHealth = 50;
        myAttributes.CurrentHealth = 50;
        myAttributes.Defence = 18;
        myAttributes.Offence = 4;
        myAttributes.damageDie = 12;
        myAttributes.NameHealth = "Health";
        myAttributes.NameDefence = "Armor";
        myAttributes.NameOffence = "Thrust";
        return myAttributes;
    }
    public Attributes rogue() {
        Attributes myAttributes = new Attributes("Rogue");
        myAttributes.TotalHealth = 40;
        myAttributes.CurrentHealth = 40;
        myAttributes.Defence = 15;
        myAttributes.Offence = 5;
        myAttributes.damageDie = 8;
        myAttributes.numDamageDie = 2;
        myAttributes.NameHealth = "Health";
        myAttributes.NameDefence = "Dodge";
        myAttributes.NameOffence = "Slashed";
        return myAttributes;
    }
    public Attributes mage() {
        Attributes myAttributes = new Attributes("Mage");
        myAttributes.TotalHealth = 30;
        myAttributes.CurrentHealth = 30;
        myAttributes.Defence = 12;
        myAttributes.Offence = 6;
        myAttributes.damageDie = 6;
        myAttributes.numDamageDie = 3;
        myAttributes.NameHealth = "Health";
        myAttributes.NameDefence = "Magic Shield";
        myAttributes.NameOffence = "Cast";
        return myAttributes;
    }
}
