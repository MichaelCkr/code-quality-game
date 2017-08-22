package es.macero.cqgame.modules.stats.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import es.macero.cqgame.modules.badges.domain.SonarBadge;
import es.macero.cqgame.modules.users.domain.SonarUser;

public final class SonarStatsRow {
	private SonarUser user;
	private SonarStats stats;

	public SonarStatsRow combine(final SonarStatsRow r2) {
	    Set<SonarBadge> allBadges = new HashSet<>();
	    allBadges.addAll(getBadges());
	    allBadges.addAll(r2.getBadges());
		return new SonarStatsRowBuilder(user.getAlias(), user.getTeam())
				.withTotalPaidDebt(getTotalPaidDebt() + r2.getTotalPaidDebt())
				.withBlocker(getBlocker() + r2.getBlocker())
				.withCritical(getCritical() + r2.getCritical())
				.withMajor(getMajor() + r2.getMajor())
				.withMinor(getMinor() + r2.getMinor())
				.withInfo(getInfo() + r2.getInfo())
				.withDept(getDept().plus(r2.getDept()))
				.withBadges(allBadges)
				.createSonarStatsRow();
	}

	public String getUserAlias() {
		return user.getAlias();
	}

	public String getUserTeam() {
		return user.getTeam();
	}

	public int getTotalPoints() {
		return stats.getTotalPoints();
	}

	public int getTotalPaidDebt() {
		return stats.getTotalPaidDebt();
	}

	public int getBlocker() {
		return stats.getBlocker();
	}

	public int getCritical() {
		return stats.getCritical();
	}

	public int getMajor() {
		return stats.getMajor();
	}

	public int getMinor() {
		return stats.getMinor();
	}

	public TechnicalDept getDept() {
		return stats.getDept();
	}

	public int getInfo() {
		return stats.getInfo();
	}

	public Collection<SonarBadge> getBadges() {
		return stats.getBadges();
	}

    public SonarStatsRow(SonarUser user, SonarStats stats) {
		this.user = user;
		this.stats = stats;
    }
}
