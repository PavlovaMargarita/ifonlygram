package ifonlygram.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.String.format;

public class ExecutePython {
    private static final String PYTHON_EXEC = "python %s -i %s";

    public static void executePython(final String script, final String parameter) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(format(PYTHON_EXEC, script, parameter));
//
//            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
//            return stdInput.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
