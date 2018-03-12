
var types = {
    dialogue:0,
    player:0,
    npc:1,
    options:2
}

var expressions = {
    happy:588,
    calm:589,
    calm_continued:590,
    default:591,
    evil:592,
    evil_continued:593,
    delighted_evil:594,
    annoyed:595,
    distressed:596,
    distressed_continued:597,
    disoriented_left:600,
    disoriented_right:601,
    uninterested:602,
    sleepy:603,
    plain_evil:603,
    laughing:605,
    laughing_two:608,
    longer_laughing:606,
    longer_laughing_two:607,
    evil_laugh_short:609,
    slightly_sad:610,
    sad:599,
    very_sad:611,
    other:612,
    near_tears:598,
    near_tears_two:613,
    angry_1:614,
    angry_2:615,
    angry_3:616,
    angry_3:617
}

var npc_dialogues = {
   "Baraek":{
       type:types.dialogue,
       dialogue:[
           {
                type: types.player,
                expression:expressions.happy,
                lines:[
                    "Hello",
                    "Welcome to runescape"
                ]
           },
           {
               type: types.npc,
               lines:[
                   "Hey, you're the new one! How much gold do you want?"
               ]
           },
           {
               type: types.options,
               lines:[
                   "1M GP",
                   "2M GP"
               ]
           }
       ]
   }
}

var dialogueHandlers = {}

/**
* Handles sending npc dialogues.
* */
dialogueHandlers[types.npc] = function sendNpcDialogue(player, npc, npcDialogueObject){
    if(!player || !npc || !npcDialogueObject){
        return;
    }

    /*Build the npc dialogue or get the array of lines.*/
    var lineArr = typeof npcDialogueObject.lines === 'function'?npcDialogueObject.lines(player,npc):npcDialogueObject.lines;

    if(!Array.isArray(lineArr)){
        return;
    }

    switch(lineArr.length){
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

dialogueHandlers[types.player] = function sendPlayerDialogue(player, npc, playerDialogueObject){
    if(!player || !npc || !playerDialogueObject){
        return;
    }

    /*Build the npc dialogue or get the array of lines.*/
    var lineArr = typeof playerDialogueObject.lines === 'function'?playerDialogueObject.lines(player,npc):playerDialogueObject.lines;

    if(!Array.isArray(lineArr)){
        return;
    }

    switch (lineArr.length) {
        case 1:
            player.client.outgoingPacketBuilder.sendInterfaceAnimation(969, playerDialogueObject.expression || expressions.calm);
            player.client.outgoingPacketBuilder.sendInterfaceText(player.getUsername(), 970);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[0], 971);
            player.client.outgoingPacketBuilder.sendPlayerModelOnInterface(969);
            player.client.outgoingPacketBuilder.sendChatInterface(968);
            break;
        case 2:
            player.client.outgoingPacketBuilder.sendInterfaceAnimation(974, playerDialogueObject.expression || expressions.calm);
            player.client.outgoingPacketBuilder.sendInterfaceText(player.getUsername(), 975);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[0], 976);
            player.client.outgoingPacketBuilder.sendInterfaceText(lineArr[1], 977);
            player.client.outgoingPacketBuilder.sendPlayerModelOnInterface(974);
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

dialogueHandlers[types.options] = function optionsDialogue(player, npc, optionsDialogue){
    if(!player || !npc || !optionsDialogue){
        return;
    }

    /*Build the npc dialogue or get the array of lines.*/
    var lineArr = typeof optionsDialogue.lines === 'function'?optionsDialogue.lines(player,npc):optionsDialogue.lines;

    if(!Array.isArray(lineArr)){
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


function handleNpcActionOne(nextDialogue,player,npc){

    if(npc_dialogues[npc.getNpcDefinition().getName()] == undefined){
        return;
    }
    nextDialogue = nextDialogue < 0? 0: nextDialogue;

    var dialogueObj = npc_dialogues[npc.getNpcDefinition().name];
    var stage = {
        nextDialogueStage:0
    }

    if(dialogueObj.type == types.dialogue){

        if(Array.isArray(dialogueObj.dialogue) && dialogueObj.dialogue[nextDialogue]) {

            var nextDialogue = dialogueObj.dialogue[nextDialogue];

            if (nextDialogue.type != undefined && dialogueHandlers[nextDialogue.type]) {
                dialogueHandlers[nextDialogue.type](player, npc, nextDialogue);
            }

        }else{
            //Close chat interface
            stage.nextDialogueStage = -1;
            return stage;
        }
    }

    stage.nextDialogueStage+=1;
    return stage;
}