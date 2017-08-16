package es.macero.cqgame.modules.stats.domain;

import java.util.ArrayList;
import java.util.Collection;

import es.macero.cqgame.modules.badges.domain.SonarBadge;
import es.macero.cqgame.modules.users.domain.SonarUser;

public final class SonarStatsRowBuilder {

	private final SonarUser user;
	private final SonarStats stats = new SonarStats();

    public SonarStatsRowBuilder(String userAlias, String userTeam) {
        this.user= new SonarUser("", userAlias, userTeam);
    }

    public SonarStatsRowBuilder withTotalPaidDebt(int totalPaidDebt) {
        this.stats.setTotalPaidDebt(totalPaidDebt);
        return this;
    }

    public SonarStatsRowBuilder withBlocker(int blocker) {
        this.stats.setBlocker(blocker);
        return this;
    }

    public SonarStatsRowBuilder withCritical(int critical) {
        this.stats.setCritical(critical);
        return this;
    }

    public SonarStatsRowBuilder withMajor(int major) {
        this.stats.setMajor(major);
        return this;
    }

    public SonarStatsRowBuilder withMinor(int minor) {
        this.stats.setMinor(minor);
        return this;
    }

    public SonarStatsRowBuilder withInfo(int info) {
        this.stats.setInfo(info);
        return this;
    }

    public SonarStatsRowBuilder withDept(TechnicalDept dept) {
    	this.stats.setDept(dept);
    	return this;
    }

    public SonarStatsRowBuilder withBadges(Collection<SonarBadge> badges) {
        this.stats.setBadges(new ArrayList<>(badges));
        return this;
    }

    public SonarStatsRow createSonarStatsRow() {
        return new SonarStatsRow(user, stats);
    }
}