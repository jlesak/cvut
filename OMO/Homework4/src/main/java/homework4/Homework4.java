package homework4;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Instance implementujici PeerInterface reprezentuje lokalni uzel nebo uzel
 * pripojeny pres sit.
 */
interface PeerInterface {
    /**
     * Sdeli vzdalenemu uzlu, ze uzel sender prave obdrzel blok s indexem
     * blockIndex.
     */
    void have(PeerInterface sender, int blockIndex);

    /**
     * Pozada vzdaleny uzel o blok s indexem blockIndex.
     */
    void request(PeerInterface sender, int blockIndex);

    /**
     * Zasle vzdalenemu uzlu blok s indexem blockIndex.
     */
    void piece(PeerInterface sender, int blockIndex, byte[] data);
}

/**
 * Instance tridy Peer reprezentuje lokalni uzel.
 */
class Peer implements PeerInterface {
    /**
     * Fronta nezpracovanych zprav.
     */
    final Queue<Message> messageQueue = new LinkedBlockingQueue<>();
    /**
     * Mapovani z uzlu na bitovou mapu urcujici ktere bloky ma dany uzel k
     * dispozici.
     */
    final Map<PeerInterface, boolean[]> peers2BlocksMap;
    /**
     * Celkovy pocet bloku ve stahovanem souboru.
     */
    final int totalBlocksCount;
    /**
     * Pole se stazenymi bloky.
     */
    final byte[][] data;
    /**
     * Pocet stazenych bloku.
     */
    int downloadedBlocksCount = 0;

    public Peer(Map<PeerInterface, boolean[]> peers2BlocksMap, int totalBlocksCount) {
        this.peers2BlocksMap = peers2BlocksMap;
        this.totalBlocksCount = totalBlocksCount;
        data = new byte[totalBlocksCount][];
    }

    /**
     * Prijme zpravu "have" a prida ji do fronty zprav.
     */
    public void have(PeerInterface sender, int blockIndex) {
        messageQueue.add(new HaveMessage(blockIndex, sender));
    }

    /**
     * Prijme zpravu "request" a prida ji do fronty zprav.
     */
    public void request(PeerInterface sender, int blockIndex) {
        messageQueue.add(new RequestMessage(blockIndex, sender));
    }

    /**
     * Prijme zpravu "piece" a prida ji do fronty zprav.
     */
    public void piece(PeerInterface sender, int blockIndex, byte[] data) {
        messageQueue.add(new PieceMessage(blockIndex, data, sender));
    }

    /**
     * Vyzvedne nejstarsi zpravu z fronty zprav a zpracuje ji pomoci zadaneho
     * navstevnika. Pokud ve fronte zadna zprava neni, zasle sam sobe a zpracuje
     * zpravu "idle". Vrati true v pripade, ze tento uzel stahnul vsechny bloky,
     * false jinak.
     */
    boolean processMessage(MessageVisitor visitor) {
        return (messageQueue.isEmpty() ? new IdleMessage(this) : messageQueue.poll()).accept(visitor);
    }
}

/**
 * Instance tridy MessageVisitor reprezentuji navstevniky zpracovavajici zpravy.
 */
abstract class MessageVisitor {
    final Peer peer;

    public MessageVisitor(Peer peer) {
        this.peer = peer;
    }

    /*
     * Zpracuje zpravu "have": v lokalnim uzlu vyznaci, ze dany vzdaleny uzel ma
     * k dispozici blok s danym indexem.
     *
     * Vzdy vrati false.
     */
    abstract boolean visitHaveMessage(HaveMessage message);

    /*
     * Zpracuje zpravu "request": pokud ma lokalni uzel pozadovany blok k
     * dispozici, obratem ho posle vzdalenemu uzlu pomoci zpravy "piece". Pokud
     * ne, pozadavek ignoruje.
     *
     * Vzdy vrati false.
     */
    abstract boolean visitRequestMessage(RequestMessage message);

    /*
     * Zpracuje zpravu "piece": ulozi obdrzena data do lokalniho uzlu, zvysi
     * pocet stazenych bloku a vsem vzdalenym uzlum (vcetne toho, od ktereho
     * data obdrzel) rozesle zpravu "have".
     *
     * Vrati true, pokud lokalni uzel stahl vsechny bloky, false jinak.
     */
    abstract boolean visitPieceMessage(PieceMessage message);

    /*
     * Zpracuje zpravu "idle": vybere nejvzacnejsi jeste nestazeny blok a zazada
     * o nej u nektereho z jeho vlastniku. Nejvzacnejsi blok je takovy, ktery
     * vlastni nejmene vzdalenych uzlu. Pokud je nejvzacnejsich bloku nekolik,
     * vybere jeden z nich.
     *
     * Vzdy vrati false.
     */
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

class Homework4 extends MessageVisitor {
    public Homework4(Peer peer) {
        super(peer);
    }

    /*
     * Zpracuje zpravu "have": v lokalnim uzlu vyznaci, ze dany vzdaleny uzel ma
     * k dispozici blok s danym indexem.
     *
     * Vzdy vrati false.
     */
    @Override
    boolean visitHaveMessage(HaveMessage message) {
        boolean[] blocks;
        blocks = peer.peers2BlocksMap.get(message.sender);
        if (blocks == null)return false;
        blocks[message.blockIndex] = true;
        return false;
    }
    /*
        * Zpracuje zpravu "request": pokud ma lokalni uzel pozadovany blok k
        * dispozici, obratem ho posle vzdalenemu uzlu pomoci zpravy "piece". Pokud
        * ne, pozadavek ignoruje.
        *
        * Vzdy vrati false.
        */
    @Override
    boolean visitRequestMessage(RequestMessage message) {
        if (peer.data[message.blockIndex] != null){
            message.sender.piece(peer, message.blockIndex, peer.data[message.blockIndex]);
        }

        return false;
    }

    /*
       * Zpracuje zpravu "piece": ulozi obdrzena data do lokalniho uzlu, zvysi
       * pocet stazenych bloku a vsem vzdalenym uzlum (vcetne toho, od ktereho
       * data obdrzel) rozesle zpravu "have".
       *
       * Vrati true, pokud lokalni uzel stahl vsechny bloky, false jinak.
       */
    @Override
    boolean visitPieceMessage(PieceMessage message) {
        peer.data[message.blockIndex] = message.data; //ulozi data do lokalniho uzlu
        peer.downloadedBlocksCount++;//zvysi pocet stazenych bloku
        for (PeerInterface p: peer.peers2BlocksMap.keySet()){
            p.have(peer, message.blockIndex);//vsem vzdalenym uzlum (vcetne toho, od ktereho data obdrzel) rozesle zpravu "have"
        }

        return peer.downloadedBlocksCount==peer.totalBlocksCount;//Vrati true, pokud lokalni uzel stahl vsechny bloky, false jinak.
    }
    /*
         * Zpracuje zpravu "idle": vybere nejvzacnejsi jeste nestazeny blok a zazada
         * o nej u nektereho z jeho vlastniku. Nejvzacnejsi blok je takovy, ktery
         * vlastni nejmene vzdalenych uzlu. Pokud je nejvzacnejsich bloku nekolik,
         * vybere jeden z nich.
         *
         * Vzdy vrati false.
         */
    @Override
    boolean visitIdleMessage(IdleMessage message) {
        int minValue = Integer.MAX_VALUE;
        int blockIndex = 0;
        int[] rarity = new int[peer.totalBlocksCount]; //id tohoto pole oznacuje id bloku, hodnota na teto pozici oznacuje raritu bloku
        Map<Integer, PeerInterface> blockOwnerMap = new HashMap<>();

        ////
        for (PeerInterface p : peer.peers2BlocksMap.keySet()){
            boolean[] blocks = peer.peers2BlocksMap.get(p);
            for (int i = 0; i < blocks.length; i++) {
                if (peer.data[i] != null) continue;
                if (blocks[i]) {
                    rarity[i]++;
                    blockOwnerMap.put(i, p);
                }
            }
        }

        for (int i = 0; i < rarity.length; i++) {
            if (rarity[i] < minValue) {
                minValue = rarity[i];
                blockIndex = i;
            }
        }
        /////
        blockOwnerMap.get(blockIndex).request(message.sender, blockIndex); //zazada o blok dat
        return false;
    }

    /**
     * Zjisti, pokud peer ma blok dat s danym indexem
     * @param peer peer, u ktereho chci existenci bloku zjisti
     * @param block blok
     * @return boolean
     */
    boolean peerHasBlock(Peer peer, byte[] block){
        for (byte[] storedByte : peer.data) {
            if (block == storedByte) {
                return true;
            }
        }
        return false;
    }
    // zde doplnte svuj kod
}