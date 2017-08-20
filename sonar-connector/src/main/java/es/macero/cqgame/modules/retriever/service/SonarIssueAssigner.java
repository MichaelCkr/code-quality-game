package es.macero.cqgame.modules.retriever.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import es.macero.cqgame.modules.configuration.domain.SonarServerConfiguration;
import es.macero.cqgame.modules.configuration.service.SonarServerConfigurationService;
import es.macero.cqgame.modules.sonarapi.resultbeans.Issue;

@Service
@Component
public class SonarIssueAssigner {

	private static final String GET_ISSUES_FROM_CLASS = "/api/issues/search?componentKeys={projectId}:{moduleId}:{classPath}&statuses={status}&assigned={assigned}";
	private static final String POST_ISSUES_ASSIGN = "/api/issues/bulk_change?actions={actions}&assign.assignee={assignee}&issues={issueKey}";
	private static Map<String, String> mapping;
	private SonarServerConfiguration configuration;
	private List<String> issueKeys = new ArrayList<>();

	static {
		Map<String, String> map = new HashMap<>();
		map.put("edi.validation", "com.nextlevel.editools");
		map.put("edi.resources", "com.nextlevel.editools");
		map.put("edi.generator", "com.nextlevel.editools");
		map.put("analyzerFunctionalView", "com.nextlevel.editools");
		mapping = Collections.unmodifiableMap(map);
	}

	@Autowired
	public SonarIssueAssigner(final SonarServerConfigurationService configurationService) {
		this.configuration = configurationService.getConfiguration();
	}

	public void assignIssues(String classname) {
		processSingleClass(classname);
	}

	private void processSingleClass(String classname) {
		for (Entry<String, String> mapElement : mapping.entrySet()) {
			if (StringUtils.contains(classname, mapElement.getKey())) {
				processProject(mapElement.getValue(), mapElement.getKey(), classname);
			}
		}
	}

	private void processProject(String projectId, String moduleId, String classname) {
		String sonarUrl = "http://ci.next-level-integration.com/sonar/";
		String user = configuration.getUser();
		String passwd = configuration.getPassword();
		RestTemplate restTemplate = new RestTemplate();
		RequestLauncher launcher = new RequestLauncher(user, passwd, restTemplate) {

			@Override
			public void process(String id, List<Issue> issues) {
				issueKeys = issues.stream().map(Issue::getKey).collect(Collectors.toList());
				System.err.println(issueKeys);
				// for (Issue issue : issues) {
				// System.err.println(issue);
				// issueKeys.add(issue.getKey());
				// }
			}

			@Override
			public URI getUrl(String assignee, int pageIndex) {
				System.err.println(assignee);
				System.err.println(pageIndex);
				String issueStatus = "OPEN";
				String isAssigned = "false";
				URI uri = UriComponentsBuilder.fromHttpUrl(sonarUrl + GET_ISSUES_FROM_CLASS)
						.buildAndExpand("assign", moduleId,
								"src/main/java/com/nextlevel/editools/edi/validation/ClonerFactory.java", issueStatus,
								isAssigned)
						.toUri();
				System.err.println(uri);
				return uri;
			}
		};
		launcher.accept("mecker");
		System.err.println(issueKeys);
		RequestLauncher launcher2 = new RequestLauncher(user, passwd, restTemplate) {

			@Override
			public void process(String id, List<Issue> issues) {
				// TODO Auto-generated method stub
				System.err.println(id);
				System.err.println(issues);
			}

			@Override
			public URI getUrl(String assignee, int pageIndex) {
				URI uri = UriComponentsBuilder.fromHttpUrl(sonarUrl + POST_ISSUES_ASSIGN)
						.buildAndExpand("assign", "mecker", "38c32639-f2c9-45de-8d30-0ee73582f73a").toUri();
				System.err.println(uri);
				return uri;
			}
		};
		launcher2.setMethod(HttpMethod.POST);
		launcher2.accept("mecker");
	}
}
