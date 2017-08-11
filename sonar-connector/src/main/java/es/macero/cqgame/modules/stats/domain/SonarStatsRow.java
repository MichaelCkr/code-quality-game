package es.macero.cqgame.modules.stats.domain;

import es.macero.cqgame.modules.badges.domain.SonarBadge;
import lombok.Getter;

import java.util.Collection;

@Getter
public final class SonarStatsRow {
    private String userAlias;
    private String userTeam;
    private int totalPoints;
    private int totalPaidDebt;
    private int blocker;
    private int critical;
    private int major;
    private int minor;
    private TechnicalDept dept;
    private int info;
    private Collection<SonarBadge> badges;

    public SonarStatsRow(String userAlias, String userTeam, int totalPoints, int totalPaidDebt, int blocker, int critical, int major, int minor, int info, TechnicalDept dept, Collection<SonarBadge> badges) {
        super();
        this.userAlias = userAlias;
        this.userTeam = userTeam;
        this.totalPoints = totalPoints;
        this.totalPaidDebt = totalPaidDebt;
        this.blocker = blocker;
        this.critical = critical;
        this.major = major;
        this.minor = minor;
        this.info = info;
        this.dept = dept;
        this.badges = badges;
    }

}
