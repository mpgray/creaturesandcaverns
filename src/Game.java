import java.util.*;

public class Game {
    private Map<String, Actor> creaturePresets;
    private Map<String, Actor> playerPresets;
    private Map<String, Actor> players;
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

    public String battleReport(String playerName, String targetName){
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
        players.put("Creature", creaturePresets.get(key));
    }

    public String[] getScoreBoard(){
        //TODO Format current players and health/stats for display in gui
        return null;
    }



}
