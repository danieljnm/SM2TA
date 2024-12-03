package danieljnm.sm2ta;

import org.eclipse.xtext.xbase.lib.Conversions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("all")
public class TranslatorTest {
  @Test
  public void emptyMachineTest() {
    int _length = ((Object[])Conversions.unwrapArray(Translator.getStateMachine().getStates(), Object.class)).length;
    boolean _equals = (_length == 0);
    Assertions.assertEquals(Boolean.valueOf(true), Boolean.valueOf(_equals));
  }
}
