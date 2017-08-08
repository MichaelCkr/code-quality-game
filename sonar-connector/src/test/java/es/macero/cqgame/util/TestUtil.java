package es.macero.cqgame.util;

import java.time.LocalDate;

import es.macero.cqgame.modules.sonarapi.resultbeans.Issue;
import es.macero.cqgame.modules.stats.domain.SonarStats;
import es.macero.cqgame.util.IssueDateFormatter;

public class TestUtil {
	private TestUtil() { /*empty */
	}

	public static Issue createIssue(final LocalDate creationDate, final int debtMinutes, final SonarStats.SeverityType severityType) {
		final Issue issue = new Issue();
		issue.setCreationDate(IssueDateFormatter.toIssueDate(creationDate));
		issue.setDebt(debtMinutes + "m");
		issue.setSeverity(severityType.toString());
		return issue;
	}
}