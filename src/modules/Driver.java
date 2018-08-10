package modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Driver {

//    private BufferedReader in;
//    private PrintWriter out;
//
//    private boolean connectToServer(){
//        String serverAddress = "localhost";
//        Socket socket = null;
//
//        try {
//            socket = new Socket(serverAddress, 8989);
//            in = new BufferedReader(new InputStreamReader(
//                    socket.getInputStream()));
//            out = new PrintWriter(socket.getOutputStream(), true);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("Could not connect to " + serverAddress + ". Continuing anyway for testing. \n");
//            return false;
//        }
//        System.out.println("Connected to " + serverAddress + " successfully.\n");
//        return true;
//    }
//
//
//
//    public void run() {

//        boolean isConnected = connectToServer();
//        username = getUser();
//        sendJson(JSONLibrary.sendUser(username));
//
//
//        playerCharacter = getPlayerCharacter();
//        sendJson(JSONLibrary.sendPlayerCharacter(playerCharacter));
//
//        // Process all messages from server, according to the protocol.
//        while (true) {
//            //THIS IS WHERE THE SERVER COMMUNICATES WITH THE UI!!!!!!!!!!!!!
//            //Put Handler here...
//
//            //  sendPlayerCharacter(playerCharacter);
//            //Just a test//
//            ActorPresets actorPresets = new ActorPresets();
//            Actor player1 = actorPresets.playerPresets.get(playerCharacter);
//
//        }


}

