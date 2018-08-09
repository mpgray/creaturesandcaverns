package modules;

import modules.Actor;
import modules.ActorPresets;
import modules.Die;

import java.util.*;

public class Game {
    private HashMap<String, Actor> creaturePresets;
    private HashMap<String, Actor> playerPresets;
    private TreeMap<String, Actor> players;
    private Actor currentPlayer;
    private Actor currentTarget;
    private boolean hit, gameStarted;
    private int damage;
    private String battleReport, playerTurn;

    public Game(){
        creaturePresets = new ActorPresets().creatures;
        playerPresets = new ActorPresets().playerPresets;
        players = new TreeMap<>();
        gameStarted = false;
    }

    public void startGame(){
        gameStarted = true;
        playerTurn = players.firstKey();
    }

    public void endGame(){
        gameStarted = false;
        for(String user : players.keySet()){
            players.remove(user);
        }
    }

    public void addPlayer(String playerName, String playerType){
        players.put(playerName, playerPresets.get(playerType));
    }

    public String runCombat(String playerName, String targetName, int attackRoll, int damageRoll){
        currentPlayer = players.get(playerName);
        currentTarget = players.get(targetName);

        if(attackRoll >= currentTarget.getArmorClass()){
            hit = true;
            damage = damageRoll;
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

    public String[] getNames(){
        String[] playerNames = null;
        players.keySet().toArray(playerNames);
        return playerNames;
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

    public String[] getScoreboard(){
        String[] colorActorStats = null;
        ArrayList<String> eachActor = new ArrayList<>();
        for (Iterator<Actor> it = getCurrentPlayers().values().iterator(); it.hasNext();) {
            Actor actor = it.next();
            String color = "#D49B90";
            if (actor.isPlayer()){
                color = "#A7D490";
            }
            eachActor.add(color);
            eachActor.add(actor.toString());
        }

        eachActor.toArray(colorActorStats);
        return colorActorStats;
    }
}
