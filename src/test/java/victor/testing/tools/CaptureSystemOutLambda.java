package victor.testing.tools;

import org.apache.commons.io.output.TeeOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class CaptureSystemOutLambda {
    public static String capture(Runnable r) {

        PrintStream original = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        TeeOutputStream tee = new TeeOutputStream(original, baos);
        System.setOut(new PrintStream(tee));
        try {

            r.run();

            return baos.toString();
        } finally {
            System.setOut(original);
        }
    }
}
