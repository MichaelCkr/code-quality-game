package es.macero.cqgame.domain.badges;

import java.util.List;
import java.util.Optional;

import es.macero.cqgame.resultbeans.Issue;

public interface BadgeCalculator
{

    public Optional<SonarBadge> badgeFromIssueList(List<Issue> issues);
    
}
