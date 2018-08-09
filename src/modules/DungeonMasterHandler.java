package modules;

import org.json.JSONObject;

public class DungeonMasterHandler extends Handler {


    static final String MODULE = "CREATURESANDCAVERNS";
    private Game game = new Game();

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

        broadcast(JSONLibrary.serverBattleReport(battleReport), MODULE);
        broadcast(JSONLibrary.serverScoreboard(game.getNames(), game.getScoreboard()), MODULE);
    }

    private void addPlayer(JSONObject message) {
        String username = message.getString("username");
        String playerType = message.getString("playerType");

        game.addPlayer(username, playerType);
    }

    private void startGame() {
        game.startGame();
        broadcast(JSONLibrary.serverGameStarted(), MODULE);
        broadcast(JSONLibrary.serverScoreboard(game.getNames(), game.getScoreboard()), MODULE);
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
