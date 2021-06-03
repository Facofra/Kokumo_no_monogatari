package connection;

import com.google.gson.Gson;

public class MessageManager {
    Gson gson;

    public MessageManager(Gson gson) {
        this.gson = gson;
    }

    public String toJson(Message message){
        return gson.toJson(message);
    }

    public Message jsonToMessage(String json){
        return gson.fromJson(json,Message.class);
    }
}
