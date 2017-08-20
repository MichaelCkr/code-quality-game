package es.macero.cqgame.app;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HookApplicationTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() throws Exception {
		String path = "C:\\Users\\mecker\\AppData\\Local\\Temp\\svnD3AE.tmp";
		String depth = "3";
		String messageFile = "C:\\Users\\mecker\\AppData\\Local\\Temp\\svnD3AF.tmp";
		String cwd = "C:\\NLI\\P_EdiTools\\EdiToolsTrunk\\edi.validation\\src\\main\\java\\com\\nextlevel\\editools\\edi\\validation";

		path = "/app/commitedFiles.tmp";
		messageFile = "/app/commitMessage.tmp";

		HookApplication.main(new String[] { path, depth, messageFile, cwd });
	}

	@After
	public void tearDown() throws Exception {
	}

}
