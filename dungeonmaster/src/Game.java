import java.util.*;

public class Game {
    private LinkedHashMap<String, Actor> creaturePresets;
    private HashMap<String, Actor> playerPresets;
    private TreeMap<String, Actor> actors;
    private ArrayList<String> playerTurnList, monsterOrderList;
    private Actor currentActor;
    private Actor currentTarget;
    private boolean hit, gameStarted, gameOver;
    private int damage, numDead, turnIndex, currentMonsterIndex;
    private String currentTurnName, currentCreatureName, battleReport, winner;

    public Game(){
        creaturePresets = new ActorPresets().creatures;
        playerPresets = new ActorPresets().playerPresets;
        actors = new TreeMap<>();

        gameStarted = false;
        winner = null;
    }

    public void startGame(){
        gameStarted = true;
        gameOver = false;
        numDead = 0;
        turnIndex = 0;
        currentMonsterIndex = 0;
        this.addNextMonster();
        playerTurnList = new ArrayList<>(Arrays.asList(this.getNames()));
        currentTurnName = playerTurnList.get(turnIndex);
        while(!actors.get(currentTurnName).isPlayer()){
            this.incrementTurn();
        }
        for(Actor a : actors.values()){
            System.out.println("Initiative: " + a.getInitiative());
        }
    }

    public String getWhosTurn(){
        return currentTurnName;
    }

    public void incrementTurn(){
        turnIndex++;
        playerTurnList = new ArrayList<>(Arrays.asList(this.getNames()));
        if(playerTurnList.size() == 1){
            return;
        }
        if(turnIndex >= playerTurnList.size()){
            turnIndex = 0;
        }
        currentTurnName = playerTurnList.get(turnIndex);
    }

    public void endGame(){
        gameStarted = false;
        for(String a : actors.keySet()){
            actors.remove(a);
        }
    }

    public void addPlayer(String playerName, String playerType){
        actors.put(playerName, playerPresets.get(playerType));
    }

    public void removePlayer(String playerName){
        actors.remove(playerName);
    }

    public String runCombat(String playerName, String targetName, int attackRoll, int damageRoll){
        currentActor = actors.get(playerName);
        currentTarget = actors.get(targetName);

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

        String attackName = currentActor.getAttackName();
        String defenseName = currentTarget.getDefenseName();

        if(currentTarget.getIsDead()){
            battleReport = (playerName + "'s attack roll of " + attackRoll + " hit and killed " + targetName + " with " + attackName + " for " + damage + "!");
            if(!currentTarget.isPlayer()){
                addNextMonster();
                if(currentCreatureName.equalsIgnoreCase("Win")){
                    battleReport += "\nYou win!";
                    return battleReport;
                }
                battleReport += "\n" + currentCreatureName + " joined the game!";
                turnIndex = 0;
            }
            this.removePlayer(targetName);
        } else {
            battleReport = hit ? (playerName + "'s attack roll of " + attackRoll + " hit " + targetName + " with " + attackName + " for " + damage + "!") :
                    (targetName + " avoided damage from " + playerName + "'s attack roll of " + attackRoll + " with " + defenseName + "!");
        }

        return battleReport;
    }

    public String getCurrentCreatureName(){
        return currentCreatureName;
    }

    public void addNextMonster(){
        if(currentMonsterIndex >= creaturePresets.size()){
            gameOver = true;
            currentCreatureName = "Win";
            return;
        }
        Object[] creatureKeys = creaturePresets.keySet().toArray();
        Object key = creatureKeys[currentMonsterIndex];
        currentCreatureName = creaturePresets.get(key).getType();
        System.out.println("Added Creature: " + currentCreatureName);
        actors.put(creaturePresets.get(key).getType(), creaturePresets.get(key));
        currentMonsterIndex++;
    }

    public String addRandomMonster(){
        Object[] creatureKeys = creaturePresets.keySet().toArray();
        Object key = creatureKeys[new Random().nextInt(creatureKeys.length)];
        String creatureName = creaturePresets.get(key).getType();
        actors.put(creaturePresets.get(key).getType(), creaturePresets.get(key));
        return creatureName;
    }

    public TreeMap<String, Actor> getCurrentActors(){
        return actors;
    }

    public String[] getNames(){
        String[] playerNames = new String [actors.keySet().size()];
        playerNames = actors.keySet().toArray(playerNames);
        return playerNames;
    }

    public String getPlayerStats(String playerName){
        return actors.get(playerName).toString();
    }

    public Actor getCurrentTarget(){
        return currentTarget;
    }

    public String aiTargetSelect(String creatureName){
        Die D100 = new Die(100);
        int greatestChance = 0;
        int chanceAttacks = 0;
        String willAttack = actors.firstKey();
        for (String actorName : actors.keySet()) {
            chanceAttacks = D100.rollDie();
            if(actors.get(actorName).getType().equals("Fighter")){
                chanceAttacks += 50;
            }
            else if(actors.get(actorName).getType().equals("Mage")){
                chanceAttacks += 20;
            }
            else if(actors.get(actorName).getType().equals("Rogue")){
                chanceAttacks += 15;
            }
            else{
                chanceAttacks = -1000; //so it does not attack itself
            }
            if(actors.get(actorName).getCurrentHitPoints() < 10){
                chanceAttacks += 25;
            }
            if (chanceAttacks > greatestChance){
               willAttack = actorName;
               greatestChance = chanceAttacks;
            }
        }

        return willAttack;
    }

    public String[] getScoreboard(){
        String[] colorActorStats = null;
        ArrayList<String> eachActor = new ArrayList<>();
        for (Iterator<Actor> it = getCurrentActors().values().iterator(); it.hasNext();) {
            Actor actor = it.next();
            String color = "#D49B90";
            if (actor.isPlayer()){
                color = "#A7D490";
            }
            eachActor.add(color);
            eachActor.add(actor.toString());
        }

        colorActorStats = eachActor.toArray(new String[eachActor.size()]);
        return colorActorStats;
    }

    public boolean getGameOver(){
        numDead = 0;

        for(String actor : actors.keySet()){
            if(actors.get(actor).getIsDead()){
                numDead++;
            } else {
                winner = actor;
            }
        }
        if(numDead == this.getNames().length - 1){
            gameOver = true;
        }

        return gameOver;
    }

    public String getWinner(){
        return winner;
    }
}
