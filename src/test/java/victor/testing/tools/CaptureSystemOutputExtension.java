package victor.testing.tools;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public class CaptureSystemOutputExtension implements InvocationInterceptor {
  private CaptureOutputStream captureOut;

  private CaptureOutputStream captureErr;

  private ByteArrayOutputStream copy;

  public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
    this.copy = new ByteArrayOutputStream();
    this.captureOut = new CaptureOutputStream(System.out, copy);
    this.captureErr = new CaptureOutputStream(System.err, copy);
    System.setOut(new PrintStream(captureOut));
    System.setErr(new PrintStream(captureErr));
    try {

      invocation.proceed();

    } finally {
      System.setOut(captureOut.getOriginal());
      System.setErr(captureErr.getOriginal());
      this.copy = null;
    }
  }

  @Override
  public String toString() {
    try {
      captureOut.flush();
      captureErr.flush();
    } catch (IOException ex) {
      // ignore
    }
    return copy.toString();
  }

  public List<String> lines() {
    return toString().lines().collect(Collectors.toList());
  }

  private static class CaptureOutputStream extends OutputStream {
    private final PrintStream original;
    private final OutputStream copy;

    CaptureOutputStream(PrintStream original, OutputStream copy) {
      this.original = original;
      this.copy = copy;
    }

    PrintStream getOriginal() {
      return original;
    }

    @Override
    public void write(int b) throws IOException {
      copy.write(b);
      original.write(b);
      original.flush();
    }

    @Override
    public void write(byte[] b) throws IOException {
      write(b, 0, b.length);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
      copy.write(b, off, len);
      original.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
      copy.flush();
      original.flush();
    }

  }
}