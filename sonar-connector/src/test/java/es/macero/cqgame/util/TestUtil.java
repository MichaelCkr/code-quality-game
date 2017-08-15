package es.macero.cqgame.util;

import java.time.LocalDate;
import java.time.Period;

import es.macero.cqgame.modules.sonarapi.resultbeans.Issue;
import es.macero.cqgame.modules.stats.domain.SonarStats;

public class TestUtil {
	private TestUtil() { /*empty */
	}

	public static Issue createIssue(final LocalDate creationDate, LocalDate closeDate, final int debtMinutes, final SonarStats.SeverityType severityType) {
		final Issue issue = new Issue();
		issue.setCreationDate(IssueDateFormatter.toIssueDate(creationDate));
		issue.setDebt(debtMinutes + "m");
		issue.setSeverity(severityType.toString());
		if (closeDate != null) {
			issue.setCloseDate(IssueDateFormatter.toIssueDate(closeDate));
		}
		return issue;
	}

	public static LocalDate getDateXDaysAgo(int daysAgo) {
		Period days = Period.ofDays(daysAgo);
		return LocalDate.now().minus(days);
	}
}