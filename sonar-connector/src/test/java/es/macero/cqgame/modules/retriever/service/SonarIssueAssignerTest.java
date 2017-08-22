package es.macero.cqgame.modules.retriever.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.net.URI;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import es.macero.cqgame.modules.configuration.domain.SonarServerConfiguration;
import es.macero.cqgame.modules.configuration.service.SonarServerConfigurationService;
import es.macero.cqgame.modules.sonarapi.resultbeans.Issues;

public class SonarIssueAssignerTest {

	@Mock
	SonarServerConfigurationService configurationService;

	@Mock
	RestTemplate restTemplate;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void test() {
		when(configurationService.getConfiguration()).thenReturn(new SonarServerConfiguration(null, null, null));
		Issues body = new Issues();
		ResponseEntity<Issues> response = new ResponseEntity<>(body, HttpStatus.OK);
		when(restTemplate.exchange(any(URI.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(Issues.class)))
				.thenReturn(response);
		when(restTemplate.exchange(any(URI.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(Issues.class)))
		.thenReturn(response);
		SonarIssueAssigner issueAssigner = new SonarIssueAssignerImpl(configurationService, restTemplate);
		issueAssigner.assignIssues("analyzerFunctionalView-myClass");
	}

	@After
	public void tearDown() throws Exception {
	}

}
