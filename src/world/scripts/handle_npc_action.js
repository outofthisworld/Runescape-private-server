
var types = {
    dialogue:0,
    player:0,
    npc:1
}


var npc_dialogues = {
   "Baraek":{
       type:types.dialogue,
       dialogue:[
           {
                type: types.player,
                expression:1,
                lines:[
                    "Hello",
                    ""
                ]
           },
           {
               type: types.npc,
               expression:1,
               lines:[]
           }
       ]
   }
}

var dialogueHandlers = {}

/**
* Handles sending npc dialogues.
* */
dialogueHandlers[types.player] = function sendNpcDialogue(player, npc, npcDialogueObject){
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
            player.client.outgoingPacketBuilder.sendInterfaceAnimation(4883, npcDialogueObject.expression || -1);
            player.client.outgoingPacketBuilder.sendString(npc.getNpcDefintion().name, 4884);
            player.client.outgoingPacketBuilder.sendString(npcDialogueObject.lines[0], 4885);
            player.client.outgoingPacketBuilder.sendNpcModelOnInterface(4883, npc.id);
            player.client.outgoingPacketBuilder.sendChatInterface(4882);
            break;
        case 2:
            break;
        case 3:
            break;
        case 4:
            break;
    }

}

dialogueHandlers[types.npc] = function sendPlayerDialogue(player, npc, playerDialogueObject){

}


function handleNpcActionOne(player,npc){
    if(typeof player != "Object" || typeof npc != "Object"){
        throw new Error("Invalid params passed to handleNpcActionOne")
    }
    if(npc_dialogues[npc.getNpcDefinition().getName()] == undefined){
        return;
    }

    var dialogueObj = npc_dialogues[npc.getNpcDefinition().getName()];
    if(dialogueObj.type == types.dialogue && dialogueObj.dialogue){
        for(obj of dialogueObj.dialogue){
            if(obj.type && dialogueHandlers[obj.type] && typeof dialogueHandlers[obj.type] === 'function'){
                dialogueHandlers[obj.type](player,npc,obj);
            }
        }
    }
}