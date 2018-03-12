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
                ],
                handlers:[
                    function(player,npc){

                    },
                    {
                        type:types.dialogue,
                        dialogue:[
                            {
                                type: types.npc,
                                lines:[
                                    "Hey, you're the new one! How much gold do you want?"
                                ]
                            }
                        ]
                    }
                ]
            }
        ]
    }
}