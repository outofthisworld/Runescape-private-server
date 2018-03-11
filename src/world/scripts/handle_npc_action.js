
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
                lines:[]
           }
       ]
   }
}

var dialogueHandlers = {}

dialogueHandlers[types.player] = function sendNpcDialogue(player, npc, npcDialogueObject){
    if(!player || !npc || !npcDialogueObject){
        return;
    }

    if(!npcDialogueObject.lines || ! Array.isArray(npcDialogueObject.lines)){
        return;
    }

    switch(npcDialogueObject.lines.length){
        case 1:
            player.client.outgoingPacketBuilder.sendInterfaceAnimation(4883, expression.getExpression());
            player.client.outgoingPacketBuilder.sendString(NpcDefinition.DEFINITIONS[npc].getName(), 4884);
            player.client.outgoingPacketBuilder.sendString(text[0], 4885);
            player.client.outgoingPacketBuilder.sendNpcModelOnInterface(4883, npc);
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