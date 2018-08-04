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

    public String fight(String playerName, String targetName){
        currentPlayer = players.get(playerName);
        currentTarget = players.get(targetName);

        if(currentPlayer.rollAttack() >= currentTarget.getArmorClass()){
            hit = true;
            damage = currentPlayer.rollDamage();
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

    private String aiTargetSelect(String creatureName){
        //TODO Create method for decided who the creature targets.
        return null;
    }

    public ArrayList<String> getScoreBoard(){
        ArrayList<String> eachActor = new ArrayList<>();
        for (Iterator<Actor> it = players.values().iterator(); it.hasNext();) {
            Actor actor = it.next();
            eachActor.add(actor.toString());
        }
        return eachActor;
    }
}
