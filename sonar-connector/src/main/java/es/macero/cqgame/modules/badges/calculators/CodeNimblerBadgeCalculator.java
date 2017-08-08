package es.macero.cqgame.modules.badges.calculators;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import es.macero.cqgame.modules.badges.domain.SonarBadge;
import es.macero.cqgame.modules.sonarapi.resultbeans.Issue;

@Component
public class CodeNimblerBadgeCalculator implements BadgeCalculator {

    private static final int EXTRA_POINTS_PAPER = 50;
    private static final int EXTRA_POINTS_BRONZE = 125;
    private static final int EXTRA_POINTS_SILVER = 300;
    private static final int EXTRA_POINTS_GOLDEN = 500;

    private static final String TAG_NAME = "clumsy";

    @Override
    public Optional<SonarBadge> badgeFromIssueList(List<Issue> issues) {
        long count = countFilteredIssues(issues, i -> i.getTags().contains(TAG_NAME));
        if (count >= 250) {
            return Optional.of(new SonarBadge("Graceful Tumbling Expert", "Fixing 500 or more issues with clumsy code.", EXTRA_POINTS_GOLDEN));
        } else if (count >= 125) {
            return Optional.of(new SonarBadge("Dexterous Coder", "Fixing 125 or more issues with clumsy code.", EXTRA_POINTS_SILVER));
        } else if (count >= 50) {
            return Optional.of(new SonarBadge("Agile Coder", "Fixing 50 or more issues with clumsy code.", EXTRA_POINTS_BRONZE));
        } else if (count >= 10) {
            return Optional.of(new SonarBadge("Coordinated Coder", "Fixing 10 or more issues with clumsy code.", EXTRA_POINTS_PAPER));
        } else {
            return Optional.empty();
        }
    }
}
