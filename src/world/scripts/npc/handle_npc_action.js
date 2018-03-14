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
            player.client.outgoingPacketBuilder.send();
            break;
        case 2:
            player.client.outgoingPacketBuilder.sendInterfaceAnimation(4888, npcDialogueObject.expression || expressions.calm);
            player.client.outgoingPacketBuilder.sendInterfaceText(npc.getNpcDefinition().name, 4889);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[0], 4890);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[1], 4891);
            player.client.outgoingPacketBuilder.sendNpcModelOnInterface(4888, npc.id);
            player.client.outgoingPacketBuilder.sendChatInterface(4887);
            player.client.outgoingPacketBuilder.send();
            break;
        case 3:
            player.client.outgoingPacketBuilder.sendInterfaceAnimation(4894, npcDialogueObject.expression || expressions.calm);
            player.client.outgoingPacketBuilder.sendInterfaceText(npc.getNpcDefinition().name, 4895);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[0], 4896);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[1], 4897);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[2], 4898);
            player.client.outgoingPacketBuilder.sendNpcModelOnInterface(4894, npc.id);
            player.client.outgoingPacketBuilder.sendChatInterface(4893);
            player.client.outgoingPacketBuilder.send();
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
            player.client.outgoingPacketBuilder.send();
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
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[0], 971);
            player.client.outgoingPacketBuilder.sendPlayerModelOnInterface(969);
            player.client.outgoingPacketBuilder.sendChatInterface(968);
            player.client.outgoingPacketBuilder.send();
            break;
        case 2:
            player.client.outgoingPacketBuilder.sendInterfaceAnimation(974, playerDialogueObject.expression || expressions.calm);
            player.client.outgoingPacketBuilder.sendInterfaceText(player.getUsername(), 975);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[0], 976);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[1], 977);
            player.client.outgoingPacketBuilder.sendPlayerModelOnInterface(974)
            player.client.outgoingPacketBuilder.sendChatInterface(973);
            player.client.outgoingPacketBuilder.send();
            break;
        case 3:
            player.client.outgoingPacketBuilder.sendInterfaceAnimation(980, playerDialogueObject.expression || expressions.calm);
            player.client.outgoingPacketBuilder.sendInterfaceText(player.getUsername(), 981);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[0], 982);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[1], 983);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[2], 984);
            player.client.outgoingPacketBuilder.sendPlayerModelOnInterface(980);
            player.client.outgoingPacketBuilder.sendChatInterface(979);
            player.client.outgoingPacketBuilder.send();
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
            player.client.outgoingPacketBuilder.send();
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
            player.client.outgoingPacketBuilder.send();
            break;
        case 3:
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[0], 2471);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[1], 2472);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[2], 2473);
            player.client.outgoingPacketBuilder.sendChatInterface(2469);
            player.client.outgoingPacketBuilder.send();
            break;
        case 4:
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[0], 8209);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[1], 8210);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[2], 8211);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[3], 8212);
            player.client.outgoingPacketBuilder.sendChatInterface(8207);
            player.client.outgoingPacketBuilder.send();
            break;
        case 5:
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[0], 8221);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[1], 8222);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[2], 8223);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[3], 8224);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[4], 8225);
            player.client.outgoingPacketBuilder.sendChatInterface(8219);
            player.client.outgoingPacketBuilder.send();
            break;
        default:
            throw new IllegalArgumentException("Illegal dialogue option " + "length: " + text.length);
    }
}

var playerMap = {}



function interrupt(player){
    if(!player){
        throw new Error("Invlaid arguments passed to interrupt, expected non null player.")
    }

    player.client.outgoingPacketBuilder.closeInterfaces();
    playerMap[player.username] = null;
}

function forwardDialogue(player){

    if(!player){
        throw new Error("Invlaid arguments passed to forwardDialogue, expected non null player.")
    }

    if(!player || !player.username){
        return;
    }

    if(!playerMap[player.username]){
        playerMap[player.username] = null;
        return;
    }

    playerMap[player.username].currentDialogueOffset += 1;

    var displayDialogue = playerMap[player.username].currentDialogue.dialogue[playerMap[player.username].currentDialogueOffset];

    if(!displayDialogue){
        playerMap[player.username] = null;
        return;
    }

    dialogueHandlers[displayDialogue.type](player, playerMap[player.username].npc,displayDialogue);
}

function selectOption(player,optionId){

    if(!player){
        throw new Error("Invlaid arguments passed to selectOption, expected non null player.")
    }

    var displayDialogue = playerMap[player.username].currentDialogue.dialogue[playerMap[player.username].currentDialogueOffset];

    if(!displayDialogue.type == type.options){
        playerMap[player.username] = null;
        return;
    }

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
    }else if (Array.isArray(option.dialogue)) {
        var currentDialogue = option.dialogue[0];

        if (!currentDialogue) {
            interrupt(player);
            return;
        }

        dialogueHandlers[displayDialogue.type](player, npc, currentDialogue);
        playerMap[player.username].currentDialogue = option;
        playerMap[player.username].currentDialogueOffset = 1;
    } else {
        throw new Error('Type error, handler object is not array or function');
    }
}

function handleNpcActionOne(player, npc) {

    if(!player || !npc){
        throw new Error("Invalid args passed to handleNpcActionOne, expected non null player/npc");
    }

    playerMap[player.username] = {};
    playerMap[player.username].currentDialogue = npc_dialogues[npc.getNpcDefinition().getName()];
    playerMap[player.username].currentDialogueOffset = 0;
    playerMap[player.username].npc = npc;


    if (!Array.isArray(playerMap[player.username].currentDialogue.dialogue)) {
        throw new Error('Invalid dialogue');
    }

    var displayDialogue = playerMap[player.username].currentDialogue.dialogue[playerMap[player.username].currentDialogueOffset];


    if (!displayDialogue || displayDialogue.type == undefined || !Array.isArray(displayDialogue.lines)) {
        interrupt(player);
        throw new Error("Invalid dialogue 1");
    }

    dialogueHandlers[displayDialogue.type](player, npc, displayDialogue);
}