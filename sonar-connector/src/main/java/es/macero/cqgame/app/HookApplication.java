package es.macero.cqgame.app;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.client.RestTemplate;

import es.macero.cqgame.modules.configuration.service.SonarServerConfigurationService;
import es.macero.cqgame.modules.retriever.service.SonarIssueAssigner;

//@Service
@ComponentScan(basePackages = "es.macero.cqgame")
@EnableAutoConfiguration
//@Configurable
//@PropertySource("classpath:config.properties")
public class HookApplication extends SpringBootServletInitializer {

	// componentKeys=com.nextlevel.editools:edi.validation:src/main/java/com/nextlevel/editools/edi/validation/ClonerFactory.java
	// componentKeys=B2B2.5-trunk:extensions:core-extension:com.nextlevel.b2b.dividing.network:src/main/java/org/b2bbp/dividing/network/actions/strategies/total/INSRPT_SplitStrategy.java

	//@Autowired
	//private static HookApplication application;
	//@Autowired
	//private SonarIssueAssigner issueAssigner;

	@Autowired
	public HookApplication(final SonarIssueAssigner issueAssigner) {
		System.err.println("TEST");
	}
	
//	@Autowired
//	public HookApplication() {
//		System.err.println("TEST");
//	}

	public static void main(String[] args) throws Exception {
        //SpringApplication.run(HookApplication.class, args);
		//ApplicationContext context = new AnnotationConfigApplicationContext("es.macero.cqgame");
		//HookApplication application = new HookApplication();
		SpringApplication springApplication = new SpringApplication(HookApplication.class);
		Set<ApplicationListener<?>> listeners = springApplication.getListeners();
		Set<Object> sources = springApplication.getSources();
		springApplication.setApplicationContextClass(AnnotationConfigApplicationContext.class);
		//springApplication.setWebEnvironment(true);
		ConfigurableApplicationContext applicationContext = springApplication.run(args);
		//ApplicationContext context = new AnnotationConfigApplicationContext(SonarIssueAssigner.class);
		ConfigurableEnvironment environment = (ConfigurableEnvironment) applicationContext.getEnvironment();
		//springApplication.setEnvironment(environment);
		//ApplicationEnvironmentPreparedEvent event = new ApplicationEnvironmentPreparedEvent(springApplication, new String[] {},
		//		environment);
		//context.publishEvent(event);
		//Environment environment2 = context.getEnvironment();
		String[] names = applicationContext.getBeanDefinitionNames();
		String file = args[0];
		String depth = args[1];
		String messageFile = args[2];
		String folder = args[3];
		Path files = getPath(file);
		List<String> allLines = Files.readAllLines(files);
		//application.process(allLines);
		System.err.println(allLines);
		Path message = getPath(messageFile);
		String commitMessage = new String(Files.readAllBytes(message));
		System.err.println(commitMessage);
	}

	private void process(List<String> allLines) {
		for (String classname : allLines) {
			//issueAssigner.assignIssues(classname);
		}
	}

	private static Path getPath(String file) throws URISyntaxException {
		URL resource = HookApplication.class.getResource(file);
		File fileToRead = new File(resource.toURI());
		Path path = fileToRead.toPath();
		return path;
	}

}
