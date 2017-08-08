package es.macero.cqgame.modules.badges.calculators;

import es.macero.cqgame.modules.badges.domain.SonarBadge;
import es.macero.cqgame.modules.sonarapi.resultbeans.Issue;
import es.macero.cqgame.util.IssueDateFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UnitTesterBadgeCalculator implements BadgeCalculator {
    private static final int EXTRA_POINTS_PAPER = 100;
    private static final int EXTRA_POINTS_BRONZE = 250;
    private static final int EXTRA_POINTS_SILVER = 650;
    private static final int EXTRA_POINTS_GOLDEN = 1000;

    private static final String RULE_ID_LINE = "common-java:InsufficientLineCoverage";
    private static final String RULE_ID_BRANCH = "common-java:InsufficientBranchCoverage";

	private LocalDate coverageDate;

	@Autowired
	public UnitTesterBadgeCalculator(@Value("${coverageDate}") final String coverageDateString) {
		this.coverageDate = LocalDate.parse(coverageDateString);
	}


	@Override
    public Optional<SonarBadge> badgeFromIssueList(List<Issue> issues) {
		List<Issue> filtered = issues.stream().filter(i -> IssueDateFormatter.format(i.getCreationDate())
		                                              .isBefore(coverageDate)).collect(Collectors.toList());
        long count = countFilteredIssues(filtered, i -> RULE_ID_LINE.equalsIgnoreCase(i.getRule()) || RULE_ID_BRANCH.equalsIgnoreCase(i.getRule()));
        if (count >= 50) {
            return Optional.of(new SonarBadge("Golden Cover", "Fixing UT coverage for 50 legacy classes.", EXTRA_POINTS_GOLDEN));
        } else if (count >= 25) {
            return Optional.of(new SonarBadge("Silver Cover", "Fixing UT coverage for 25 legacy classes.", EXTRA_POINTS_SILVER));
        } else if (count >= 10) {
            return Optional.of(new SonarBadge("Bronze Cover", "Fixing UT coverage for 10 legacy classes.", EXTRA_POINTS_BRONZE));
        } else if (count >= 1) {
            return Optional.of(new SonarBadge("Paper Cover", "Fixing UT coverage for 1 legacy class.", EXTRA_POINTS_PAPER));
        } else {
            return Optional.empty();
        }
    }

}
