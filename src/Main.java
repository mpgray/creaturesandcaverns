import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Player newPlayer  = new Player("MyUsername");
        Attributes playerAttr = newPlayer.fighter();
        Creature newCreature = new Creature();
        Attributes creatureAttr = newCreature.bear();
        Game newGame = new Game();
        try {
            while (playerAttr.CurrentHealth > 0 && creatureAttr.CurrentHealth > 0) {
                System.out.printf("%s: %s, %s: %d %s: %d %s: %d/%d\n",
                        newPlayer.UserName, playerAttr.Name, playerAttr.NameOffence, playerAttr.Offence, playerAttr.NameDefence, playerAttr.Defence,
                        playerAttr.NameHealth, playerAttr.CurrentHealth, playerAttr.TotalHealth);
                System.out.printf("%s: %s: %d %s: %d %s: %d/%d\n",
                        creatureAttr.Name, creatureAttr.NameOffence, creatureAttr.Offence, creatureAttr.NameDefence, creatureAttr.Defence,
                        creatureAttr.NameHealth, creatureAttr.CurrentHealth, creatureAttr.TotalHealth);

                String line = scanner.nextLine();
                System.out.println(newGame.combat(playerAttr, creatureAttr)+"\n");
                System.out.println(newGame.combat(creatureAttr, playerAttr)+"\n");
            }
        }
        catch(IllegalStateException | NoSuchElementException e) {
            // System.in has been closed
            System.out.println("System.in was closed; exiting");
        }
        if(newGame.isDead(creatureAttr)){
            System.out.println("The "+creatureAttr.Name + " is dead");
        }
        if(newGame.isDead(playerAttr)){
            System.out.println("The "+playerAttr.Name + " is dead");
        }

    }
}
