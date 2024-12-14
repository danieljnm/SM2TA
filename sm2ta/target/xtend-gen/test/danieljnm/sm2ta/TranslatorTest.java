package danieljnm.sm2ta;

import danieljnm.sm2ta.StateMachine.StateMachine;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("all")
public class TranslatorTest {
  private StateMachine stateMachine;

  @BeforeEach
  public void reset() {
    StateMachine _stateMachine = new StateMachine();
    this.stateMachine = _stateMachine;
  }

  @Test
  public void emptyMachine() {
    this.stateMachine.name("Test");
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("process Test {");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append("system Test;");
    _builder.newLine();
    final String uppaal = _builder.toString();
    Assertions.assertEquals(uppaal, this.stateMachine.toUppaal());
  }
}
