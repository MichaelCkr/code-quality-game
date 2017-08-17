package es.macero.cqgame.modules.retriever.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import es.macero.cqgame.modules.sonarapi.resultbeans.Issue;
import es.macero.cqgame.modules.sonarapi.resultbeans.Issues;
import es.macero.cqgame.modules.sonarapi.resultbeans.Paging;
import es.macero.cqgame.modules.stats.service.SonarStatsService;
import es.macero.cqgame.util.ApiHttpUtils;

public abstract class RequestLauncher implements Consumer<String> {

	private static final Log log = LogFactory.getLog(RequestLauncher.class);
	private String sonarUser;
	private String sonarPassword;

	public RequestLauncher(final String sonarUser, final String sonarPassword) {
		this.sonarUser = sonarUser;
		this.sonarPassword = sonarPassword;
	}

	@Override
	public void accept(final String id) {
		try {
			int pageIndex = 1;
			Integer totalPages = 1;
			List<Issue> issues = new ArrayList<>();
			while (pageIndex <= totalPages) {
				RestTemplate restTemplate = new RestTemplate();
				HttpEntity<String> request = new HttpEntity<>(getHeaders());
				URI uri = getUrl(id, pageIndex);
				ResponseEntity<Issues> response = restTemplate.exchange(uri, HttpMethod.GET, request, Issues.class);
				if (pageIndex == 1) {
					Paging paging = response.getBody().getPaging();
					totalPages = (Integer) paging.getAdditionalProperties().get("pages");
				}
				issues.addAll(response.getBody().getIssues());
				pageIndex++;
			}
			process(id, issues);
		} catch (final HttpServerErrorException serverException) {
			log.error(serverException);
			throw serverException;
		}
	}

	private HttpHeaders getHeaders() {
		if (StringUtils.isNotBlank(sonarUser)) {
			return ApiHttpUtils.getHeaders(sonarUser, sonarPassword);
		}
		return new HttpHeaders();
	}

	abstract URI getUrl(final String assignee, final int pageIndex);

	abstract void process(final String id, List<Issue> issues);
}