import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by ciel on 2018/8/27
 */
public class JsUtil {
    private static JsUtil instance = new JsUtil();

    public static JsUtil getInstance() {
        return instance;
    }
    private JsUtil() {
    }


    public String execute(String filePath, String userId) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");

        String jsFileName = filePath;   // 读取js文件

        FileReader reader = null;   // 执行指定脚本
        try {
            reader = new FileReader(jsFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            engine.eval(reader);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        String c = null;

        if(engine instanceof Invocable) {
            Invocable invoke = (Invocable)engine;    // 调用merge方法，并传入参数
            try {
                c = (String)invoke.invokeFunction("generateSignature", userId);
            } catch (ScriptException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    return c;
    }

}
