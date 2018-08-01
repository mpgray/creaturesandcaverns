import java.util.*;

public class Game {
    private HashMap<String, Actor> creaturePresets;
    private TreeMap<String, Actor> players;
    private Actor creature;

    public Game(){
        creaturePresets = new ActorPresets().creatures;
        players = new TreeMap<>();
        players.put("Enemy Creature", randomMonster());

    }

    public void addPlayer(String playerName, Actor playerType){
        players.put(playerName, playerType);
    }

    public Actor randomMonster(){
        Object[] creatureKeys = creaturePresets.keySet().toArray();
        Object key = creatureKeys[new Random().nextInt(creatureKeys.length)];
        return creaturePresets.get(key);
    }

}
