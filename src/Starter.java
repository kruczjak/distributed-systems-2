/**
 * Created by kruczjak on 3/25/17.
 */

public class Starter {
    private String nickname;

    private Starter(String nickname) {
        this.nickname = nickname;
    }

    private void run() {

    }

    public static void main(String [] args) {
        String nickname = args[0];

        new Starter(nickname).run();
    }
}
