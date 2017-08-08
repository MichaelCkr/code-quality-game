package es.macero.cqgame.modules.stats.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.macero.cqgame.modules.badges.calculators.BadgeCalculator;
import es.macero.cqgame.modules.badges.domain.SonarBadge;
import es.macero.cqgame.modules.sonarapi.resultbeans.Issue;

@Service
public class BadgeCalculatorServiceImpl implements BadgeCalculatorService {

	List<BadgeCalculator> badgeCalculators;

	@Autowired
	public BadgeCalculatorServiceImpl(List<BadgeCalculator> badgeCalculators) {
		this.badgeCalculators = badgeCalculators;
	}



	public List<SonarBadge> awardBadges(List<Issue> issues) {
		return badgeCalculators.stream().map(c -> c.badgeFromIssueList(issues))
		                                      .filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
	}
}