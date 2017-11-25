import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException, ScriptException {
        Process p = Runtime.getRuntime().exec("python test.py Ira");

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

        System.out.println("Python output:\n");
        String pythonOutput = null;
        while ((pythonOutput = stdInput.readLine()) != null) {
            System.out.println(pythonOutput);
        }

    }
}
