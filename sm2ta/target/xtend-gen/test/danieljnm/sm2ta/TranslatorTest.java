package danieljnm.sm2ta;

import org.eclipse.xtext.xbase.lib.Conversions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("all")
public class TranslatorTest {
  @BeforeEach
  public void reset() {
    Translator.reset();
  }

  @Test
  public void emptyMachineTest() {
    int _length = ((Object[])Conversions.unwrapArray(Translator.getStateMachine().getStates().values(), Object.class)).length;
    boolean _equals = (_length == 0);
    Assertions.assertEquals(Boolean.valueOf(true), Boolean.valueOf(_equals));
  }

  @Test
  public void basicMachineOneState() {
    Translator.getStateMachine().addState("Idle");
    int _length = ((Object[])Conversions.unwrapArray(Translator.getStateMachine().getStates().values(), Object.class)).length;
    boolean _equals = (_length == 1);
    Assertions.assertEquals(Boolean.valueOf(true), Boolean.valueOf(_equals));
    State _get = Translator.getStateMachine().getStates().get("Idle");
    boolean _tripleNotEquals = (_get != null);
    Assertions.assertEquals(Boolean.valueOf(true), Boolean.valueOf(_tripleNotEquals));
  }

  @Test
  public void basicMachineWithOneTransition() {
    Translator.addState("Idle");
    Translator.addTransition("Idle", "Planning", "Ready");
    int _length = ((Object[])Conversions.unwrapArray(Translator.getStateMachine().getStates().values(), Object.class)).length;
    boolean _equals = (_length == 2);
    Assertions.assertEquals(Boolean.valueOf(true), Boolean.valueOf(_equals));
    int _length_1 = ((Object[])Conversions.unwrapArray(Translator.getStateMachine().getStates().get("Idle").getTransitions(), Object.class)).length;
    boolean _equals_1 = (_length_1 == 1);
    Assertions.assertEquals(Boolean.valueOf(true), Boolean.valueOf(_equals_1));
  }
}
