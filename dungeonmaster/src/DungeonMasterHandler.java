import modules.Handler;
import org.json.*;

public class DungeonMasterHandler extends Handler {

    static final String MODULE = "CREATURESANDCAVERNS";
    private Game game = new Game();
    private String currentPlayer;
    private boolean gameStarted;

    public DungeonMasterHandler(String portString) {
        super(portString);
    }

    @Override
    protected void handle(JSONObject message) {
        if(message.has("action") && message.opt("action").equals("broadcast")){
            return;
        }

        if(message.opt("module") != null || MODULE.equals(message.get("module"))){
            String action = message.has("gameAction") ? message.optString("gameAction") : message.optString("action");
            switch(action){
                case "addPlayerCharacter"        :   addPlayer(message);
                    break;
                case "startNewGame"              :   startGame(message.opt("username").toString());
                    break;
                case "runCombat"                 :   runCombat(message);
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
                broadcast(JSONLibrary.serverPlayerRemoved(username), MODULE);
                return;
            }
        }
        game.endGame();
        game = new Game();
        System.out.println("All players quit. Game Ended.");
    }

    private void runCombat(JSONObject message) {
        System.out.println("Running player combat.");
        String attackerUsername = message.getString("attacker");
        String targetUsername = message.getString("target");
        int attackRoll = message.getInt("attackRoll");
        int damageRoll = message.getInt("damageRoll");
        String battleReport = game.runCombat(attackerUsername, targetUsername, attackRoll, damageRoll);

        if(game.getCurrentCreatureName().equalsIgnoreCase("Win")){
            broadcast(JSONLibrary.serverBattleReport(battleReport), MODULE);
            broadcast(JSONLibrary.serverTargetNames(game.getNames()), MODULE);
            broadcast(JSONLibrary.serverScoreboard(game.getNames(), game.getScoreboard()), MODULE);
            endGame("You Win!");
            return;
        }

        if(game.getCurrentTarget().getIsDead()){
            broadcast(JSONLibrary.serverPlayerRemoved(targetUsername), MODULE);
            netSend(JSONLibrary.serverPlayerDeath(), targetUsername, MODULE);
        }

        broadcast(JSONLibrary.serverBattleReport(battleReport), MODULE);
        broadcast(JSONLibrary.serverTargetNames(game.getNames()), MODULE);
        broadcast(JSONLibrary.serverScoreboard(game.getNames(), game.getScoreboard()), MODULE);

        incrementPlayerTurn();
    }

    private void runAICombat(String creatureName){
        System.out.println("Running AI combat.");
        String attackerUsername = creatureName;
        String targetUsername = game.aiTargetSelect(creatureName);
        int attackRoll = game.getCurrentActors().get(creatureName).rollAttack();
        int damageRoll = game.getCurrentActors().get(creatureName).rollDamage();
        String battleReport = game.runCombat(attackerUsername, targetUsername, attackRoll, damageRoll);
        if(game.getCurrentTarget().equals(game.getCurrentActors().get(creatureName))){
            endGame("You Lose!");
        }
        if(game.getCurrentTarget().getIsDead()){
            broadcast(JSONLibrary.serverPlayerRemoved(targetUsername), MODULE);
            netSend(JSONLibrary.serverPlayerDeath(), targetUsername, MODULE);
        }

        broadcast(JSONLibrary.serverBattleReport(battleReport), MODULE);
        broadcast(JSONLibrary.serverTargetNames(game.getNames()), MODULE);
        broadcast(JSONLibrary.serverScoreboard(game.getNames(), game.getScoreboard()), MODULE);
    }

    private void addPlayer(JSONObject message) {
        String username = message.getString("username");
        if(!gameStarted){
            String playerType = message.getString("playerType");
            game.addPlayer(username, playerType);
            System.out.println(username + " joined the game.");
        }else{
            netSend(JSONLibrary.serverGameInProgress(), username, MODULE);
            System.out.println("Cannot join game. Game already in progress.");
        }

    }

    private void startGame(String username) {
        if(gameStarted){
            netSend(JSONLibrary.serverGameInProgress(), username, MODULE);
            System.out.println("Cannot start game. Game already in progress.");
            return;
        }
        game.startGame();
        gameStarted = true;
        currentPlayer = game.getWhosTurn();
        broadcast(JSONLibrary.serverGameStarted(), MODULE);
        broadcast(JSONLibrary.serverTargetNames(game.getNames()), MODULE);
        broadcast(JSONLibrary.serverScoreboard(game.getNames(), game.getScoreboard()), MODULE);
        netSend(JSONLibrary.serverYourTurn(), currentPlayer, MODULE);
        System.out.println("Game started.");
    }

    private void incrementPlayerTurn(){
        game.incrementTurn();
        currentPlayer = game.getWhosTurn();
        if(game.getCurrentActors().size() == 1 && game.getCurrentActors().get(currentPlayer).isPlayer()){
            endGame("You Win!");
            return;
        }
        while(!game.getCurrentActors().get(currentPlayer).isPlayer()){
            if(game.getCurrentActors().size() == 1){
                endGame("You Lose!");
                return;
            }
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

    private void endGame(String winMsg){
        gameStarted = false;
        broadcast(JSONLibrary.serverGameOver(winMsg), MODULE);
        game = new Game();
        System.out.println("Game Ended.");
    }

    public static void main(String[] args) {
        String portString = "8990";
        new Thread(new DungeonMasterHandler(portString)).start();
    }


}
