import org.jgroups.JChannel;
import org.jgroups.Message;

/**
 * Created by kruczjak on 3/25/17.
 */
public class SingleChannelChat {
    private final String nickname;
    private final String channelName;

    private final JChannel jChannel;

    SingleChannelChat(String nickname, String channelName) throws Exception {
        this.nickname = nickname;
        this.channelName = channelName;

        this.jChannel = this.createJChannel();
        this.connectToChannel();
    }

    public void leave() {
        this.jChannel.close();
        System.out.println("*** Closing channel: " + this.channelName);
    }

    public void sendMessage(Message message) throws Exception {
        this.jChannel.send(message);
    }

    private void connectToChannel() throws Exception {
        this.jChannel.connect(this.channelName);
        System.out.println("*** Connected to channel: " + this.channelName);
    }

    private JChannel createJChannel() throws Exception {
        org.jgroups.stack.ProtocolStack protocolStack = ProtocolStack.getProtocolStack(channelName);
        JChannel jChannel = new JChannel(false);
        jChannel.setReceiver(new SingleChannelChatReceiver(this.channelName));
        jChannel.setName(this.nickname);
        jChannel.setProtocolStack(protocolStack);
        protocolStack.init();

        return jChannel;
    }
}
