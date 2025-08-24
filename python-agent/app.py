# Creating an app.py will turn this Python agent into a REST API. Without this the other files are just CLI files

# Let your Java Spring Boot service call it directly at http://localhost:5000/research.

# Allow you to return structured data (title, link, published) so Day 2 algorithms (ranking, deduplication, TF-IDF) can work smoothly.

# app.py
from flask import Flask, request, jsonify
from research import run_research

app = Flask(__name__)

@app.route("/research", methods=["POST"])
def research():
    data = request.json
    query = data.get("query", "")
    if not query:
        return jsonify({"error": "Query missing"}), 400

    try:
        summary = run_research(query)
        return jsonify(summary)
    except Exception as e:
        print("Error:", e)
        return jsonify({"error": "Failed to fetch research"}), 500

if __name__ == "__main__":
    app.run(debug=True)
