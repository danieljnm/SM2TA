package Uppaal;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

@SuppressWarnings("all")
public class GridLayout {
  private int horizontalSpacing = 400;

  private int verticalSpacing = 150;

  private int labelOffset = 50;

  private int spacing = 15;

  private int maxPerRow = 4;

  public void applyLayout(final Template template) {
    final Procedure2<Location, Integer> _function = (Location location, Integer index) -> {
      final int row = ((index).intValue() / this.maxPerRow);
      final int col = ((index).intValue() % this.maxPerRow);
      location.x = (col * this.horizontalSpacing);
      location.y = (row * this.verticalSpacing);
      if ((location.name != null)) {
        location.name.x = (location.x - this.spacing);
        location.name.y = (location.y + this.spacing);
      }
      if ((location.label != null)) {
        location.label.x = (location.x - this.spacing);
        location.label.y = (location.y + (this.spacing * 2));
      }
    };
    IterableExtensions.<Location>forEach(template.locations, _function);
    this.applyTransitions(template);
  }

  public void applyTransitions(final Template template) {
    final Function1<Transition, String> _function = (Transition it) -> {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(it.source);
      _builder.append("-->");
      _builder.append(it.target);
      return _builder.toString();
    };
    final Map<String, List<Transition>> transitionGroups = IterableExtensions.<String, Transition>groupBy(template.transitions, _function);
    final BiConsumer<String, List<Transition>> _function_1 = (String key, List<Transition> transitions) -> {
      final Function1<Location, Boolean> _function_2 = (Location it) -> {
        return Boolean.valueOf(Objects.equals(it.id, IterableExtensions.<Transition>head(transitions).source));
      };
      final Location source = IterableExtensions.<Location>findFirst(template.locations, _function_2);
      final Function1<Location, Boolean> _function_3 = (Location it) -> {
        return Boolean.valueOf(Objects.equals(it.id, IterableExtensions.<Transition>head(transitions).target));
      };
      final Location target = IterableExtensions.<Location>findFirst(template.locations, _function_3);
      if (((source == null) || (target == null))) {
        return;
      }
      final int midX = ((source.x + target.x) / 2);
      final int midY = ((source.y + target.y) / 2);
      final Procedure2<Transition, Integer> _function_4 = (Transition transition, Integer index) -> {
        int _size = IterableExtensions.<Transition>head(transitions).labels.size();
        int _multiply = (_size * this.spacing);
        final int offset = ((index).intValue() * _multiply);
        final Procedure2<Label, Integer> _function_5 = (Label label, Integer labelIndex) -> {
          int _xifexpression = (int) 0;
          if ((source.id == target.id)) {
            _xifexpression = 15;
          } else {
            _xifexpression = this.labelOffset;
          }
          int _minus = (midX - _xifexpression);
          label.x = _minus;
          int _xifexpression_1 = (int) 0;
          if ((source.id == target.id)) {
            _xifexpression_1 = 70;
          } else {
            _xifexpression_1 = 0;
          }
          int _minus_1 = ((((midY + offset) + ((labelIndex).intValue() * this.spacing)) + this.spacing) - _xifexpression_1);
          label.y = _minus_1;
        };
        IterableExtensions.<Label>forEach(transition.labels, _function_5);
      };
      IterableExtensions.<Transition>forEach(transitions, _function_4);
    };
    transitionGroups.forEach(_function_1);
  }
}
