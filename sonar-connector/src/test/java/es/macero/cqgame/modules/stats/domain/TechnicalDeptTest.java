package es.macero.cqgame.modules.stats.domain;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.junit.Test;

public class TechnicalDeptTest {

	@Test
	public void testToStringWithDays() {
		TechnicalDept t = new TechnicalDept((int) Duration.ofDays(1).toMinutes());
		assertThat(t.toString(), is("1d 0h 0m"));
	}

	@Test
	public void testToStringWithDaysAndHours() {
		Duration duration = Duration.ofDays(1);
		duration = duration.plus(2, ChronoUnit.HOURS);
		TechnicalDept t = new TechnicalDept((int) duration.toMinutes());
		assertThat(t.toString(), is("1d 2h 0m"));
	}

	@Test
	public void testToStringWithDaysAndMinutes() {
		Duration duration = Duration.ofDays(1);
		duration = duration.plus(2, ChronoUnit.MINUTES);
		TechnicalDept t = new TechnicalDept((int) duration.toMinutes());
		assertThat(t.toString(), is("1d 0h 2m"));
	}

	@Test
	public void testToStringOnlyMinutes() {
		Duration duration = Duration.ofDays(0);
		duration = duration.plus(2, ChronoUnit.MINUTES);
		TechnicalDept t = new TechnicalDept((int) duration.toMinutes());
		assertThat(t.toString(), is("2m"));
	}

	@Test
	public void testToStringWithDaysAndHoursAndMinutes() {
		Duration duration = Duration.ofDays(1);
		duration = duration.plus(2, ChronoUnit.HOURS);
		duration = duration.plus(17, ChronoUnit.MINUTES);
		TechnicalDept t = new TechnicalDept((int) duration.toMinutes());
		assertThat(t.toString(), is("1d 2h 17m"));
	}

	@Test
	public void testToStringWithHours() {
		Duration duration = Duration.ofDays(0);
		duration = duration.plus(2, ChronoUnit.HOURS);
		TechnicalDept t = new TechnicalDept((int) duration.toMinutes());
		assertThat(t.toString(), is("2h 0m"));
	}

	@Test
	public void testToStringWithHoursAndMinutes() {
		Duration duration = Duration.ofDays(0);
		duration = duration.plus(2, ChronoUnit.HOURS);
		duration = duration.plus(17, ChronoUnit.MINUTES);
		TechnicalDept t = new TechnicalDept((int) duration.toMinutes());
		assertThat(t.toString(), is("2h 17m"));
	}

}