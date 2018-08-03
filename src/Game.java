import java.util.*;

public class Game {
    private HashMap<String, Actor> creaturePresets;
    private HashMap<String, Actor> playerPresets;
    private TreeMap<String, Actor> players;
    private Actor currentPlayer;
    private Actor currentTarget;
    private boolean hit;
    private int damage;

    public Game(){
        creaturePresets = new ActorPresets().creatures;
        playerPresets = new ActorPresets().playerPresets;
        players = new TreeMap<>();
    }

    public void addPlayer(String playerName, String playerType){
        players.put(playerName, playerPresets.get(playerType));
    }

    public String initiateCombat(String playerName, String targetName){
        currentPlayer = players.get(playerName);
        currentTarget = players.get(targetName);

        if(currentPlayer.rollAttack() >= currentTarget.getArmorClass()){
            hit = true;
            damage = currentPlayer.rollDamage();
            currentTarget.setCurrentHitPoints(currentTarget.getCurrentHitPoints() - damage);
        } else {
            hit = false;
        }

        String attackName = currentPlayer.getAttackName();
        String defenseName = currentTarget.getDefenseName();

        return hit ? (playerName + " hit " + targetName + " with " + attackName + " for " + damage + "!") :
                (targetName + " avoided damage with " + defenseName + " from " + playerName + "!");
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

    public String getPlayerStats(String playerName){
        return players.get(playerName).toString();
    }

    public String[] getScoreBoard(){
        //TODO Format current players and health/stats for display in gui
        return null;
    }



}
