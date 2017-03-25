import com.google.protobuf.InvalidProtocolBufferException;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by kruczjak on 3/25/17.
 */
public class ChatManagerReceiver extends ReceiverAdapter {
    private final ChatManager chatManager;
    private ChatOperationProtos.ChatState chatState;

    ChatManagerReceiver(ChatManager chatManager) {
        this.chatManager = chatManager;
        this.chatState = ChatOperationProtos.ChatState.newBuilder().build();
    }

    @Override
    public void receive(Message msg) {
        try {
            this.handleReceive(msg);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    private void handleReceive(Message msg) throws InvalidProtocolBufferException {
        ChatOperationProtos.ChatAction chatAction = ChatOperationProtos.ChatAction.parseFrom(msg.getRawBuffer());
        ChatOperationProtos.ChatState.Builder builder = this.getChatStateBuilder();

        this.buildStateSynchronized(builder);
    }

    @Override
    public void getState(OutputStream output) throws Exception {
        this.getStateSynchronized(output);
    }

    @Override
    public void setState(InputStream input) throws Exception {
        this.setStateSynchronized(input);
    }

    private synchronized void setStateSynchronized(InputStream input) throws IOException {
        this.chatState = ChatOperationProtos.ChatState.parseFrom(input);
    }

    private synchronized void getStateSynchronized(OutputStream output) throws IOException {
        output.write(this.chatState.toByteArray());
    }

    private synchronized void buildStateSynchronized(ChatOperationProtos.ChatState.Builder builder) {
        this.chatState = builder.build();
    }

    private ChatOperationProtos.ChatState.Builder getChatStateBuilder() {
        return this.chatState.toBuilder();
    }
}
