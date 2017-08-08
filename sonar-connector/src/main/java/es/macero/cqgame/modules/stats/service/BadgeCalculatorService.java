package es.macero.cqgame.modules.stats.service;

import java.util.List;

import es.macero.cqgame.modules.badges.domain.SonarBadge;
import es.macero.cqgame.modules.sonarapi.resultbeans.Issue;

public interface BadgeCalculatorService {

	List<SonarBadge> awardBadges(List<Issue> issues);
}
