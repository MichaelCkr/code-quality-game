package es.macero.cqgame.modules.stats.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.when;

import java.security.acl.LastOwnerException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import es.macero.cqgame.modules.sonarapi.resultbeans.Issue;
import es.macero.cqgame.modules.stats.domain.SonarStats;
import es.macero.cqgame.util.TestUtil;

public class SonarStatsCalculatorServiceImplTest {

    private static final String LEGACY_DATE_STRING = "2016-05-01";
    private static final String LEGACY_DATE_PERIOD = "30";

	private SonarStatsCalculatorService service;


    @Mock
    private BadgeCalculatorService badgeCalculatorService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        service = new SonarStatsCalculatorServiceImpl(LEGACY_DATE_STRING, LEGACY_DATE_PERIOD, badgeCalculatorService);
        when(badgeCalculatorService.awardBadges(anyListOf(Issue.class))).thenReturn(Collections.emptyList());
    }

    @Test
    public void emptyListShouldWorkAndReturnZeroPoints() {
        final SonarStats stats = service.fromIssueList(Collections.emptyList());
        assertEquals(0, stats.getTotalPoints());
    }

    @Test
    public void issuesAfterLegacyDateAreNotProcessed() {
        final Issue issue = TestUtil.createIssue(LocalDate.of(2016, 5, 1), null, 30, SonarStats.SeverityType.BLOCKER);
        final SonarStats stats = service.fromIssueList(Collections.singletonList(issue));
        assertEquals(0, stats.getTotalPoints());
    }

    @Test
    public void issuesAtLegacyDateAreNotProcessed() {
        final Issue issue = TestUtil.createIssue(LocalDate.of(2016, 5, 20), null, 30, SonarStats.SeverityType.BLOCKER);
        final SonarStats stats = service.fromIssueList(Collections.singletonList(issue));
        assertEquals(0, stats.getTotalPoints());
    }

    @Test
    public void issuesBeforeLegacyDateAreProcessed() {
        final Issue issue = TestUtil.createIssue(LocalDate.of(2016, 4, 20), null, 30, SonarStats.SeverityType.BLOCKER);
        final SonarStats stats = service.fromIssueList(Collections.singletonList(issue));
        assertNotEquals(0, stats.getTotalPoints());
    }

    @Test
    public void checkStatsNumbers() {
	    final LocalDate date = LocalDate.of(2016, 1, 1);
	    final LocalDate closeDate = LocalDate.of(2016, 2, 1);
        final Issue i1 = TestUtil.createIssue(date, closeDate, 5, SonarStats.SeverityType.MINOR);
        final Issue i2 = TestUtil.createIssue(date, closeDate, 10, SonarStats.SeverityType.BLOCKER);
        final Issue i3 = TestUtil.createIssue(date, closeDate, 15, SonarStats.SeverityType.CRITICAL);
        final Issue i4 = TestUtil.createIssue(date, closeDate, 20, SonarStats.SeverityType.INFO);
        final Issue i5 = TestUtil.createIssue(date, closeDate, 25, SonarStats.SeverityType.MAJOR);

        final SonarStats sonarStats = service.fromIssueList(Stream.of(i1, i2, i3, i4, i5).collect(Collectors.toList()));
        assertEquals(1, sonarStats.getMinor());
        assertEquals(1, sonarStats.getBlocker());
        assertEquals(1, sonarStats.getCritical());
        assertEquals(1, sonarStats.getInfo());
        assertEquals(1, sonarStats.getMajor());
        assertEquals(75, sonarStats.getTotalPaidDebt());
        assertEquals(0, sonarStats.getBadges().size());
        assertNotEquals(0, sonarStats.getTotalPoints());
    }

    @Test
    public void issuesNotWithinLegacyPeriod() {
	    SonarStatsCalculatorServiceImpl service = new SonarStatsCalculatorServiceImpl(StringUtils.EMPTY, LEGACY_DATE_PERIOD, badgeCalculatorService);
	    LocalDate issueDate = TestUtil.getDateXDaysAgo(10);
	    LocalDate closeDate = TestUtil.getDateXDaysAgo(9);
		final Issue issue = TestUtil.createIssue(issueDate, closeDate, 30, SonarStats.SeverityType.BLOCKER);
    	final SonarStats stats = service.fromIssueList(Collections.singletonList(issue));
    	assertEquals(0, stats.getTotalPoints());
    }

	@Test
	public void issuesWithinLegacyPeriod() {
		SonarStatsCalculatorServiceImpl service = new SonarStatsCalculatorServiceImpl(StringUtils.EMPTY, LEGACY_DATE_PERIOD, badgeCalculatorService);
		LocalDate issueDate = LocalDate.of(2016, 4, 20);
		LocalDate closeDate = LocalDate.of(2016, 7, 20);
		final Issue issue = TestUtil.createIssue(issueDate, closeDate, 30, SonarStats.SeverityType.BLOCKER);
		final SonarStats stats = service.fromIssueList(Collections.singletonList(issue));
		assertNotEquals(0, stats.getTotalPoints());
	}

	@Test
	public void testOldIssuesFixedToSoon() {
		SonarStatsCalculatorServiceImpl service = new SonarStatsCalculatorServiceImpl(StringUtils.EMPTY, LEGACY_DATE_PERIOD, badgeCalculatorService);
		LocalDate issueDate = LocalDate.of(2016, 4, 20);
		LocalDate closeDate = LocalDate.of(2016, 4, 21);
		final Issue issue = TestUtil.createIssue(issueDate, closeDate, 30, SonarStats.SeverityType.BLOCKER);
		final SonarStats stats = service.fromIssueList(Collections.singletonList(issue));
		assertThat(stats.getTotalPoints(), is(0));
	}

	@Test
    public void issuesWithoutLegacyDateAndPeriod() {
    	SonarStatsCalculatorServiceImpl service = new SonarStatsCalculatorServiceImpl(StringUtils.EMPTY, StringUtils.EMPTY, badgeCalculatorService);
    	LocalDate issueDate = TestUtil.getDateXDaysAgo(1);
    	LocalDate closeDate = LocalDate.now();
    	final Issue issue = TestUtil.createIssue(issueDate, closeDate, 30, SonarStats.SeverityType.BLOCKER);
    	final SonarStats stats = service.fromIssueList(Collections.singletonList(issue));
    	assertNotEquals(0, stats.getTotalPoints());
    }

    
}