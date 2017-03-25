import org.jgroups.protocols.*;
import org.jgroups.protocols.pbcast.*;

/**
 * Created by kruczjak on 3/25/17.
 */

public class ProtocolStack {
    public static org.jgroups.stack.ProtocolStack getProtocolStack() {
        return new org.jgroups.stack.ProtocolStack().addProtocol(new UDP())
            .addProtocol(new PING())
            .addProtocol(new MERGE3())
            .addProtocol(new FD_SOCK())
            .addProtocol(new FD_ALL().setValue("timeout", 12000).setValue("interval", 3000))
            .addProtocol(new VERIFY_SUSPECT())
            .addProtocol(new BARRIER())
            .addProtocol(new NAKACK2())
            .addProtocol(new UNICAST3())
            .addProtocol(new STABLE())
            .addProtocol(new GMS())
            .addProtocol(new UFC())
            .addProtocol(new MFC())
            .addProtocol(new FRAG2())
            .addProtocol(new STATE_TRANSFER())
            .addProtocol(new FLUSH());
    }
}
