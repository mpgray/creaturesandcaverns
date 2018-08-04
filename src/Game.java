import java.util.*;

public class Game {
    private HashMap<String, Actor> creaturePresets;
    private HashMap<String, Actor> playerPresets;
    private TreeMap<String, Actor> players;
    private Actor currentPlayer;
    private Actor currentTarget;
    private boolean hit;
    private int damage;
    private String battleReport;

    public Game(){
        creaturePresets = new ActorPresets().creatures;
        playerPresets = new ActorPresets().playerPresets;
        players = new TreeMap<>();
    }

    public void addPlayer(String playerName, String playerType){
        players.put(playerName, playerPresets.get(playerType));
    }

    public String runCombat(String playerName, String targetName){
        currentPlayer = players.get(playerName);
        currentTarget = players.get(targetName);

        if(currentPlayer.getAttackDie().getLastRoll() >= currentTarget.getArmorClass()){
            hit = true;
            damage = currentPlayer.getLastDamageTotal();
            currentTarget.setCurrentHitPoints(currentTarget.getCurrentHitPoints() - damage);
            if(currentTarget.getCurrentHitPoints() <= 0){
                currentTarget.setIsDead(true);
            }
        } else {
            hit = false;
        }

        String attackName = currentPlayer.getAttackName();
        String defenseName = currentTarget.getDefenseName();

        if(currentTarget.getIsDead()){
            battleReport = (playerName + " killed " + targetName + " with " + attackName + " for " + damage + "!");
        } else {
            battleReport = hit ? (playerName + " hit " + targetName + " with " + attackName + " for " + damage + "!") :
                    (targetName + " avoided damage from " + playerName + " with " + defenseName + "!");
        }

        return battleReport;
    }

    public void addRandomMonster(){
        Object[] creatureKeys = creaturePresets.keySet().toArray();
        Object key = creatureKeys[new Random().nextInt(creatureKeys.length)];
        players.put(creaturePresets.get(key).getType(), creaturePresets.get(key));
    }

    public TreeMap<String, Actor> getCurrentPlayers(){
        return players;
    }

    public int getAttackRoll(String playerName){
        return players.get(playerName).getAttackDie().getLastRoll();
    }

    public int[] getDamageRolls(String playerName){
        int n = players.get(playerName).getDamageDice().toArray().length;
        int[] vals = new int[n];
        int i = 0;
        for(Die d : players.get(playerName).getDamageDice()){
            vals[i] = d.getLastRoll();
            i++;
        }
        return vals;
    }

    public int getDamageDieSides(String playerName){
        return players.get(playerName).getDamageDice().get(0).getNumSides();
    }

    public String getPlayerStats(String playerName){
        return players.get(playerName).toString();
    }

    private Actor aiTargetSelect(String creatureName){
        Die D100 = new Die(100);
        int greatestChance = 0;
        int chanceAttacks = 0;
        Actor willAttack = players.firstEntry().getValue();
        for (Iterator<Actor> it = players.values().iterator(); it.hasNext();) {
            Actor actor = it.next();
            chanceAttacks = D100.rollDie();
            if(actor.getType().equals("Fighter")){
                chanceAttacks += 50;
            }
            else if(actor.getType().equals("Mage")){
                chanceAttacks += 20;
            }
            else if(actor.getType().equals("Rogue")){
                chanceAttacks += 15;
            }
            else{
                chanceAttacks = -1000; //so it does not attack itself
            }
            if(actor.getCurrentHitPoints() < 10){
                chanceAttacks += 25;
            }
            if (chanceAttacks > greatestChance){
               willAttack = actor;
               greatestChance = chanceAttacks;
            }
        }
        return willAttack;
    }

    public ArrayList<String> getNames(){
        ArrayList<String> eachName = new ArrayList<>();
        for (Iterator<String> it = getCurrentPlayers().keySet().iterator(); it.hasNext();) {
            String name = it.next();
            eachName.add(name);
        }
        return eachName;
    }

    public ArrayList<String> getScoreBoard(){
        ArrayList<String> eachActor = new ArrayList<>();
        for (Iterator<Actor> it = getCurrentPlayers().values().iterator(); it.hasNext();) {
            Actor actor = it.next();
            eachActor.add(actor.toString());
        }
        return eachActor;
    }
}
