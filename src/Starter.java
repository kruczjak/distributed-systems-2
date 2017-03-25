import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by kruczjak on 3/25/17.
 */

public class Starter {
    private String nickname;
    private ChatManager chatManager;

    private Starter(String nickname) {
        this.nickname = nickname;
    }

    private void run() throws Exception {
        this.chatManager = new ChatManager(this.nickname);
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            if (!this.inputLoop(inputReader)) break;
        }
        this.chatManager.close();
    }

    private boolean inputLoop(BufferedReader inputReader) throws Exception {
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
            this.chatManager.joinToChannel(channelName);
        } else if (input.startsWith("leave")) {
            String channelName = getChannelName("leave ", input);
            this.chatManager.leaveChannel(channelName);
        } else if (input.startsWith("send")) {
            String channelName = getChannelName("send", input);

        } else if (input.startsWith("list channels")) {
            List<ChatChannel> chatChannelList = this.chatManager.listChannels();
            chatChannelList.forEach(System.out::println);
        }

        return true;
    }

    public static void main(String [] args) throws Exception {
        String nickname = args[0];

        new Starter(nickname).run();
    }

    private String getChannelName(String prefixString, String word) {
        return word.substring(prefixString.length());
    }
}
