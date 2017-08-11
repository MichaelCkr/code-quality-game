package es.macero.cqgame.modules.stats.domain;

import java.util.List;

import es.macero.cqgame.modules.badges.domain.SonarBadge;
import lombok.ToString;

@ToString
public class SonarStats {
	public enum SeverityType {
		BLOCKER, CRITICAL, MAJOR, MINOR, INFO
	}

	private int totalPaidDebt;
	private int blocker;
	private int critical;
	private int major;
	private int minor;
	private int info;
	private TechnicalDept dept;
	private int totalPoints;

	private List<SonarBadge> badges;

	public SonarStats(final int totalPaidDebt, final int blocker, final int critical, final int major, final int minor,
			final int info, final List<SonarBadge> badges) {
		super();
		this.totalPaidDebt = totalPaidDebt;
		this.blocker = blocker;
		this.critical = critical;
		this.major = major;
		this.minor = minor;
		this.info = info;
		this.badges = badges;
		calculatePoints();
		calculateDept();
	}

	private void calculateDept() {
		dept = new TechnicalDept(totalPaidDebt);
	}

	private void calculatePoints() {
		totalPoints = blocker * 8 + critical * 5 + major * 3 + minor * 2 + info * 1
				+ badges.stream().mapToInt(SonarBadge::getExtraPoints).sum();
	}

	public int getTotalPaidDebt() {
		return totalPaidDebt;
	}

	public int getBlocker() {
		return blocker;
	}

	public int getCritical() {
		return critical;
	}

	public int getMajor() {
		return major;
	}

	public int getMinor() {
		return minor;
	}

	public int getInfo() {
		return info;
	}

	public TechnicalDept getDept() {
		return dept;
	}

	public int getTotalPoints() {
		return totalPoints;
	}

	public List<SonarBadge> getBadges() {
		return badges;
	}

}
