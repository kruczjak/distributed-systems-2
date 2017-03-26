import com.google.protobuf.InvalidProtocolBufferException;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos;

/**
 * Created by kruczjak on 3/25/17.
 */
public class SingleChannelChatReceiver extends ReceiverAdapter {
    private final String channelName;

    SingleChannelChatReceiver(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public void receive(Message msg) {
        try {
            ChatOperationProtos.ChatMessage chatMessage = ChatOperationProtos.ChatMessage.parseFrom(msg.getRawBuffer());
            String result = msg.getSrc() + "@#" + this.channelName + ": " + chatMessage.getMessage();

            System.out.println(result);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }
}
