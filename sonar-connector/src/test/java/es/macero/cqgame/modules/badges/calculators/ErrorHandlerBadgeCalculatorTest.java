package es.macero.cqgame.modules.badges.calculators;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.*;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import edu.emory.mathcs.backport.java.util.Arrays;
import es.macero.cqgame.modules.badges.domain.SonarBadge;
import es.macero.cqgame.modules.sonarapi.resultbeans.Issue;

public class ErrorHandlerBadgeCalculatorTest {

	private static final String[] irrelevantTags = new String[] {"pitfall", "convention", "generic", "irrelevant"};
	private static final SonarBadge FAILURE = new SonarBadge("Failure", "", -10);

	private ErrorHandlerBadgeCalculator calculator = new ErrorHandlerBadgeCalculator();

	@Test
	public void testNoFixedIssuesNoBadge() {
		Optional<SonarBadge> result = calculator.badgeFromIssueList(mockIssues(500, 0));
		assertThat(result.isPresent(), is(false));
	}

	@Test
	public void testFixedIssuesBelowThresholdNoBadge() {
		Optional<SonarBadge> result = calculator.badgeFromIssueList(mockIssues(50, 9));
		assertThat(result.isPresent(), is(false));
	}

	@Test
	public void testFixedIssuesFirstThresholdNoBadge() {
		Optional<SonarBadge> result = calculator.badgeFromIssueList(mockIssues(250, 10));
		assertThat(result.isPresent(), is(true));
		assertThat(result.orElse(FAILURE).getName(), is("Error Tamer"));
	}

	@Test
	public void testFixedIssuesBelowSecondThresholdNoBadge() {
		Optional<SonarBadge> result = calculator.badgeFromIssueList(mockIssues(5, 49));
		assertThat(result.isPresent(), is(true));
		assertThat(result.orElse(FAILURE).getName(), is("Error Tamer"));
	}

	@Test
	public void testFixedIssuesSecondThresholdNoBadge() {
		Optional<SonarBadge> result = calculator.badgeFromIssueList(mockIssues(33, 50));
		assertThat(result.isPresent(), is(true));
		assertThat(result.orElse(FAILURE).getName(), is("Error Trainer"));
	}

	@Test
	public void testFixedIssuesBelowThirdThresholdNoBadge() {
		Optional<SonarBadge> result = calculator.badgeFromIssueList(mockIssues(0, 124));
		assertThat(result.isPresent(), is(true));
		assertThat(result.orElse(FAILURE).getName(), is("Error Trainer"));
	}

	@Test
	public void testFixedIssuesThirdThresholdNoBadge() {
		Optional<SonarBadge> result = calculator.badgeFromIssueList(mockIssues(125, 125));
		assertThat(result.isPresent(), is(true));
		assertThat(result.orElse(FAILURE).getName(), is("Error Handler"));
	}

	@Test
	public void testFixedIssuesBelowFourthThresholdNoBadge() {
		Optional<SonarBadge> result = calculator.badgeFromIssueList(mockIssues(1024, 249));
		assertThat(result.isPresent(), is(true));
		assertThat(result.orElse(FAILURE).getName(), is("Error Handler"));
	}

	@Test
	public void testFixedIssuesFourthThresholdNoBadge() {
		Optional<SonarBadge> result = calculator.badgeFromIssueList(mockIssues(24, 250));
		assertThat(result.isPresent(), is(true));
		assertThat(result.orElse(FAILURE).getName(), is("Error Handler"));
	}

	private LinkedList<Issue> mockIssues(int others, int matching) {
			LinkedList<Issue> issues = new LinkedList<>();
			while (issues.size() < others + matching) {
					List<String> tags = mockIrellevantTags(RandomUtils.nextInt(1, 4));
				if (issues.size() >= others) {
					tags.add("error-handling");
					Collections.shuffle(tags);
				}
				issues.add(new Issue().withTags(tags));
			}
			return issues;
	}

	private List<String> mockIrellevantTags(int numberTags) {
		List<String> tags = new ArrayList<>(Arrays.asList(irrelevantTags));
		Collections.shuffle(tags);
		return tags.subList(0, numberTags - 1);
	}

}