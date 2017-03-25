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
    public boolean equals(Object obj) {
        if (obj instanceof String) {
            String comparedObj = (String) obj;
            return this.channelId.equals(comparedObj);
        }
        return super.equals(obj);
    }
}
