package fr.ensim.interop.introrest.data;

public class MessageApi {

    private Long chat_id;
    private String text;

    public MessageApi(Long _chat_id, String _text){
        chat_id = _chat_id;
        text = _text;
    }
    public Long getChat_id() {
        return chat_id;
    }
    public void setChat_id(Long chat_id) {
        this.chat_id = chat_id;
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
