package es.macero.cqgame.modules.retriever.service;

import java.net.URI;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import es.macero.cqgame.modules.configuration.domain.SonarServerConfiguration;
import es.macero.cqgame.modules.configuration.service.SonarServerConfigurationService;
import es.macero.cqgame.modules.sonarapi.resultbeans.Issue;
import es.macero.cqgame.modules.stats.service.SonarStatsService;

@Service
@EnableAsync
@EnableScheduling
final class SonarDataRetriever {

	private static final Log log = LogFactory.getLog(SonarDataRetriever.class);
	private static final String GET_ISSUES_COMMAND = "/api/issues/search?assignees={assignees}&resolutions={resolutions}&p={page}&ps={pageSize}";

	private final SonarStatsService statsService;
	private final SonarServerConfiguration configuration;

	@Autowired
	public SonarDataRetriever(final SonarStatsService statsService,
			final SonarServerConfigurationService configurationService) {
		this.statsService = statsService;
		this.configuration = configurationService.getConfiguration();
	}

	@Scheduled(fixedRate = 10 * 60000)
	public void retrieveData() {
		String sonarUrl = configuration.getUrl();
		RequestLauncher requestLauncher = new RequestLauncher(configuration.getUser(),
				configuration.getPassword()) {
			@Override
			URI getUrl(final String assignee, final int pageIndex) {
				URI uri = UriComponentsBuilder.fromHttpUrl(sonarUrl + GET_ISSUES_COMMAND)
						.buildAndExpand(assignee.toLowerCase() + "," + assignee.toUpperCase(), "FIXED", pageIndex, 500)
						.toUri();
				log.debug("generated URI " + uri);
				return uri;
			}

			@Override
			void process(final String id, List<Issue> issues) {
				statsService.updateStats(id, issues);
			}
		};
		// It seems that sonar doesn't allow parallel queries with same user since it
		// creates a register for internal
		// stats and that causes an error when inserting into the database.
		statsService.getIds().forEach(requestLauncher);
	}

}
