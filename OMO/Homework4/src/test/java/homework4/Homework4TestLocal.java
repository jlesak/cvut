package homework4;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.mockito.Mockito.*;

/**
 *
 * @author sebek
 */

interface PeerInterface {
    void have(PeerInterface sender, int blockIndex);
    void request(PeerInterface sender, int blockIndex);
    void piece(PeerInterface sender, int blockIndex, byte[] data);
}

class Peer implements PeerInterface {
    final Queue<Message> messageQueue = new LinkedBlockingQueue<>();
    final Map<PeerInterface, boolean[]> peers2BlocksMap;
    final int totalBlocksCount;
    final byte[][] data;
    int downloadedBlocksCount = 0;

    public Peer(Map<PeerInterface, boolean[]> peers2BlocksMap, int totalBlocksCount) {
        this.peers2BlocksMap = peers2BlocksMap;
        this.totalBlocksCount = totalBlocksCount;
        data = new byte[totalBlocksCount][];
    }

    public void have(PeerInterface sender, int blockIndex) {
        messageQueue.add(new HaveMessage(blockIndex, sender));
    }

    public void request(PeerInterface sender, int blockIndex) {
        messageQueue.add(new RequestMessage(blockIndex, sender));
    }

    public void piece(PeerInterface sender, int blockIndex, byte[] data) {
        messageQueue.add(new PieceMessage(blockIndex, data, sender));
    }

    boolean processMessage(MessageVisitor visitor) {
        return (messageQueue.isEmpty() ? new IdleMessage(this) : messageQueue.poll()).accept(visitor);
    }
}

abstract class MessageVisitor {
    final Peer peer;

    public MessageVisitor(Peer peer) {
        this.peer = peer;
    }

    abstract boolean visitHaveMessage(HaveMessage message);

    abstract boolean visitRequestMessage(RequestMessage message);

    abstract boolean visitPieceMessage(PieceMessage message);

    abstract boolean visitIdleMessage(IdleMessage message);
}

abstract class Message {
    final PeerInterface sender;

    public Message(PeerInterface sender) {
        this.sender = sender;
    }

    abstract boolean accept(MessageVisitor visitor);
}

class HaveMessage extends Message {
    final int blockIndex;

    public HaveMessage(int blockIndex, PeerInterface sender) {
        super(sender);
        this.blockIndex = blockIndex;
    }

    boolean accept(MessageVisitor visitor) {
        return visitor.visitHaveMessage(this);
    }
}

class RequestMessage extends Message {
    final int blockIndex;

    public RequestMessage(int blockIndex, PeerInterface sender) {
        super(sender);
        this.blockIndex = blockIndex;
    }

    boolean accept(MessageVisitor visitor) {
        return visitor.visitRequestMessage(this);
    }
}

class PieceMessage extends Message {
    final int blockIndex;
    final byte[] data;

    public PieceMessage(int blockIndex, byte[] data, PeerInterface sender) {
        super(sender);
        this.blockIndex = blockIndex;
        this.data = data;
    }

    boolean accept(MessageVisitor visitor) {
        return visitor.visitPieceMessage(this);
    }
}

class IdleMessage extends Message {
    public IdleMessage(PeerInterface sender) {
        super(sender);
    }

    @Override
    boolean accept(MessageVisitor visitor) {
        return visitor.visitIdleMessage(this);
    }
}

/*
 * =============================================================================
 *                    T E S T O V A C I   T R I D A
 * =============================================================================
 */

class Homework4Test {

    static final int blockLength = 8;

    public static void main(String[] args) {

        checkHaveMessage();
        checkRequestMessage();
        checkPieceMessage();
        checkIdleMessage();

    }

    static void checkHaveMessage() {
        PeerInterface peer1 = mock(PeerInterface.class);
        PeerInterface peer2 = mock(PeerInterface.class);
        PeerInterface peer3 = mock(PeerInterface.class);

        Map<PeerInterface, boolean[]> map = new HashMap<>();
        map.put(peer1, new boolean[blockLength]);
        map.put(peer2, new boolean[blockLength]);
        map.put(peer3, new boolean[blockLength]);

        Peer p = new Peer(map, blockLength);
        Homework4 v = new Homework4(p);

        v.visitHaveMessage(new HaveMessage(3,peer1));

        assert map.get(peer1)[3];
        verifyZeroInteractions(peer1);
        verifyZeroInteractions(peer2);
        verifyZeroInteractions(peer3);
        System.out.println("checkHaveMessage done");
    }

    static void checkRequestMessage() {
        PeerInterface peer1 = mock(PeerInterface.class);
        PeerInterface peer2 = mock(PeerInterface.class);
        PeerInterface peer3 = mock(PeerInterface.class);

        Map<PeerInterface, boolean[]> map = new HashMap<>();
        map.put(peer1, new boolean[blockLength]);
        map.put(peer2, new boolean[blockLength]);
        map.put(peer3, new boolean[blockLength]);

        Peer p = new Peer(map, blockLength);
        Homework4 v = new Homework4(p);


        byte[] data1 = {10};
        byte[] data2 = {20};

        p.data[4] = data1;
        v.visitRequestMessage(new RequestMessage(4, peer1));
        verify(peer1).piece(p, 4, data1);
        verifyNoMoreInteractions(peer1);
        verifyZeroInteractions(peer2);
        verifyZeroInteractions(peer3);
        System.out.println("checkRequestMessage done");
    }

    static void checkPieceMessage() {
        PeerInterface peer1 = mock(PeerInterface.class);
        PeerInterface peer2 = mock(PeerInterface.class);
        PeerInterface peer3 = mock(PeerInterface.class);

        Map<PeerInterface, boolean[]> map = new HashMap<>();
        map.put(peer1, new boolean[blockLength]);
        map.put(peer2, new boolean[blockLength]);
        map.put(peer3, new boolean[blockLength]);

        Peer p = new Peer(map, blockLength);
        Homework4 v = new Homework4(p);

        byte[] data1 = {10};
        byte[] data2 = {20};


        v.visitPieceMessage(new PieceMessage(3, data1, peer1));
        verify(peer1).have(p, 3);
        verify(peer2).have(p, 3);
        verify(peer3).have(p, 3);
        System.out.println("checkPieceMessage done");

    }

    static void checkIdleMessage() {

        Map<PeerInterface, boolean[]> map1 = new HashMap<>();

        PeerInterface pi1 = mock(PeerInterface.class);
        PeerInterface pi2 = mock(PeerInterface.class);
        PeerInterface pi3 = mock(PeerInterface.class);
        map1.put(pi1, new boolean[] { true ,  true,  false, true  });
        map1.put(pi2, new boolean[] { false , true , true,  false });
        map1.put(pi3, new boolean[] { true,   false, true,  false });

        Peer p = new Peer(map1, 4);

        Homework4 v1 = new Homework4(p);

        v1.visitIdleMessage(new IdleMessage(p));
        verify(pi1).request(p, 3);
        verifyNoMoreInteractions(pi1);
        System.out.println("checkIdleMessage done");

    }

}
