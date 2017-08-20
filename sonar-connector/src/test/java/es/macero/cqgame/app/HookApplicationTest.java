package es.macero.cqgame.app;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import es.macero.cqgame.modules.configuration.domain.sonar.SonarServerStatus;
import es.macero.cqgame.modules.retriever.service.SonarIssueAssigner;
import es.macero.cqgame.modules.sonarapi.resultbeans.Issue;
import es.macero.cqgame.modules.sonarapi.resultbeans.Issues;

public class HookApplicationTest {

	@Mock
	RestTemplate restTemplate;
	@Mock
	SonarIssueAssigner issueAssigner;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@SuppressWarnings("static-access")
	@Test
	public void test() throws Exception {
		String path = "C:\\Users\\mecker\\AppData\\Local\\Temp\\svnD3AE.tmp";
		String depth = "3";
		String messageFile = "C:\\Users\\mecker\\AppData\\Local\\Temp\\svnD3AF.tmp";
		String cwd = "C:\\NLI\\P_EdiTools\\EdiToolsTrunk\\edi.validation\\src\\main\\java\\com\\nextlevel\\editools\\edi\\validation";

		path = "/app/commitedFiles.tmp";
		messageFile = "/app/commitMessage.tmp";
//		Issues body = new Issues();
//		when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(Issues.class)))
//				.thenReturn(new ResponseEntity<>(body, HttpStatus.OK));
		HookApplication hookApp = new HookApplication(issueAssigner);
		String classname = "C:/NLI/P_EdiTools/EdiToolsTrunk/edi.validation/src/main/java/com/nextlevel/editools/edi/validation/ClonerFactory.java";
		//Mockito.doNothing().when(issueAssigner).assignIssues(classname);
		//Mockito.doNothing().when(issueAssigner).assignIssues(any(String.class));
		Mockito.doNothing().when(issueAssigner).assignIssues(classname);
		hookApp.process(new String[] { path, depth, messageFile, cwd });
	}

	@After
	public void tearDown() throws Exception {
	}

}
