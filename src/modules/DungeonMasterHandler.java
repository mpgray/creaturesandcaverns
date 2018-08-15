package modules;


import org.json.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class DungeonMasterHandler extends Handler {

    static final String MODULE = "CREATURESANDCAVERNS";
    private Game game = new Game();
    private String currentPlayer;
    private boolean gameOver;

    public DungeonMasterHandler(String portString) {
        super(portString);
    }

    @Override
    protected void handle(JSONObject message) {
        if(message.has("action") && message.opt("action").equals("broadcast")){
            return;
        }

        System.out.println("Client Says: " + message.toString());
        if(message.opt("module") != null || MODULE.equals(message.get("module"))){
            String action = message.has("gameAction") ? message.optString("gameAction") : message.optString("action");
            switch(action){
                case "addPlayerCharacter"        :   addPlayer(message);
                    break;
                case "startNewGame"              :   startGame();
                    break;
                case "runCombat"                 :   runCombat(message);
                    break;
                case "addCreature"               :   addCreature();
                    break;
                case "quit"                      :   removePlayer(message);
                    break;
                case "passTurn"                  :   incrementPlayerTurn();
                    break;

            }
        }
    }

    private void removePlayer(JSONObject message) {
        String username = message.getString("username");
        game.removePlayer(username);
        System.out.println(username + " left the game.");
        for(Actor a : game.getCurrentActors().values()){
            if(a.isPlayer()){
                System.out.println(a.getType() + " is still in the game.");
                return;
            }
        }
        game.endGame();
        game = new Game();
        System.out.println("All players quit. Game Ended.");
    }

    private void runCombat(JSONObject message) {
        String attackerUsername = message.getString("attacker");
        String targetUsername = message.getString("target");
        int attackRoll = message.getInt("attackRoll");
        int damageRoll = message.getInt("damageRoll");
        String battleReport = game.runCombat(attackerUsername, targetUsername, attackRoll, damageRoll);

        if(game.getCurrentTarget().getIsDead()){
            netSend(JSONLibrary.serverPlayerDeath(), targetUsername, MODULE);
        }

        broadcast(JSONLibrary.serverBattleReport(battleReport), MODULE);
        broadcast(JSONLibrary.serverTargetNames(game.getNames()), MODULE);
        broadcast(JSONLibrary.serverScoreboard(game.getNames(), game.getScoreboard()), MODULE);

        incrementPlayerTurn();
    }

    private void runAICombat(String creatureName){
        String attackerUsername = creatureName;
        String targetUsername = game.aiTargetSelect(creatureName);
        int attackRoll = game.getCurrentActors().get(creatureName).rollAttack();
        int damageRoll = game.getCurrentActors().get(creatureName).rollDamage();
        String battleReport = game.runCombat(attackerUsername, targetUsername, attackRoll, damageRoll);
        System.out.println(battleReport);
        if(game.getCurrentTarget().getIsDead()){
            netSend(JSONLibrary.serverPlayerDeath(), targetUsername, MODULE);
        }

        broadcast(JSONLibrary.serverBattleReport(battleReport), MODULE);
        broadcast(JSONLibrary.serverTargetNames(game.getNames()), MODULE);
        broadcast(JSONLibrary.serverScoreboard(game.getNames(), game.getScoreboard()), MODULE);
    }

    private void addPlayer(JSONObject message) {
        String username = message.getString("username");
        String playerType = message.getString("playerType");
        game.addPlayer(username, playerType);
    }

    private void startGame() {
        game.startGame();
        gameOver = false;
        currentPlayer = game.getWhosTurn();
        broadcast(JSONLibrary.serverGameStarted(), MODULE);
        broadcast(JSONLibrary.serverTargetNames(game.getNames()), MODULE);
        broadcast(JSONLibrary.serverScoreboard(game.getNames(), game.getScoreboard()), MODULE);
        netSend(JSONLibrary.serverYourTurn(), currentPlayer, MODULE);
    }

    private void incrementPlayerTurn(){
        game.incrementTurn();
        currentPlayer = game.getWhosTurn();
        while(!game.getCurrentActors().get(currentPlayer).isPlayer()){
            if(game.getCurrentActors().get(currentPlayer).getIsDead()){
                game.incrementTurn();
                currentPlayer = game.getWhosTurn();
            } else {
                runAICombat(currentPlayer);
                game.incrementTurn();
                currentPlayer = game.getWhosTurn();
            }
        }
        netSend(JSONLibrary.serverYourTurn(), currentPlayer, MODULE);
    }

    private void addCreature() {
        String addedCreature = game.addRandomMonster();
        broadcast(JSONLibrary.serverAddedCreature(addedCreature), MODULE);
        broadcast(JSONLibrary.serverScoreboard(game.getNames(), game.getScoreboard()), MODULE);
        broadcast(JSONLibrary.serverTargetNames(game.getNames()), MODULE);
    }

    public static void main(String[] args) {
        String portString = "8990";
        new Thread(new DungeonMasterHandler(portString)).start();
    }


}
