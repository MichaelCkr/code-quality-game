package es.macero.cqgame.modules.stats.domain;

import java.time.Duration;

public class TechnicalDept {

	private long days;
	private long hours;
	private long minutes;

	@SuppressWarnings("unused")
	private TechnicalDept() {
		// not relevant
	}

	public TechnicalDept(int dept) {
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

}
