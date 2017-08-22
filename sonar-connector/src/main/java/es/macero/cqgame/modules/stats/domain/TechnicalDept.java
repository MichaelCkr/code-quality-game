package es.macero.cqgame.modules.stats.domain;

import java.time.Duration;

import org.apache.commons.lang3.time.DurationFormatUtils;

public class TechnicalDept {

	private int dept;

	@SuppressWarnings("unused")
	private TechnicalDept() {
		// not relevant
	}

	public TechnicalDept(int minutes) {
		this.dept = minutes;
	}

	@Override
	public String toString() {
		Duration duration = Duration.ofMinutes(dept);
		String formatter = "m'm'";
		if (duration.toHours() > 0) {
			formatter = "H'h' " + formatter;
		}
		if (duration.toDays() > 0) {
			formatter = "d'd' " + formatter;
		}
		return DurationFormatUtils.formatDuration(duration.toMillis(), formatter, false);
	}

	public TechnicalDept plus(TechnicalDept technicalDept) {
		return new TechnicalDept(dept + technicalDept.getDept());
	}

	public int getDept() {
		return dept;
	}

}
