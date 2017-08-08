package es.macero.cqgame.modules.badges.calculators;

import static es.macero.cqgame.util.TestUtil.createIssue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import org.junit.Test;

import es.macero.cqgame.modules.badges.domain.SonarBadge;
import es.macero.cqgame.modules.sonarapi.resultbeans.Issue;
import es.macero.cqgame.modules.stats.domain.SonarStats;

public class UnitTesterBadgeCalculatorTest {
	private static final String COVERAGE_DATE_STRING = "2016-06-01";

	private UnitTesterBadgeCalculator calculator = new UnitTesterBadgeCalculator(COVERAGE_DATE_STRING);

	@Test
	public void issuesAfterCoverageDateAreNotProcessedForBadges() {
		final Issue issue = createIssue(LocalDate.of(2016, 7, 1), 30, SonarStats.SeverityType.BLOCKER);
		issue.setRule("common-java:InsufficientLineCoverage");
		Optional<SonarBadge> badge = calculator.badgeFromIssueList(Collections.singletonList(issue));
		// List should be empty, issue does not pass the date filter.
		assertFalse(badge.isPresent());
	}

	@Test
	public void issuesBeforeCoverageDateAreProcessedForBadges() {
		final Issue issue = createIssue(LocalDate.of(2016, 5, 20), 30, SonarStats.SeverityType.BLOCKER);
		issue.setRule("common-java:InsufficientLineCoverage");
		Optional<SonarBadge> badge = calculator.badgeFromIssueList(Collections.singletonList(issue));
		// List should be empty, issue does not pass the date filter.
		assertTrue(badge.isPresent());
	}



}