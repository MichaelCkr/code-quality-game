package es.macero.cqgame.modules.stats.domain;

import java.time.Duration;

public class TechnicalDept {

	private int dept;
	private long days;
	private long hours;
	private long minutes;

	@SuppressWarnings("unused")
	private TechnicalDept() {
		// not relevant
	}

	public TechnicalDept(int dept) {
		this.dept = dept;
		Duration duration = Duration.ofMinutes(dept);
		days = duration.toDays();
		duration = duration.minusDays(days);
		hours = duration.toHours();
		duration = duration.minusHours(hours);
		minutes = duration.toMinutes();
	}

	@Override
	public String toString() {
		String reslutDays = days != 0 ? days + "d " : "";
		String resultHours = hours != 0 ? hours + "h " : "";
		return reslutDays + resultHours + minutes + "m";
	}

	public TechnicalDept plus(TechnicalDept technicalDept) {
		return new TechnicalDept(dept + technicalDept.getDept());
	}

	public int getDept() {
		return dept;
	}

}
