package demo.domain.entities;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by anthonyhayes on 8/7/14.
 */
@EqualsAndHashCode
@ToString
public class DashboardMessage {

    private String channel;

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    private String token;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    private String action;

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    private String type;

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    private Object message;

    public Object getMessage() { return message; }
    public void setMessage(Object message) { this.message = message; }

}
