load('src/world/scripts/npc/dialogue_types.js');
load('src/world/scripts/npc/dialogue_expressions.js');
load('src/world/scripts/npc/npc_dialogues.js');

var dialogueHandlers = {}

/**
 * Handles sending npc dialogues.
 * */
dialogueHandlers[types.npc] = function sendNpcDialogue(player, npc, npcDialogueObject) {
    if (!player || !npc || !npcDialogueObject) {
        return;
    }

    /*Build the npc dialogue or get the array of lines.*/
    var lineArr = typeof npcDialogueObject.lines === 'function' ? npcDialogueObject.lines(player, npc) : npcDialogueObject.lines;

    if (!Array.isArray(lineArr)) {
        return;
    }

    switch (lineArr.length) {
        case 1:
            player.client.outgoingPacketBuilder.sendInterfaceAnimation(4883, npcDialogueObject.expression || expressions.calm);
            player.client.outgoingPacketBuilder.sendInterfaceText(npc.getNpcDefinition().name, 4884);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[0], 4885);
            player.client.outgoingPacketBuilder.sendNpcModelOnInterface(4883, npc.id);
            player.client.outgoingPacketBuilder.sendChatInterface(4882);
            break;
        case 2:
            player.client.outgoingPacketBuilder.sendInterfaceAnimation(4888, npcDialogueObject.expression || expressions.calm);
            player.client.outgoingPacketBuilder.sendInterfaceText(npc.getNpcDefinition().name, 4889);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[0], 4890);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[1], 4891);
            player.client.outgoingPacketBuilder.sendNpcModelOnInterface(4888, npc.id);
            player.client.outgoingPacketBuilder.sendChatInterface(4887);
            break;
        case 3:
            player.client.outgoingPacketBuilder.sendInterfaceAnimation(4894, npcDialogueObject.expression || expressions.calm);
            player.client.outgoingPacketBuilder.sendInterfaceText(npc.getNpcDefinition().name, 4895);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[0], 4896);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[1], 4897);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[2], 4898);
            player.client.outgoingPacketBuilder.sendNpcModelOnInterface(4894, npc.id);
            player.client.outgoingPacketBuilder.sendChatInterface(4893);
            break;
        case 4:
            player.client.outgoingPacketBuilder.sendInterfaceAnimation(4901, npcDialogueObject.expression || expressions.calm);
            player.client.outgoingPacketBuilder.sendInterfaceText(npc.getNpcDefinition().name, 4902);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[0], 4903);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[1], 4904);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[2], 4905);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[3], 4906);
            player.client.outgoingPacketBuilder.sendNpcModelOnInterface(4901, npc.id);
            player.client.outgoingPacketBuilder.sendChatInterface(4900);
            break;
        default:
            throw new Error("Invalid lines length");
    }

}

dialogueHandlers[types.player] = function sendPlayerDialogue(player, npc, playerDialogueObject) {
    if (!player || !npc || !playerDialogueObject) {
        return;
    }

    /*Build the npc dialogue or get the array of lines.*/
    var lineArr = typeof playerDialogueObject.lines === 'function' ? playerDialogueObject.lines(player, npc) : playerDialogueObject.lines;

    if (!Array.isArray(lineArr)) {
        return;
    }

    switch (lineArr.length) {
        case 1:
            player.client.outgoingPacketBuilder.sendInterfaceAnimation(969, playerDialogueObject.expression || expressions.calm);
            player.client.outgoingPacketBuilder.sendInterfaceText(player.getUsername(), 970);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[0], 971));
            player.client.outgoingPacketBuilder.sendPlayerModelOnInterface(969);
            player.client.outgoingPacketBuilder.sendChatInterface(968);
            break;
        case 2:
            player.client.outgoingPacketBuilder.sendInterfaceAnimation(974, playerDialogueObject.expression || expressions.calm);
            player.client.outgoingPacketBuilder.sendInterfaceText(player.getUsername(), 975);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[0], 976));
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[1], 977);
            player.client.outgoingPacketBuilder.sendPlayerModelOnInterface(974)
            player.client.outgoingPacketBuilder.sendChatInterface(973);
            break;
        case 3:
            player.client.outgoingPacketBuilder.sendInterfaceAnimation(980, playerDialogueObject.expression || expressions.calm);
            player.client.outgoingPacketBuilder.sendInterfaceText(player.getUsername(), 981);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[0], 982);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[1], 983);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[2], 984);
            player.client.outgoingPacketBuilder.sendPlayerModelOnInterface(980);
            player.client.outgoingPacketBuilder.sendChatInterface(979);
            break;
        case 4:
            player.client.outgoingPacketBuilder.sendInterfaceAnimation(987, playerDialogueObject.expression || expressions.calm);
            player.client.outgoingPacketBuilder.sendInterfaceText(player.getUsername(), 988);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[0], 989);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[1], 990);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[2], 991);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[3], 992);
            player.client.outgoingPacketBuilder.sendPlayerModelOnInterface(987);
            player.client.outgoingPacketBuilder.sendChatInterface(986);
            break;
        default:
            throw new Error("Illegal player dialogue " + "length: " + text.length);
    }

}

var options = {
    0: [14445, 2471, 8209, 8221],
    1: [14446, 2472, 8210, 8222],
    2: [2473, 8211, 8223],
    3: [8212, 8224],
    5: [8225]
}

dialogueHandlers[types.options] = function optionsDialogue(player, npc, optionsDialogue) {
    if (!player || !npc || !optionsDialogue) {
        return;
    }

    /*Build the npc dialogue or get the array of lines.*/
    var lineArr = typeof optionsDialogue.lines === 'function' ? optionsDialogue.lines(player, npc) : optionsDialogue.lines;

    if (!Array.isArray(lineArr)) {
        return;
    }

    switch (lineArr.length) {
        case 2:
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[0], 14445);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[1], 14446);
            player.client.outgoingPacketBuilder.sendChatInterface(14443);
            break;
        case 3:
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[0], 2471);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[1], 2472);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[2], 2473);
            player.client.outgoingPacketBuilder.sendChatInterface(2469);
            break;
        case 4:
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[0], 8209);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[1], 8210);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[2], 8211);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[3], 8212);
            player.client.outgoingPacketBuilder.sendChatInterface(8207);
            break;
        case 5:
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[0], 8221);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[1], 8222);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[2], 8223);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[3], 8224);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[4], 8225);
            player.client.outgoingPacketBuilder.sendChatInterface(8219);
            break;
        default:
            throw new IllegalArgumentException("Illegal dialogue option " + "length: " + text.length);
    }
}

var playerMap = {}

function handleNpcActionOne(player, npc, optionId) {

    if(!playerMap){
        throw new Error("player map undefined");
    }



    if (!playerMap[player.username] || !playerMap[player.username].currentDialogue || playerMap[player.username].currentDialogueOffset == undefined) {
        playerMap[player.username] = {};
        playerMap[player.username].currentDialogue = npc_dialogues[npc.getNpcDefinition().getName()];
        playerMap[player.username].currentDialogueOffset = 0;
        playerMap[player.username].npc = npc;
    }else{
        print("found existing")
    }

    if(!playerMap[player.username].currentDialogue){
        print("no dialogue")
        return;
    }
    if (!Array.isArray(playerMap[player.username].currentDialogue.dialogue)) {
        print("no aray")
        throw new Error('Invalid dialogue');
    }

    var displayDialogue = playerMap[player.username].currentDialogue.dialogue[playerMap[player.username].currentDialogueOffset];

    print(JSON.stringify(displayDialogue))

    if (!displayDialogue || displayDialogue.type == undefined) {
        print("no displayDialogue dialogue")
        playerMap[player.username] = null;
        return;
    }

    if (displayDialogue.type == types.player || displayDialogue.type == types.npc || (displayDialogue.type == types.options && optionId < 0)) {
        player.client.outgoingPacketBuilder.closeInterfaces();
        dialogueHandlers[displayDialogue.type](player, npc || playerMap[player.username].npc, displayDialogue);
        print("advancing")
        if (displayDialogue.type == types.player || displayDialogue.type == types.npc) {
            print("advancing 2")
            playerMap[player.username].currentDialogueOffset += 1;
        }
    }else if (optionId != -1 && playerMap[player.username].currentDialogue.type == types.options && Array.isArray(displayDialogue.handlers)) {
        print("advancing")
        var optId = -1;

        if (!options) throw new Error("Illegal state, options mapping does not exist");

        for (k in options) {
            var arr = options[k];
            if (!Array.isArray(arr)) {
                continue;
            }
            if (arr.indexOf(optionId) != -1) {
                optId = k;
                break;
            }
        }

        if (optId == -1) {
            playerMap[player.username] = null;
            return;
        }

        var option = displayDialogue.handlers[optId];

        if (!option) {
            playerMap[player.username] = null;
            return;
        }

        if (typeof option == 'function') {
            option(player, npc);
            playerMap[player.username] = null;
            return;
        }

        if (option && Array.isArray(option.dialogue)) {
            var currentDialogue = option.dialogue[0];

            if (!currentDialogue) {
                playerMap[player.username] = null;
                return;
            }

            dialogueHandlers[displayDialogue.type](player, npc, currentDialogue);
            playerMap[player.username].currentDialogue = option;
            playerMap[player.username].currentDialogueOffset = 1;
        } else {
            playerMap[player.username] = null;
            return;
        }
    } else {
        print("no options matched")
        playerMap[player.username] = null;
    }
}