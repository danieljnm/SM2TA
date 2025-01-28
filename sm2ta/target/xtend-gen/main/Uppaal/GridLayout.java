package Uppaal;

import java.util.Objects;
import java.util.function.Consumer;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

@SuppressWarnings("all")
public class GridLayout {
  private int startX = 0;

  private int startY = 0;

  private int horizontalSpacing = 400;

  private int verticalSpacing = 200;

  private int maxPerRow = 4;

  public void applyLayout(final Template template) {
    final Procedure2<Location, Integer> _function = (Location location, Integer index) -> {
      final int row = ((index).intValue() / this.maxPerRow);
      final int col = ((index).intValue() % this.maxPerRow);
      location.x = (this.startX + (col * this.horizontalSpacing));
      location.y = (this.startY + (row * this.verticalSpacing));
      if ((location.name != null)) {
        location.name.x = (location.x - 15);
        location.name.y = (location.y + 15);
      }
      if ((location.label != null)) {
        location.label.x = (location.x - 15);
        location.label.y = (location.y + 30);
      }
    };
    IterableExtensions.<Location>forEach(template.locations, _function);
    this.applyTransitions(template);
  }

  public void applyTransitions(final Template template) {
    final Consumer<Transition> _function = (Transition transition) -> {
      final Function1<Location, Boolean> _function_1 = (Location it) -> {
        return Boolean.valueOf(Objects.equals(it.id, transition.source));
      };
      final Location source = IterableExtensions.<Location>findFirst(template.locations, _function_1);
      final Function1<Location, Boolean> _function_2 = (Location it) -> {
        return Boolean.valueOf(Objects.equals(it.id, transition.target));
      };
      final Location target = IterableExtensions.<Location>findFirst(template.locations, _function_2);
      if (((source == null) || (target == null))) {
        return;
      }
      final int midX = ((source.x + target.x) / 2);
      final int midY = ((source.y + target.y) / 2);
      final Procedure2<Label, Integer> _function_3 = (Label label, Integer index) -> {
        label.x = (midX + ((index).intValue() * 15));
        label.y = (midY + ((index).intValue() * 15));
      };
      IterableExtensions.<Label>forEach(transition.labels, _function_3);
    };
    template.transitions.forEach(_function);
  }
}
