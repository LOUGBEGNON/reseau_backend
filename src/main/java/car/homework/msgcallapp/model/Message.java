package car.homework.msgcallapp.model;

public class Message {
    private String sender;
    private String recipient;
    private String content;
    private String groupId;  // Nouveau champ pour le groupe

    public Message(String sender, String recipient, String content, String groupId) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.groupId = groupId;
    }

    // Getters et Setters
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Getters et Setters
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}