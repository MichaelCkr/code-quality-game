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
import es.macero.cqgame.util.ApiHttpUtils;

public abstract class RequestLauncher implements Consumer<String> {

	private static final Log log = LogFactory.getLog(RequestLauncher.class);
	private String sonarUser;
	private String sonarPassword;
	private RestTemplate restTemplate;
	private HttpMethod method = HttpMethod.GET;

	public RequestLauncher(final String sonarUser, final String sonarPassword, RestTemplate restTemplate) {
		this.sonarUser = sonarUser;
		this.sonarPassword = sonarPassword;
		this.restTemplate = restTemplate;
	}

	@Override
	public void accept(final String id) {
		try {
			int pageIndex = 1;
			Integer totalPages = 1;
			List<Issue> issues = new ArrayList<>();
			while (pageIndex <= totalPages) {
				HttpEntity<String> request = new HttpEntity<>(getHeaders());
				URI uri = getUrl(id, pageIndex);
				ResponseEntity<Issues> response = restTemplate.exchange(uri, method, request, Issues.class);
				Issues body = response.getBody();
				if (pageIndex == 1) {
					Paging paging = body.getPaging();
					if (paging != null) {
						totalPages = (Integer) paging.getAdditionalProperties().get("pages");
					}
				}
				issues.addAll(body.getIssues());
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

	public abstract URI getUrl(final String assignee, final int pageIndex);

	public abstract void process(final String id, List<Issue> issues);

	public void setMethod(HttpMethod method) {
		this.method = method;
	}
}