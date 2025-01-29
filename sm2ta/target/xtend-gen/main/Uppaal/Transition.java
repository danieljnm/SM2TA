package Uppaal;

import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class Transition {
  public String source;

  public String target;

  public List<Label> labels = CollectionLiterals.<Label>newArrayList();

  public Transition(final String source, final String target) {
    this.source = source;
    this.target = target;
  }

  public boolean labels(final danieljnm.sm2ta.StateMachine.Transition transition) {
    boolean _xblockexpression = false;
    {
      boolean _hasGuard = transition.hasGuard();
      if (_hasGuard) {
        String _replace = transition.guard.replace("false", "0").replace("true", "1");
        Label _label = new Label("guard", _replace);
        this.labels.add(_label);
      }
      boolean _hasTimeout = transition.hasTimeout();
      if (_hasTimeout) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("gen_clock >= ");
        _builder.append(transition.timeout);
        Label _label_1 = new Label("guard", _builder.toString());
        this.labels.add(_label_1);
      }
      boolean _hasSignal = transition.hasSignal();
      if (_hasSignal) {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append(transition.signal);
        _builder_1.append("!");
        Label _label_2 = new Label("synchronisation", _builder_1.toString());
        this.labels.add(_label_2);
      }
      boolean _hasWhen = transition.hasWhen();
      if (_hasWhen) {
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append(transition.when);
        _builder_2.append("?");
        Label _label_3 = new Label("synchronisation", _builder_2.toString());
        this.labels.add(_label_3);
      }
      boolean _xifexpression = false;
      boolean _hasAssignment = transition.hasAssignment();
      if (_hasAssignment) {
        String _join = IterableExtensions.join(transition.assignments(), ", ");
        Label _label_4 = new Label("assignment", _join);
        _xifexpression = this.labels.add(_label_4);
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }

  @Override
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<transition>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<source ref=\"");
    _builder.append(this.source, "\t");
    _builder.append("\"/>");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("<target ref=\"");
    _builder.append(this.target, "\t");
    _builder.append("\"/>");
    _builder.newLineIfNotEmpty();
    {
      for(final Label label : this.labels) {
        _builder.append("\t");
        _builder.append(label, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("</transition>");
    _builder.newLine();
    return _builder.toString();
  }
}
