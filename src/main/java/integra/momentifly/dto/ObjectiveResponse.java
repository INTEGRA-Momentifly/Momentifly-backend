package integra.momentifly.dto;

import integra.momentifly.model.MacroType;
import integra.momentifly.model.Objective;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public record ObjectiveResponse(
        Integer calories,
        Integer protein,
        Integer carbohydrates,
        Integer fats) {

    public static ObjectiveResponse from(List<Objective> objectives) {
        Map<MacroType, Integer> valueByType = new EnumMap<>(MacroType.class);
        objectives.forEach(objective -> valueByType.put(objective.getType(), objective.getValue()));
        return new ObjectiveResponse(
                valueByType.get(MacroType.CALORIES),
                valueByType.get(MacroType.PROTEIN),
                valueByType.get(MacroType.CARBOHYDRATES),
                valueByType.get(MacroType.FATS));
    }
}
