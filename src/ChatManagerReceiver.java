import com.google.protobuf.InvalidProtocolBufferException;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kruczjak on 3/25/17.
 */
public class ChatManagerReceiver extends ReceiverAdapter {
    private ChatOperationProtos.ChatState chatState;

    ChatManagerReceiver() {
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

        if (chatAction.getAction() == ChatOperationProtos.ChatAction.ActionType.JOIN) {
            builder.addState(chatAction);
            System.out.println("*** " + chatAction.getNickname() + " is joining " + chatAction.getChannel());
        } else {
            // handle LEAVE
            System.out.println("*** " + chatAction.getNickname() + " is leaving " + chatAction.getChannel());
            List<ChatOperationProtos.ChatAction> newChatActionList = builder
                .getStateList()
                .stream()
                .filter(action -> this.isNotJoinActionToDelete(chatAction, action))
                .collect(Collectors.toList());
            builder.clear().addAllState(newChatActionList);
        }

        this.buildStateSynchronized(builder);
    }

    private boolean isNotJoinActionToDelete(ChatOperationProtos.ChatAction action, ChatOperationProtos.ChatAction actionFromList) {
        return actionFromList.getAction() != ChatOperationProtos.ChatAction.ActionType.JOIN
                || !action.getNickname().equals(actionFromList.getNickname())
                || !action.getChannel().equals(actionFromList.getChannel());
    }

    public ChatOperationProtos.ChatState getState() {
        return this.chatState;
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
