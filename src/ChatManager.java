import org.jgroups.JChannel;
import org.jgroups.Message;
import pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos;

/**
 * Created by kruczjak on 3/25/17.
 */
public class ChatManager {
    private static final String CHANNEL_NAME = "ChatManagement321321";
    private static final Integer TIMEOUT = 100000;
    private final JChannel channel;
    private final String nickname;

    ChatManager(String nickname) throws Exception {
        this.nickname = nickname;
        this.channel = this.initializedChannel();
        this.connectToChannel();
    }

    public void joinToChannel(String channel) throws Exception {
        this.channel.send(new Message(
                null,
                null,
                buildChatAction(channel, ChatOperationProtos.ChatAction.ActionType.JOIN).toByteArray()
        ));
    }

    public void leaveChannel(String channel) throws Exception {
        this.channel.send(new Message(
                null,
                null,
                buildChatAction(channel, ChatOperationProtos.ChatAction.ActionType.LEAVE).toByteArray()
        ));
    }

    public void close() {
        this.channel.close();
    }

    private JChannel initializedChannel() throws Exception {
        JChannel channel = new JChannel(false);
        channel.setReceiver(new ChatManagerReceiver(this));
        org.jgroups.stack.ProtocolStack protocolStack = ProtocolStack.getProtocolStack();
        protocolStack.init();
        channel.setProtocolStack(protocolStack);

        return channel;
    }

    private void connectToChannel() throws Exception {
        this.channel.connect(CHANNEL_NAME);
        this.channel.getState(null, TIMEOUT);
    }

    private ChatOperationProtos.ChatAction buildChatAction(String channel, ChatOperationProtos.ChatAction.ActionType action) {
        return ChatOperationProtos.ChatAction.newBuilder()
                .setNickname(this.nickname)
                .setAction(action)
                .setChannel(channel)
                .build();
    }
}
