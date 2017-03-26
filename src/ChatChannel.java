import java.util.ArrayList;
import java.util.List;

/**
 * Created by kruczjak on 3/25/17.
 */
public class ChatChannel {
    private String channelId;
    private List<String> channelMembers;

    ChatChannel(String channelId) {
        this.channelId = channelId;
        this.channelMembers = new ArrayList<>();
    }

    public void addChannelMember(String channelMemberNickname) {
        this.channelMembers.add(channelMemberNickname);
    }


    public void removeChannelMember(String channelMemberNickname) {
        this.channelMembers.remove(channelMemberNickname);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("> Channel: " + this.channelId + "\n");
        for (String member : this.channelMembers) stringBuilder.append("!!> ").append(member).append("\n");
        return stringBuilder.toString();
    }
}
