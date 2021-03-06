package world.scripts;


import javax.script.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public final class ScriptManager {
    private static ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
    private static Invocable invocable = (Invocable) engine;

    public ScriptManager(String scriptDir){
        loadScripts(scriptDir);
    }

    private final void loadScripts(String dir)  {
        File f = new File(dir);
        if(!f.exists()){
            throw new IllegalArgumentException("Invalid dir");
        }
        for(File file: f.listFiles()){
            if(file.isDirectory()) {
                loadScripts(file.getAbsolutePath());
            }else if(file.getName().endsWith(".js")){
                try {
                    engine.eval(new FileReader(file));
                } catch (ScriptException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Invocable getInvocable() {
        return invocable;
    }
}
