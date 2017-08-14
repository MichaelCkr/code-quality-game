package es.macero.cqgame.modules.badges.calculators;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import es.macero.cqgame.modules.badges.domain.SonarBadge;
import es.macero.cqgame.modules.badges.domain.TagBadge;

@Component
public class CodeNimblerBadgeCalculator extends TagBadgeCalculator {

	private static final List<TagBadge> badges = Arrays.asList(
			new TagBadge(250, () -> new SonarBadge("Graceful Tumbling Expert", "Fixing 500 or more issues with clumsy code.", 500)),
			new TagBadge(125, () -> new SonarBadge("Dexterous Coder", "Fixing 125 or more issues with clumsy code.", 300)),
			new TagBadge(50, () -> new SonarBadge("Agile Coder", "Fixing 50 or more issues with clumsy code.", 125)),
			new TagBadge(10, () -> new SonarBadge("Coordinated Coder", "Fixing 10 or more issues with clumsy code.", 50)));


	private static final String TAG_NAME = "clumsy";

	public List<TagBadge> getBadges() {
		return badges;
	}

	String getTagName() {
		return TAG_NAME;
	}
}
