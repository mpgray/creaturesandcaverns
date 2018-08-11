package modules;

import org.json.*;

public class DungeonMasterHandler extends Handler {

    static final String MODULE = "CREATURESANDCAVERNS";
    private Game game = new Game();
    private String currentPlayer;
    private int currentPlayerIndex;
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

    private void incrementPlayerTurn() {
        if(game.getGameOver()){
            broadcast(JSONLibrary.serverGameOver(game.getWinner()), MODULE);
            return;
        }

        do{
            if(currentPlayerIndex < game.getNames().length){
                currentPlayerIndex++;
            } else {
                currentPlayerIndex = 0;
            }
            currentPlayer = game.getNames()[currentPlayerIndex];
        } while(game.getCurrentActors().get(currentPlayer).getIsDead());

        if(game.getCurrentActors().get(currentPlayer).isPlayer()){
            netSend(JSONLibrary.serverYourTurn(), currentPlayer, MODULE);
        } else {
            runAICombat(currentPlayer);
        }

    }

    private void runAICombat(String creatureName){
        String attackerUsername = creatureName;
        String targetUsername = game.aiTargetSelect(creatureName);
        int attackRoll = game.getCurrentActors().get(creatureName).rollAttack();
        int damageRoll = game.getCurrentActors().get(creatureName).rollDamage();
        String battleReport = game.runCombat(attackerUsername, targetUsername, attackRoll, damageRoll);

        if(game.getCurrentTarget().getIsDead()){
            netSend(JSONLibrary.serverPlayerDeath(), targetUsername, MODULE);
        }

        broadcast(JSONLibrary.serverBattleReport(battleReport), MODULE);
        broadcast(JSONLibrary.serverTargetNames(game.getNames()), MODULE);
        broadcast(JSONLibrary.serverScoreboard(game.getNames(), game.getScoreboard()), MODULE);

        incrementPlayerTurn();
    }

    private void addPlayer(JSONObject message) {
        String username = message.getString("username");
        String playerType = message.getString("playerType");
        game.addPlayer(username, playerType);
    }

    private void startGame() {
        game.startGame();
        currentPlayerIndex = 0;
        currentPlayer = game.getNames()[currentPlayerIndex];
        gameOver = false;
        broadcast(JSONLibrary.serverGameStarted(), MODULE);
        broadcast(JSONLibrary.serverTargetNames(game.getNames()), MODULE);
        broadcast(JSONLibrary.serverScoreboard(game.getNames(), game.getScoreboard()), MODULE);
        if(game.getCurrentActors().get(currentPlayer).isPlayer()){
            netSend(JSONLibrary.serverYourTurn(), currentPlayer, MODULE);
        } else {
            runAICombat(currentPlayer);
        }

    }

    private void addCreature() {
        game.addRandomMonster();
        broadcast(JSONLibrary.serverScoreboard(game.getNames(), game.getScoreboard()), MODULE);
    }

    public static void main(String[] args) {
        String portString = "8990";
        new Thread(new DungeonMasterHandler(portString)).start();
    }


}
