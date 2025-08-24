//package com.researchservice.controller;
//
//import com.researchservice.model.Paper;
//import com.researchservice.model.ResearchRequest;
//import com.researchservice.model.ResearchResponse;
//import com.researchservice.service.PythonAgentClient;
//import com.researchservice.service.RankingService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Arrays;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/research")
//public class ResearchController {
//
//    @Autowired
//    private PythonAgentClient python;
//
//    @Autowired
//    private RankingService ranker;
//
//    @PostMapping
//    public ResearchResponse research(@RequestBody ResearchRequest request) {
//        String query = request.getQuery();
//        Paper[] raw = python.fetchSources(query);
//        List<Paper> ranked = ranker.rankAndDedup(query, Arrays.asList(raw));
//        return new ResearchResponse(ranked);
//    }
package com.researchservice.controller;

import com.researchservice.model.Paper;
import com.researchservice.model.ResearchResponse;
import com.researchservice.service.PythonAgentClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ResearchController {

    @Autowired
    private PythonAgentClient pythonAgentClient;

    @PostMapping("/research")
    public Map<String, Object> research(@RequestBody Map<String, String> request) {
        String query = request.get("query");
        if (query == null || query.isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Query missing");
            return error;
        }

        // Fetch papers and summary from Python Agent
        ResearchResponse response = pythonAgentClient.fetchResearch(query);
        List<Paper> papers = response.getAnswer();

        // Build response map
        Map<String, Object> result = new HashMap<>();
        result.put("introduction", "This brief covers " + papers.size() + " papers related to: " + query);

        // Key points (titles + year)
        result.put("key_points", papers.stream()
                .map(p -> p.getTitle() + " (" + p.getPublished() + ").")
                .toList());

        // Citations
        result.put("citations", papers);

        // Conclusion
        result.put("conclusion", "These sources collectively outline recent directions and methods; see citations for details.");

        return result;
    }
}
