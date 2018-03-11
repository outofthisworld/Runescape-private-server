



var obj = {
   "Baraek":{
       type:"dialogue",
       dialogue:[
           {

           }
       ]
   }
}




function handleNpcActionOne(player,npc){
    if(typeof player != "Object" || typeof npc != "Object"){
        throw new Error("Invalid params passed to handleNpcActionOne")
    }

    npc.getNpcDefinition().getName();
}