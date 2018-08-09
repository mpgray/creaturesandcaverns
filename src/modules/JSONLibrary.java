package modules;

import org.json.JSONObject;

public class JSONLibrary {
    public static final String MODULE = "CREATURESANDCAVERNS";

    public JSONLibrary(){

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
        startCommand.put("action", "startNewGame");
        startGame.put("message", startCommand);

        return startGame.toString();
    }

    public static String sendPlayerCharacter(String pc, String username){
        JSONObject sendPC = new JSONObject();
        JSONObject playerCharacter = new JSONObject();

        sendPC.put("type", "application");
        playerCharacter.put("module", MODULE);
        playerCharacter.put("action", "addPlayerCharacter");
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
        initiateTurn.put("action", "runCombat");
        initiateTurn.put("attacker", attackerUsername);
        initiateTurn.put("target", targetUsername);
        initiateTurn.put("attachRoll", attackRoll);
        initiateTurn.put("damageRoll", damageRoll);

        sendInitiateTurn.put("message", initiateTurn);

        return sendInitiateTurn.toString();
    }

    public static String sendAddCreature( ){

        JSONObject addCreature = new JSONObject();
        JSONObject addCreatureMsg = new JSONObject();

        addCreature.put("type", "application");

        addCreatureMsg.put("module", MODULE);
        addCreatureMsg.put("action", "addCreature");

        addCreature.put("message", addCreatureMsg);

        return addCreature.toString();
    }

    public static String sendPassTurn(){

        JSONObject sendPassTurn = new JSONObject();
        JSONObject passTurnMsg = new JSONObject();

        sendPassTurn.put("type", "application");

        passTurnMsg.put("module", MODULE);
        passTurnMsg.put("action", "true");

        sendPassTurn.put("message", passTurnMsg);

        return sendPassTurn.toString();
    }

    //SERVER JSON

    public static JSONObject serverGameStarted(){

        JSONObject gameStarted = new JSONObject();
        JSONObject gameStartedMsg = new JSONObject();

        gameStarted.put("type", "application");

        gameStartedMsg.put("module", MODULE);
        gameStartedMsg.put("isStarted","true");

        gameStarted.put("message", gameStarted);

        return gameStarted;
    }

    public static JSONObject serverBattleReport(String battleReport ){

        JSONObject sendBattleReport = new JSONObject();
        JSONObject battleReportMsg = new JSONObject();

        sendBattleReport.put("type", "application");
        battleReportMsg.put("module", MODULE);
        battleReportMsg.put("battleReport", battleReport);

        sendBattleReport.put("message", battleReportMsg);

        return sendBattleReport;
    }

    public static JSONObject serverScoreboard(String[] playerNames, String[] colorActorStats){

        JSONObject sendScoreboard = new JSONObject();
        JSONObject scoreboardMsg = new JSONObject();

        sendScoreboard.put("type", "application");

        scoreboardMsg.put("module", MODULE);
        scoreboardMsg.put("playerNames", playerNames);
        scoreboardMsg.put("colorActorStats", colorActorStats);

        sendScoreboard.put("message", scoreboardMsg);

        return sendScoreboard;
    }

    public static JSONObject serverPlayerDeath(){

        JSONObject sendPlayerDeath = new JSONObject();
        JSONObject playerDeathMsg = new JSONObject();

        sendPlayerDeath.put("type", "application");

        playerDeathMsg.put("module", MODULE);
        playerDeathMsg.put("isDead", true);

        sendPlayerDeath.put("message", playerDeathMsg);

        return sendPlayerDeath;
    }

    public static JSONObject serverYourTurn(){

        JSONObject sendYourTurn = new JSONObject();
        JSONObject yourTurnMsg = new JSONObject();

        sendYourTurn.put("type", "application");

        yourTurnMsg.put("module", MODULE);
        yourTurnMsg.put("yourTurn", true);

        sendYourTurn.put("message", yourTurnMsg);

        return sendYourTurn;
    }



}