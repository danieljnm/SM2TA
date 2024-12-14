package danieljnm.sm2ta.StateMachine;

import java.util.HashMap;
import java.util.function.Function;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class StateMachine {
  public String name;

  public HashMap<String, State> states = CollectionLiterals.<String, State>newHashMap();

  public String name(final String name) {
    return this.name = name;
  }

  public State getInitialState() {
    final Function1<State, Boolean> _function = (State it) -> {
      return Boolean.valueOf(it.isInitial);
    };
    return IterableExtensions.<State>findFirst(this.states.values(), _function);
  }

  public State state(final String name) {
    final Function<String, State> _function = (String it) -> {
      return new State(this, name);
    };
    return this.states.computeIfAbsent(name, _function);
  }

  public String toUppaal() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("process ");
    _builder.append(this.name);
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    _builder.append("system ");
    _builder.append(this.name);
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
}
