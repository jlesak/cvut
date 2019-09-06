import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

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