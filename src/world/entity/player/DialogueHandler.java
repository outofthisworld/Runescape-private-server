package world.entity.player;

import util.integrity.Preconditions;
import world.WorldManager;
import world.entity.npc.Npc;
import world.entity.player.Player;

import javax.script.ScriptException;

public class DialogueHandler {

    private final Player player;

    public DialogueHandler(Player player){
        Preconditions.notNull(player);
        this.player = player;
    }

    public void forwardDialogue(){
        try {
            WorldManager.getScriptManager().getInvocable().invokeFunction("forwardDialogue",this.player);
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void interruptDialogue(){
        try {
            WorldManager.getScriptManager().getInvocable().invokeFunction("interrupt",this.player);
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void startDialogue(Npc npc){
        try {
            WorldManager.getScriptManager().getInvocable().invokeFunction("handleNpcActionOne",this.player,npc);
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void selectOption(int optionId){
        try {
            WorldManager.getScriptManager().getInvocable().invokeFunction("handleNpcActionOne",this.player,optionId);
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
