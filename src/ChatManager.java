import org.jgroups.JChannel;
import org.jgroups.Message;
import pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kruczjak on 3/25/17.
 */
public class ChatManager {
    private static final String CHANNEL_NAME = "ChatManagement321321";
    private static final Integer TIMEOUT = 100000;
    private final JChannel channel;
    private final String nickname;
    private final ChatManagerReceiver chatManagerReceiver = new ChatManagerReceiver();

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

    public Map<String, ChatChannel> listChannels() {
        Map<String, ChatChannel> chatChannelMap = new HashMap<>();
        ChatOperationProtos.ChatState state = this.chatManagerReceiver.getState();

        if (state.getStateCount() <= 0) return chatChannelMap;

        for (ChatOperationProtos.ChatAction chatAction : state.getStateList()) {
            ChatOperationProtos.ChatAction.ActionType actionType = chatAction.getAction();
            String channelName = chatAction.getChannel();
            String channelMemberNickname = chatAction.getNickname();

            ChatChannel chatChannel = chatChannelMap.computeIfAbsent(channelName, ChatChannel::new);

            if (actionType == ChatOperationProtos.ChatAction.ActionType.JOIN) {
                chatChannel.addChannelMember(channelMemberNickname);
            } else {
                chatChannel.removeChannelMember(channelMemberNickname);
            }

            // so, remove empty???
        }

        return chatChannelMap;
    }

    private JChannel initializedChannel() throws Exception {
        JChannel channel = new JChannel(false);
        channel.setReceiver(this.chatManagerReceiver);
        org.jgroups.stack.ProtocolStack protocolStack = ProtocolStack.getProtocolStack();
        channel.setProtocolStack(protocolStack);
        protocolStack.init();

        return channel;
    }

    private void connectToChannel() throws Exception {
        this.channel.connect(CHANNEL_NAME);
        System.out.println("*** ChatManager connected. Fetching state ***");
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
