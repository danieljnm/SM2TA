package danieljnm.sm2ta;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("all")
public class HelloXtendTest {
  @Test
  public void testHelloXtend() {
    Assertions.assertEquals("Hello Xtend!", HelloXtend.getMessage().toString());
  }
}
