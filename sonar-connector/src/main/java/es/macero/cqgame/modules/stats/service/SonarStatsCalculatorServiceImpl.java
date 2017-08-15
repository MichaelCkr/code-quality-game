package es.macero.cqgame.modules.stats.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import es.macero.cqgame.modules.badges.domain.SonarBadge;
import es.macero.cqgame.modules.sonarapi.resultbeans.Issue;
import es.macero.cqgame.modules.stats.domain.SonarStats;
import es.macero.cqgame.util.IssueDateFormatter;
import es.macero.cqgame.util.Utils;

@Component
final class SonarStatsCalculatorServiceImpl implements SonarStatsCalculatorService {

	private static final Log log = LogFactory.getLog(SonarStatsCalculatorServiceImpl.class);
	private BadgeCalculatorService badgeCalculatorService;

	private LocalDate legacyDate;
	private Period legacyPeriod;

	@Autowired
	SonarStatsCalculatorServiceImpl(@Value("${legacyDate}") final String legacyDateString,
			@Value("${legacyPeriod}") final String legacyPeriodString, BadgeCalculatorService badgeCalculatorService) {
		this.legacyDate = StringUtils.isEmpty(legacyDateString) ? null : LocalDate.parse(legacyDateString);
		this.legacyPeriod = StringUtils.isEmpty(legacyPeriodString) ? null
				: Period.ofDays(Integer.valueOf(legacyPeriodString));
		this.badgeCalculatorService = badgeCalculatorService;
	}

	@Override
	public SonarStats fromIssueList(final List<Issue> issues) {
		// For the stats we only use those issues created before 'legacy date'
		issues.stream().map(i -> formatDate(i.getCreationDate())).forEach(log::info);
		List<Issue> issuesFilteredByLegacyTime;
		if (legacyDate != null) {
			issuesFilteredByLegacyTime = issues.stream().filter(this::isBeforeLegacyDate).collect(Collectors.toList());
		} else if (legacyPeriod != null) {
			issuesFilteredByLegacyTime = issues.stream().filter(this::isOldEnough).collect(Collectors.toList());
		} else {
			issuesFilteredByLegacyTime = issues;
		}
		int debtSum = (int) issuesFilteredByLegacyTime.stream().map(Issue::getDebt).filter(Objects::nonNull)
				.map(Utils::durationTranslator).map(Duration::parse).mapToLong(Duration::toMinutes).sum();

		Map<String, Long> typeCount = issuesFilteredByLegacyTime.stream()
				.collect(Collectors.groupingBy(Issue::getSeverity, Collectors.counting()));
		int blocker = typeCount.getOrDefault(SonarStats.SeverityType.BLOCKER.toString(), 0L).intValue();
		int critical = typeCount.getOrDefault(SonarStats.SeverityType.CRITICAL.toString(), 0L).intValue();
		int major = typeCount.getOrDefault(SonarStats.SeverityType.MAJOR.toString(), 0L).intValue();
		int minor = typeCount.getOrDefault(SonarStats.SeverityType.MINOR.toString(), 0L).intValue();
		int info = typeCount.getOrDefault(SonarStats.SeverityType.INFO.toString(), 0L).intValue();

		// Badge calculators use all the issues resolved no matter what date they were
		// created
		List<SonarBadge> badges = badgeCalculatorService.awardBadges(issues);

		return new SonarStats(debtSum, blocker, critical, major, minor, info, badges);
	}

	private boolean isBeforeLegacyDate(Issue i) {
		LocalDate date = formatDate(i.getCreationDate());
		return date.isBefore(legacyDate);
	}

	protected boolean isOldEnough(Issue i) {
		LocalDate creationData = formatDate(i.getCreationDate());
		LocalDate fixDate = formatDate((i.getCloseDate()));
		long age = creationData.until(fixDate, ChronoUnit.DAYS);

		return age >= legacyPeriod.getDays();
	}

	private LocalDate formatDate(String date) {
		return IssueDateFormatter.format(date);
	}

}
