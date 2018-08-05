package modules;

import org.json.JSONObject;

public class MyHandler extends Handler {

    public MyHandler(String portString) {
        super(portString);
    }

    @Override
    protected void handle(JSONObject message) {
            System.out.println(message);
    }


    public static void main(String[] args) {
        String portString = "8990";
        new Thread(new MyHandler(portString)).start();
    }
}
