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
            String action = message.optString("gameAction");
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

            }
        }
    }

    private void removePlayer(JSONObject message) {
        String username = message.getString("username");
        game.removePlayer(username);
    }

    private ArrayList<String> notCurrentPlayers(String currentPlayer){
        String[] n = game.getNames();
        final ArrayList<String> notCurrentPlayers =  new ArrayList<String>();
        Collections.addAll(notCurrentPlayers, n);
        notCurrentPlayers.remove(currentPlayer);

        return notCurrentPlayers;
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
            runAICombat(currentPlayer);
            game.incrementTurn();
            currentPlayer = game.getWhosTurn();
        }
        netSend(JSONLibrary.serverYourTurn(), currentPlayer, MODULE);
    }

    private void addCreature() {
        game.addRandomMonster();
        broadcast(JSONLibrary.serverScoreboard(game.getNames(), game.getScoreboard()), MODULE);
        broadcast(JSONLibrary.serverTargetNames(game.getNames()), MODULE);
    }

    public static void main(String[] args) {
        String portString = "8990";
        new Thread(new DungeonMasterHandler(portString)).start();
    }


}
