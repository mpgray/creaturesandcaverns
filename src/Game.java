import org.json.JSONObject;

import java.util.*;

public class Game {
    private HashMap<String, Actor> creaturePresets;
    private HashMap<String, Actor> playerPresets;
    private TreeMap<String, Actor> players;
    private Actor creature;
    private Actor player;
    private Actor target;
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

    public String playerTurn(String playerName, String targetName){
        player = players.get(playerName);
        target = players.get(targetName);

        if(player.getAttackRoll() >= target.getArmorClass()){
            hit = true;
            damage = player.getDamageRoll();
            target.setCurrentHitPoints(target.getCurrentHitPoints() - damage);
        } else {
            hit = false;
        }

        return battleReport();
    }

    private String battleReport(){
        String playerName, targetName, playerAttack, targetDefense;
        //TODO
        return null;
    }

    public void randomMonster(){
        Object[] creatureKeys = creaturePresets.keySet().toArray();
        Object key = creatureKeys[new Random().nextInt(creatureKeys.length)];
        players.put("Enemy Creature", creaturePresets.get(key));
    }



}
