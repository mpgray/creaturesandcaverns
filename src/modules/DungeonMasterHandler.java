package modules;

import org.json.JSONObject;

public class DungeonMasterHandler extends Handler {


    static final String MODULE = "CREATURESANDCAVERNS";
    private Game game = new Game();
    private String currentPlayer;
    private int currentPlayerIndex = 0;
    boolean gameOver = false;

    public DungeonMasterHandler(String portString) {
        super(portString);
    }

    @Override
    protected void handle(JSONObject message) {
        if(message.opt("module") != null || MODULE.equals(message.getString("module"))){
            String action = message.getString("action");
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
                case "passTurn"                  :   passPlayerTurn(message);
                    break;
            }
        }
    }

    private void passPlayerTurn(JSONObject message) {
        incrementPlayerTurn();
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
        broadcast(JSONLibrary.serverScoreboard(game.getNames(), game.getScoreboard()), MODULE);

        incrementPlayerTurn();
    }

    private void incrementPlayerTurn() {
        if(game.getGameOver()){
            //TODO
            //broadcast(JSONLibrary.serverGameOver(), MODULE);
            return;
        }

        do{
            if(currentPlayerIndex < game.getNames().length - 1){
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
        currentPlayer = game.getNames()[currentPlayerIndex];
        broadcast(JSONLibrary.serverGameStarted(), MODULE);
        broadcast(JSONLibrary.serverScoreboard(game.getNames(), game.getScoreboard()), MODULE);
        netSend(JSONLibrary.serverYourTurn(), currentPlayer, MODULE);
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