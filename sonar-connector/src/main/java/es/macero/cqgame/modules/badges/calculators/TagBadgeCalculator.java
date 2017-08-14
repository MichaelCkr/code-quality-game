package es.macero.cqgame.modules.badges.calculators;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import es.macero.cqgame.modules.badges.domain.SonarBadge;
import es.macero.cqgame.modules.badges.domain.TagBadge;
import es.macero.cqgame.modules.sonarapi.resultbeans.Issue;

public abstract class TagBadgeCalculator implements BadgeCalculator {

	abstract String getTagName();

	public abstract List<TagBadge> getBadges();

	@Override
	public Optional<SonarBadge> badgeFromIssueList(List<Issue> issues) {
		final long count = countFilteredIssues(issues, i -> i.getTags().contains(getTagName()));

		return getBadges().stream()
		                  .sorted(Comparator.reverseOrder())
		                  .filter(b -> b.isApplicable(count))
		                  .map(TagBadge::getBadge)
		                  .findFirst();
	}

}
