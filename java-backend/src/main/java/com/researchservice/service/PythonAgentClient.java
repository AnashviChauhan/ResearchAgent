//package com.researchservice.service;
//
//import com.researchservice.model.Paper;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class PythonAgentClient {
//
//    private static final String PY_URL = "http://127.0.0.1:5000/research";
//
//    public Paper[] fetchSources(String query) {
//        RestTemplate rt = new RestTemplate();
//        Map<String, String> payload = new HashMap<>();
//        payload.put("query", query);
//
//        Map<?,?> response = rt.postForObject(PY_URL, payload, Map.class);
//        if (response == null || !response.containsKey("answer")) {
//            return new Paper[0];
//        }
//        Object answer = response.get("answer");
//        List<?> list = (List<?>) answer;
//
//        Paper[] arr = new Paper[list.size()];
//        for (int i = 0; i < list.size(); i++) {
//            Map<?,?> row = (Map<?,?>) list.get(i);
//            Paper p = new Paper();
//            p.setTitle(String.valueOf(row.getOrDefault("title","")));
//            p.setLink(String.valueOf(row.getOrDefault("link","")));
//            p.setPublished(String.valueOf(row.getOrDefault("published","")));
//            p.setAbstractText(String.valueOf(row.getOrDefault("abstract","")));
//            p.setSource(String.valueOf(row.getOrDefault("source","arxiv")));
//            arr[i] = p;
//        }
//        return arr;
//    }
//}
package com.researchservice.service;

import com.researchservice.model.Paper;
import com.researchservice.model.ResearchResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PythonAgentClient {

    private static final String PY_URL = "http://127.0.0.1:5000/research";

    public ResearchResponse fetchResearch(String query) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> payload = new HashMap<>();
        payload.put("query", query);

        Map<?, ?> response = restTemplate.postForObject(PY_URL, payload, Map.class);
        if (response == null || !response.containsKey("citations")) {
            return new ResearchResponse(new ArrayList<>());
        }

        List<?> citations = (List<?>) response.get("citations");
        List<Paper> papers = new ArrayList<>();

        for (Object obj : citations) {
            if (!(obj instanceof Map)) continue;
            Map<?, ?> row = (Map<?, ?>) obj;

            Paper p = new Paper();
            p.setTitle(stringOrDefault(row.get("title"), ""));
            p.setLink(stringOrDefault(row.get("link"), ""));
            p.setPublished(stringOrDefault(row.get("year"), ""));
            p.setSource(stringOrDefault(row.get("source"), "Portia"));
            p.setAbstractText(stringOrDefault(row.get("abstract"), ""));
            papers.add(p);
        }

        return new ResearchResponse(papers);
    }

    private String stringOrDefault(Object obj, String def) {
        return obj == null ? def : obj.toString();
    }
}
