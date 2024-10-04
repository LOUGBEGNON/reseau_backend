package car.homework.msgcallapp.model;

public class INMSGPacket {
    private String version;
    private int packetLength;
    private String sessionId;
    private String serviceCode;
    private String status;
    private String data;

    // Constructeur
    public INMSGPacket(String version, int packetLength, String sessionId, String serviceCode, String status, String data) {
        this.version = version;
        this.packetLength = packetLength;
        this.sessionId = sessionId;
        this.serviceCode = serviceCode;
        this.status = status;
        this.data = data;
    }

    // Getters et Setters
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getPacketLength() {
        return packetLength;
    }

    public void setPacketLength(int packetLength) {
        this.packetLength = packetLength;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}