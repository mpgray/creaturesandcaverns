package modules;

import org.json.JSONObject;

public class MyHandler extends Handler {

    public MyHandler(String portString) {
        super(portString);
    }

    @Override
    protected void handle(JSONObject message) {
        if(message.has("rollRequest")){
            //do something
        }
        
    }

    public static void main(String[] args) {

      //  new Thread(MyHandler.handle).start();
    }
}
