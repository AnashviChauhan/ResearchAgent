# 📑 AI Research Agent

**AI Research Agent** is a research summarization tool built for **AgentHack 2025**.  
It combines **real academic papers** with **Portia AI’s summarization capabilities** to generate concise and insightful research reports.  

The goal is to help **researchers, students, and professionals** quickly understand complex academic content without spending hours reading multiple papers.

---

## ✨ Features
- 🔎 Fetches **real research papers** from sources like arXiv, Wikipedia API , and other APIs.  
- 🧠 Summarizes research using **Portia AI**, extracting:  
  - Main findings  
  - Methodologies  
  - Key insights  
- 📑 Produces **structured summaries** with:  
  - Introduction  
  - Key points  
  - Conclusion  
  - Citations (links, titles, years)  
- ⚡ Built with **Python** + **Java** backend for modular integration.  

---

## 🏗️ Tech Stack
- **Python** → Fetching sources & preprocessing  
- **Java (Spring Boot)** → Research service API  
- **Portia AI** → AI-powered summarization & structured reports  
- **APIs Used** → arXiv, Wikipedia API

---

## ⚙️ Setup & Usage

### 🔹 Prerequisites
- Python 3.9+  
- Java 17+  
- Postman or curl for API testing  
- Portia AI credentials / API access  

---

### 🔹 Running the Python Service
1. Open **Terminal 1**  
   ```bash
   py app.py
This starts the Python service.

### 🔹 Open Terminal 2 and test with curl:

curl -X POST http://127.0.0.1:5000/research \
-H "Content-Type: application/json" \
-d "{\"query\":\"Latest AI research papers 2025\"}"


✅ You should see a briefing of research papers for your query.

###🔹 Running the Java Service

Navigate to the java folder.

Run the Spring Boot app:
java -jar target/ResearchServiceApplication.jar


###🔹Open Postman (or curl) and make a POST request:

URL: http://localhost:8080/api/research
Body (raw JSON):
{
    "query": "Latest AI research papers 2025"
}


✅ Example Response:

{
  "citations": [
    {"link": "#", "title": "AI Agents in Healthcare", "year": 2025},
    {"link": "#", "title": "Multi-agent Systems Overview", "year": 2025}
  ],
  "conclusion": "...",
  "introduction": "...",
  "key_points": [
    "AI Agents in Healthcare (2025).",
    "Multi-agent Systems Overview (2025)."
  ]
}

###🔹 Future Enhancements:

Bias checker → highlight if sources are skewed to one author/journal

Citation style formatting (APA/MLA)

Export summaries to PDF / Notion / Markdown

UI/UX

🎯 Impact

By integrating Portia AI with real research papers, the project ensures summaries are:

Factually grounded in real sources

Concise & accessible for quicker knowledge discovery

Time-saving for researchers and professionals

Supportive of decision-making in research-heavy fields

👩‍💻 Author : Developed by Anashvi Chauhan and Navya Yadav for AgentHack 2025 🎉
