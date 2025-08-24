//package com.researchservice.model;
//
//import java.util.List;
//
//public class ResearchResponse {
//    private List<Paper> answer;
//
//    public ResearchResponse() {}
//
//    public ResearchResponse(List<Paper> answer) {
//        this.answer = answer;
//    }
//
//    public List<Paper> getAnswer() { return answer; }
//    public void setAnswer(List<Paper> answer) { this.answer = answer; }
//}
package com.researchservice.model;

import java.util.List;

public class ResearchResponse {

    private List<Paper> answer;

    public ResearchResponse() {}

    public ResearchResponse(List<Paper> answer) {
        this.answer = answer;
    }

    // Getter
    public List<Paper> getAnswer() {
        return answer;
    }

    // Setter
    public void setAnswer(List<Paper> answer) {
        this.answer = answer;
    }
}
