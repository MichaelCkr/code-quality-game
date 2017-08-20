package es.macero.cqgame.app;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import es.macero.cqgame.modules.retriever.service.SonarIssueAssignerImpl;

public class HookApplicationTest {

	@Mock
	SonarIssueAssignerImpl issueAssigner;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void test() throws Exception {
		String path = "C:\\Users\\mecker\\AppData\\Local\\Temp\\svnD3AE.tmp";
		String depth = "3";
		String messageFile = "C:\\Users\\mecker\\AppData\\Local\\Temp\\svnD3AF.tmp";
		String cwd = "C:\\NLI\\P_EdiTools\\EdiToolsTrunk\\edi.validation\\src\\main\\java\\com\\nextlevel\\editools\\edi\\validation";

		path = "/app/commitedFiles.tmp";
		messageFile = "/app/commitMessage.tmp";
		HookApplication hookApp = new HookApplication(issueAssigner);
		String classname = "C:/NLI/P_EdiTools/EdiToolsTrunk/edi.validation/src/main/java/com/nextlevel/editools/edi/validation/ClonerFactory.java";
		Mockito.doNothing().when(issueAssigner).assignIssues(classname);
		hookApp.process(new String[] { path, depth, messageFile, cwd });
	}

	@After
	public void tearDown() throws Exception {
		// no op
	}

}
