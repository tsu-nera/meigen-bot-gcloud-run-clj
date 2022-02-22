import nl.epij.gcf.RingHttpFunction;

public class HelloWorld extends RingHttpFunction {
  public String getHandler() { return "functions.helloworld/app"; }
}
