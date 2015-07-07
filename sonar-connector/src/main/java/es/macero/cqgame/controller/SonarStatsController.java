package es.macero.cqgame.controller;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.macero.cqgame.domain.stats.SonarStatsRow;
import es.macero.cqgame.service.SonarStatsService;

@Controller
@RequestMapping("/legacykillers")
public class SonarStatsController
{
    @Autowired
    private SonarStatsService sonarStatsService;

    @RequestMapping(value="/users", method = RequestMethod.GET)
    public String statsHome(Map<String, Object> model)
    {
        Collection<SonarStatsRow> stats = sonarStatsService.getSortedStatsPerUser();
        model.put("stats", stats);
        return "sonarstats";
    }

    @RequestMapping(value="/teams", method = RequestMethod.GET)
    public String statsTeams(Map<String, Object> model)
    {
        Collection<SonarStatsRow> stats = sonarStatsService.getSortedStatsPerTeam();
        model.put("statsTeams", stats);
        return "sonarstats";
    }
}
