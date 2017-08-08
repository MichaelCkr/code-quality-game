package es.macero.cqgame.modules.stats.service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import es.macero.cqgame.modules.badges.domain.SonarBadge;
import es.macero.cqgame.modules.stats.domain.SonarStats;
import es.macero.cqgame.util.IssueDateFormatter;
import es.macero.cqgame.modules.sonarapi.resultbeans.Issue;
import es.macero.cqgame.util.Utils;

@Component
final class SonarStatsCalculatorServiceImpl implements SonarStatsCalculatorService {

	private static final Log log = LogFactory.getLog(SonarStatsCalculatorServiceImpl.class);
	private BadgeCalculatorService badgeCalculatorService;

	private LocalDate legacyDate;

	@Autowired
	SonarStatsCalculatorServiceImpl(@Value("${legacyDate}") final String legacyDateString,
	                                BadgeCalculatorService badgeCalculatorService) {
		this.legacyDate = LocalDate.parse(legacyDateString);
		this.badgeCalculatorService = badgeCalculatorService;
	}

	@Override
	public SonarStats fromIssueList(final List<Issue> issues) {
		// For the stats we only use those issues created before 'legacy date'
		issues.stream().map(i -> IssueDateFormatter.format(i.getCreationDate())).forEach(log::info);
		List<Issue> issuesFilteredByLegacyDate = issues.stream()
			.filter(i -> IssueDateFormatter.format(i.getCreationDate())
				.isBefore(legacyDate)).collect(Collectors.toList());

		int debtSum = (int) issuesFilteredByLegacyDate.stream()
		    .map(Issue::getDebt)
		        .filter(Objects::nonNull)
		        .map(Utils::durationTranslator)
		        .map(Duration::parse)
		        .mapToLong(Duration::toMinutes)
		        .sum();
//		List<Issue> issuesFilteredByLegacyDate = issues.stream()
//			.filter(i -> IssueDateFormatter.format(i.getCreationDate())
//				.isBefore(legacyDate)).collect(Collectors.toList());
//		List<Issue> issuesFilteredByCovDate = issues.stream()
//			.filter(i -> IssueDateFormatter.format(i.getCreationDate())
//				.isBefore(coverageDate)).collect(Collectors.toList());
		List<Issue> issuesFilteredByLegacyDate = issues;
		List<Issue> issuesFilteredByCovDate = issues;
		int debtSum = (int) issuesFilteredByLegacyDate.stream().map(Issue::getDebt)
			.filter(c -> c != null).map(Utils::durationTranslator)
			.map(Duration::parse).mapToLong(Duration::toMinutes)
			.sum();

		Map<String, Long> typeCount = issuesFilteredByLegacyDate.stream()
			.collect(Collectors.groupingBy(Issue::getSeverity, Collectors.counting()));
		int blocker = typeCount.getOrDefault(SonarStats.SeverityType.BLOCKER.toString(), 0L).intValue();
		int critical = typeCount.getOrDefault(SonarStats.SeverityType.CRITICAL.toString(), 0L).intValue();
		int major = typeCount.getOrDefault(SonarStats.SeverityType.MAJOR.toString(), 0L).intValue();
		int minor = typeCount.getOrDefault(SonarStats.SeverityType.MINOR.toString(), 0L).intValue();
		int info = typeCount.getOrDefault(SonarStats.SeverityType.INFO.toString(), 0L).intValue();

		// Badge calculators use all the issues resolved no matter what date they were created
		List<SonarBadge> badges = badgeCalculatorService.awardBadges(issues);

		return new SonarStats(debtSum, blocker, critical, major, minor, info, badges);
	}

}
