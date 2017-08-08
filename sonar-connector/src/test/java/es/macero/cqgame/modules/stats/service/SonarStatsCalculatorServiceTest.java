package es.macero.cqgame.modules.stats.service;

import es.macero.cqgame.modules.sonarapi.resultbeans.Issue;
import es.macero.cqgame.modules.stats.domain.SonarStats;
import es.macero.cqgame.util.TestUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;

public class SonarStatsCalculatorServiceTest {

    private static final String LEGACY_DATE_STRING = "2016-05-01";

	private SonarStatsCalculatorService service;


    @Mock
    private BadgeCalculatorService badgeCalculatorService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        service = new SonarStatsCalculatorServiceImpl(LEGACY_DATE_STRING, badgeCalculatorService);
        when(badgeCalculatorService.awardBadges(anyListOf(Issue.class))).thenReturn(Collections.emptyList());
    }

    @Test
    public void emptyListShouldWorkAndReturnZeroPoints() {
        final SonarStats stats = service.fromIssueList(Collections.emptyList());
        assertEquals(0, stats.getTotalPoints());
    }

    @Test
    public void issuesAfterLegacyDateAreNotProcessed() {
        final Issue issue = TestUtil.createIssue(LocalDate.of(2016, 5, 1), 30, SonarStats.SeverityType.BLOCKER);
        final SonarStats stats = service.fromIssueList(Collections.singletonList(issue));
        assertEquals(0, stats.getTotalPoints());
    }

    @Test
    public void issuesAtLegacyDateAreNotProcessed() {
        final Issue issue = TestUtil.createIssue(LocalDate.of(2016, 5, 20), 30, SonarStats.SeverityType.BLOCKER);
        final SonarStats stats = service.fromIssueList(Collections.singletonList(issue));
        assertEquals(0, stats.getTotalPoints());
    }

    @Test
    public void issuesBeforeLegacyDateAreProcessed() {
        final Issue issue = TestUtil.createIssue(LocalDate.of(2016, 4, 20), 30, SonarStats.SeverityType.BLOCKER);
        final SonarStats stats = service.fromIssueList(Collections.singletonList(issue));
        assertNotEquals(0, stats.getTotalPoints());
    }

    @Test
    public void checkStatsNumbers() {
        final LocalDate date = LocalDate.of(2016, 1, 1);
        final Issue i1 = TestUtil.createIssue(date, 5, SonarStats.SeverityType.MINOR);
        final Issue i2 = TestUtil.createIssue(date, 10, SonarStats.SeverityType.BLOCKER);
        final Issue i3 = TestUtil.createIssue(date, 15, SonarStats.SeverityType.CRITICAL);
        final Issue i4 = TestUtil.createIssue(date, 20, SonarStats.SeverityType.INFO);
        final Issue i5 = TestUtil.createIssue(date, 25, SonarStats.SeverityType.MAJOR);

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


}