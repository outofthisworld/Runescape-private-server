package net.packets.incoming;

import net.Client;
import net.buffers.InputBuffer;

import java.util.logging.Logger;


public class FollowPlayerPacket extends Packet {
    private static final Logger logger = Logger.getLogger(FollowPlayerPacket.class.getName());

    @Override
    public void handle(Client c, int packetOpcode, InputBuffer in) throws Exception {
        /*int followId = in.readShort(false, ByteOrder.LITTLE);
        Player follow = Rs2Engine.getWorld().getPlayers()[followId];

        if (follow == null) {
            return;
        }

        SkillEvent.fireSkillEvents(player);
        player.follow(follow);*/
    }

    @Override
    public boolean handlesOpcode(int opcode) {
        return opcode == 39;
    }
}
