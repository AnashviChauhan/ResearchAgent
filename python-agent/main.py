#Entry point of the Python agent. Runs the research workflow.

from research import google_search

def main():
    query = "Latest AI research papers 2025"
    results = google_search(query)
    
    print("🔎 Top Search Results:")
    for idx, r in enumerate(results, 1):
        print(f"{idx}. {r}")

if __name__ == "__main__":
    main()
