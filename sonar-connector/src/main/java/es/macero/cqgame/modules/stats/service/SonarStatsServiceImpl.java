package es.macero.cqgame.modules.stats.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.macero.cqgame.modules.users.dao.SonarUserRepository;
import es.macero.cqgame.modules.stats.domain.SonarStats;
import es.macero.cqgame.modules.stats.domain.SonarStatsRow;
import es.macero.cqgame.modules.users.domain.SonarUser;
import es.macero.cqgame.modules.sonarapi.resultbeans.Issue;

@Service
final class SonarStatsServiceImpl implements SonarStatsService {

    private static final Log log = LogFactory.getLog(SonarStatsServiceImpl.class);

    private Map<String, SonarUser> idsAndUsers;
    private Map<String, SonarStats> statsPerId;

    private final SonarUserRepository sonarDao;
    private final SonarStatsCalculatorService sonarStatsCalculatorService;

    @Autowired
    public SonarStatsServiceImpl(final SonarUserRepository sonarDao, final SonarStatsCalculatorService sonarStatsCalculatorService) {
        this.sonarDao = sonarDao;
        this.sonarStatsCalculatorService = sonarStatsCalculatorService;
        statsPerId = new ConcurrentHashMap<>();
        loadUsers();
    }

    private void loadUsers() {
        idsAndUsers = sonarDao.findAll().stream().collect(Collectors.toMap(SonarUser::getId, Function.identity()));
    }

    @Override
    public Set<String> getIds() {
        return idsAndUsers.keySet();
    }

    @Override
    public void updateStats(final String id, final List<Issue> issues) {
        SonarStats stats = sonarStatsCalculatorService.fromIssueList(issues);
        statsPerId.put(id, stats);
        log.info("Processing " + id + "; Stats: " + stats);
    }

    @Override
    public Collection<SonarUser> getUsers() {
        return idsAndUsers.values();
    }

    @Override
    public Collection<SonarStatsRow> getSortedStatsPerUser() {
        List<SonarStatsRow> rows = new ArrayList<>();
        for (Entry<String, SonarStats> entry : statsPerId.entrySet()) {
            SonarUser user = idsAndUsers.get(entry.getKey());
            SonarStats stats = entry.getValue();
            rows.add(new SonarStatsRow(user, stats));
        }
        return rows.stream().sorted((r1, r2) -> Integer.compare(r2.getTotalPoints(), r1.getTotalPoints())).collect(Collectors.toList());
    }

    @Override
    public Collection<SonarStatsRow> getSortedStatsPerTeam() {
        return getSortedStatsPerUser().stream().collect(
                Collectors.toMap(SonarStatsRow::getUserTeam, Function.identity(), SonarStatsRow::combine))
                .values().stream()
                .sorted((r1, r2) -> Integer.compare(r2.getTotalPoints(), r1.getTotalPoints())).collect(Collectors.toList());
    }


}
