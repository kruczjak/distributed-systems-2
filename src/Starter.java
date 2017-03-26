import org.jgroups.Message;
import pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kruczjak on 3/25/17.
 */

public class Starter {
    private final Map<String, SingleChannelChat> singleChannelChatList = new HashMap<>();
    private String nickname;
    private ChatManager chatManager;
    private BufferedReader inputReader;

    private Starter(String nickname) {
        this.nickname = nickname;
    }

    private void run() throws Exception {
        this.chatManager = new ChatManager(this.nickname);
        this.inputReader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            if (!this.inputLoop(inputReader)) break;
        }
        this.chatManager.close();
    }

    private boolean inputLoop(BufferedReader inputReader) throws Exception {
        System.out.println("------------------------------------");
        System.out.println("Commands:");
        System.out.println("q - quit");
        System.out.println("join <channelName> - joins channel with desired name");
        System.out.println("leave <channelName> - leave from channel");
        System.out.println("send <channelName> - send a message to channel");
        System.out.println("list channels");

        String input = inputReader.readLine();

        if (input.startsWith("q")) {
            return false;
        } else if (input.startsWith("join")) {
            String channelName = getChannelName("join ", input);
            SingleChannelChat singleChannelChat = new SingleChannelChat(this.nickname, channelName);
            this.singleChannelChatList.put(channelName, singleChannelChat);
            this.chatManager.joinToChannel(channelName);
        } else if (input.startsWith("leave")) {
            String channelName = getChannelName("leave ", input);
            SingleChannelChat singleChannelChat = this.singleChannelChatList.get(channelName);

            if (singleChannelChat == null) {
                System.out.println("No such channel...");
                return true;
            }

            this.singleChannelChatList.remove(channelName);
            singleChannelChat.leave();
            this.chatManager.leaveChannel(channelName);
        } else if (input.startsWith("send")) {
            String channelName = getChannelName("send ", input);
            SingleChannelChat singleChannelChat = this.singleChannelChatList.get(channelName);

            if (singleChannelChat == null) {
                System.out.println("No such channel: " + channelName);
                return true;
            }

            singleChannelChat.sendMessage(this.preparedMessage());
        } else if (input.startsWith("list channels")) {
            Map<String, ChatChannel> chatChannelMap = this.chatManager.listChannels();
            chatChannelMap.values().forEach(System.out::println);
        }

        return true;
    }

    private Message preparedMessage() throws IOException {
        String line = inputReader.readLine();

        ChatOperationProtos.ChatMessage chatMessage = ChatOperationProtos.ChatMessage.newBuilder()
            .setMessage(line)
            .build();

        return new Message(null, null, chatMessage.toByteArray());
    }

    public static void main(String [] args) throws Exception {
        System.setProperty("java.net.preferIPv4Stack", "true");
        String nickname = args[0];

        new Starter(nickname).run();
    }

    private String getChannelName(String prefixString, String word) {
        return word.substring(prefixString.length());
    }
}
