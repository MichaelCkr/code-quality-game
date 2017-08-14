package es.macero.cqgame.modules.badges.calculators;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import es.macero.cqgame.modules.badges.domain.SonarBadge;
import es.macero.cqgame.modules.badges.domain.TagBadge;
import es.macero.cqgame.modules.sonarapi.resultbeans.Issue;

@Component
public class ErrorHandlerBadgeCalculator implements BadgeCalculator {

	private static final List<TagBadge> badges = Arrays.asList(
			new TagBadge(500, () -> new SonarBadge("Error Master", "Fixing 500 or more issues with error-handling code", 500)),
			new TagBadge(125, () -> new SonarBadge("Error Handler", "Fixing 125 or more issues with error-handling code", 300)),
			new TagBadge(50, () -> new SonarBadge("Error Trainer", "Fixing 50 or more issues with error-handling code", 125)),
			new TagBadge(10, () -> new SonarBadge("Error Tamer", "Fixing 10 or more issues with error-handling code", 50)));


    private static final String TAG_NAME = "error-handling";

    @Override
    public Optional<SonarBadge> badgeFromIssueList(List<Issue> issues) {
        final long count = countFilteredIssues(issues, i -> i.getTags().contains(TAG_NAME));

	    return badges.stream()
                 .sorted()
                 .filter(b -> b.isApplicable(count))
                 .map(TagBadge::getBadge)
                 .findFirst();
}
}
