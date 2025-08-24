#Handles searching and scraping (fetching info from the web). 
# research.py 
# Real arXiv fetcher. No API key required. 
# import requests 
# import feedparser 
# ARXIV_API = "http://export.arxiv.org/api/query" 
# def arxiv_search(query: str, max_results: int = 20): # """ # Fetch papers from arXiv. Returns list of dicts: # {title, link, published, abstract, source} # """ # params = { # "search_query": query, # "start": 0, # "max_results": max_results, # "sortBy": "relevance", # "sortOrder": "descending", # } # r = requests.get(ARXIV_API, params=params, timeout=20) # r.raise_for_status() # feed = feedparser.parse(r.text) # papers = [] # for e in feed.entries: # title = (e.get("title") or "").replace("\n", " ").strip() # link = e.get("link") or "" # published = e.get("published") or e.get("updated") or "" # abstract = (e.get("summary") or "").replace("\n", " ").strip() # papers.append({ # "title": title, # "link": link, # "published": published, # "abstract": abstract, # "source": "arxiv", # }) 
# return papers # research.py 
# arXiv fetcher. Returns list of dicts with keys: title, link, published, abstract, source
# research.py

# research.py
import requests
import feedparser
from portia_client import summarize_with_portia

ARXIV_API = "http://export.arxiv.org/api/query"

# Wikipedia search helper
WIKI_API = "https://en.wikipedia.org/w/api.php"

def fetch_arxiv(query, max_results=10):
    params = {
        "search_query": query,
        "start": 0,
        "max_results": max_results,
        "sortBy": "relevance",
        "sortOrder": "descending",
    }
    r = requests.get(ARXIV_API, params=params, timeout=20)
    r.raise_for_status()
    feed = feedparser.parse(r.text)
    papers = []
    for e in feed.entries:
        papers.append({
            "title": (e.get("title") or "").replace("\n", " ").strip(),
            "link": e.get("link") or "",
            "published": (e.get("published") or e.get("updated") or ""),
            "abstract": (e.get("summary") or "").replace("\n", " ").strip(),
            "source": "arxiv",
        })
    return papers

def fetch_wikipedia(query, limit=5):
    params = {
        "action": "query",
        "list": "search",
        "srsearch": query,
        "utf8": "",
        "format": "json",
        "srlimit": limit
    }
    r = requests.get(WIKI_API, params=params, timeout=10)
    r.raise_for_status()
    data = r.json()
    articles = []
    for item in data.get("query", {}).get("search", []):
        title = item.get("title")
        link = f"https://en.wikipedia.org/wiki/{title.replace(' ', '_')}"
        articles.append({
            "title": title,
            "link": link,
            "published": None,
            "abstract": "",
            "source": "Wikipedia"
        })
    return articles

def run_research(query: str):
    # Fetch papers
    arxiv_papers = fetch_arxiv(query)
    wiki_articles = fetch_wikipedia(query)
    all_papers = arxiv_papers + wiki_articles

    # Summarize with Portia
    summary = summarize_with_portia(query, all_papers)
    return summary
