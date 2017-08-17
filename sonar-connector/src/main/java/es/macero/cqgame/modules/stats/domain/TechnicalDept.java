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
		return DurationFormatUtils.formatDuration(Duration.ofMinutes(dept).toMillis(), "d'd' H'h' m'm'", false);
	}

	public TechnicalDept plus(TechnicalDept technicalDept) {
		return new TechnicalDept(dept + technicalDept.getDept());
	}

	public int getDept() {
		return dept;
	}

}
