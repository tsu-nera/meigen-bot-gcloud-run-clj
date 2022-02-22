import nl.epij.gcf.RingHttpFunction;

public class Tweet extends RingHttpFunction {
  public String getHandler() { return "functions.tweet/app"; }
}
