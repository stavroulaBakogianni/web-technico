package gr.europeandynamics.web.technico.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RepairType {
    PAINTING("Painting"),
    INSULATION("Insulation"),
    FRAMES("Frames"),
    PLUMBING("Plumbing"),
    ELECTRICALWORK("Electrical work");

    private final String code;
}
