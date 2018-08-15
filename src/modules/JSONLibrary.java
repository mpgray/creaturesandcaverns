package modules;
import org.json.JSONObject;

public class JSONLibrary {
    public static final String MODULE = "CREATURESANDCAVERNS";

    public JSONLibrary(){}

    public static String sendChatMessage(String message){

        JSONObject sendChatMessage = new JSONObject();

        sendChatMessage.put("type", "chat");
        sendChatMessage.put("message", message);

        return sendChatMessage.toString();
    }

    public static String sendUser(String user){
        JSONObject loginMessage = new JSONObject();
        JSONObject username = new JSONObject();

        loginMessage.put("type", "login");
        username.put("username", user);
        loginMessage.put("message", username);

        return loginMessage.toString();
    }

    public static String sendStartGame(){
        JSONObject startGame = new JSONObject();
        JSONObject startCommand = new JSONObject();

        startGame.put("type", "application");

        startCommand.put("module", MODULE);
        startCommand.put("gameAction", "startNewGame");
        startGame.put("message", startCommand);

        return startGame.toString();
    }

    public static String sendPlayerCharacter(String pc, String username){
        JSONObject sendPC = new JSONObject();
        JSONObject playerCharacter = new JSONObject();

        sendPC.put("type", "application");

        playerCharacter.put("module", MODULE);
        playerCharacter.put("gameAction", "addPlayerCharacter");
        playerCharacter.put("username", username);
        playerCharacter.put("playerType", pc);
        sendPC.put("message", playerCharacter);

        return sendPC.toString();
    }

    public static String sendInitiateTurn(String attackerUsername, String targetUsername, int attackRoll, int damageRoll ){

        JSONObject sendInitiateTurn = new JSONObject();
        JSONObject initiateTurn = new JSONObject();

        sendInitiateTurn.put("type", "application");

        initiateTurn.put("module", MODULE);
        initiateTurn.put("gameAction", "runCombat");
        initiateTurn.put("attacker", attackerUsername);
        initiateTurn.put("target", targetUsername);
        initiateTurn.put("attackRoll", attackRoll);
        initiateTurn.put("damageRoll", damageRoll);

        sendInitiateTurn.put("message", initiateTurn);

        return sendInitiateTurn.toString();
    }

    public static String sendAddCreature( ){

        JSONObject addCreature = new JSONObject();
        JSONObject addCreatureMsg = new JSONObject();

        addCreature.put("type", "application");

        addCreatureMsg.put("module", MODULE);
        addCreatureMsg.put("gameAction", "addCreature");

        addCreature.put("message", addCreatureMsg);

        return addCreature.toString();
    }

    //SERVER JSON

    public static JSONObject serverGameStarted(){

        JSONObject gameStarted = new JSONObject();
        JSONObject gameStartedMsg = new JSONObject();

        gameStartedMsg.put("module", MODULE);
        gameStartedMsg.put("gameAction", "startGame");
        gameStartedMsg.put("isStarted","true");

        gameStarted.put("message", gameStartedMsg);

        return gameStartedMsg;
    }

    public static JSONObject serverBattleReport(String battleReport ){

        JSONObject sendBattleReport = new JSONObject();
        JSONObject battleReportMsg = new JSONObject();

        battleReportMsg.put("module", MODULE);
        battleReportMsg.put("gameAction", "battleReport");
        battleReportMsg.put("battleReport", battleReport);

        sendBattleReport.put("message", battleReportMsg);

        return sendBattleReport;
    }

    public static JSONObject serverScoreboard(String[] playerNames, String[] colorActorStats){

        JSONObject sendScoreboard = new JSONObject();
        JSONObject scoreboardMsg = new JSONObject();

        scoreboardMsg.put("module", MODULE);
        scoreboardMsg.put("gameAction", "scoreBoard");
        scoreboardMsg.put("playerNames", playerNames);
        scoreboardMsg.put("colorActorStats", colorActorStats);

        sendScoreboard.put("message", scoreboardMsg);

        return sendScoreboard;
    }

    public static JSONObject serverPlayerDeath(){

        JSONObject sendPlayerDeath = new JSONObject();
        JSONObject playerDeathMsg = new JSONObject();

        playerDeathMsg.put("module", MODULE);
        playerDeathMsg.put("gameAction", "playerDeath");
        playerDeathMsg.put("isDead", true);

        sendPlayerDeath.put("message", playerDeathMsg);

        return sendPlayerDeath;
    }

    public static JSONObject serverYourTurn(){

        JSONObject sendYourTurn = new JSONObject();
        JSONObject yourTurnMsg = new JSONObject();

        yourTurnMsg.put("module", MODULE);
        yourTurnMsg.put("gameAction", "yourTurn");
        yourTurnMsg.put("yourTurn", true);

        sendYourTurn.put("message", yourTurnMsg);

        return sendYourTurn;
    }

    public static JSONObject serverNotYourTurn(){

        JSONObject sendNotYourTurn = new JSONObject();
        JSONObject notYourTurnMsg = new JSONObject();

        notYourTurnMsg.put("module", MODULE);
        notYourTurnMsg.put("gameAction", "notYourTurn");
        notYourTurnMsg.put("yourTurn", false);

        sendNotYourTurn.put("message", notYourTurnMsg);

        return sendNotYourTurn;
    }


    public static JSONObject serverGameOver(String winner){
        JSONObject sendGameOver = new JSONObject();
        JSONObject gameOverMsg = new JSONObject();

        gameOverMsg.put("module", MODULE);
        gameOverMsg.put("gameAction", "gameOver");
        gameOverMsg.put("gameOver", true);
        gameOverMsg.put("winner", winner);

        sendGameOver.put("message", gameOverMsg);

        return sendGameOver;
    }

    public static JSONObject serverTargetNames(String[] targetNames){
        JSONObject sendTargetNames = new JSONObject();
        JSONObject targetNamesMsg = new JSONObject();

        targetNamesMsg.put("module", MODULE);
        targetNamesMsg.put("gameAction", "targetNames");
        targetNamesMsg.put("targetNames", targetNames);

        sendTargetNames.put("message", targetNamesMsg);

        return sendTargetNames;
    }

    public static JSONObject serverChatMessage(String message){

        JSONObject sendChatMessage = new JSONObject();

        sendChatMessage.put("type", "chat");
        sendChatMessage.put("message", message);

        return sendChatMessage;
    }

}
