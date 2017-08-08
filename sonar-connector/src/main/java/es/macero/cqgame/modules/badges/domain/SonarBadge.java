package es.macero.cqgame.modules.badges.domain;

import lombok.Data;

@Data
public class SonarBadge {
    private String name;
    private String description;
    private int extraPoints;

    public SonarBadge(String name, String description, int extraPoints) {
        this.name = name;
        this.description = description;
        this.extraPoints = extraPoints;
    }
}
