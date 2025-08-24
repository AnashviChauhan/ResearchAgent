# Simple test client to call your Flask endpoints
import requests
url = "http://127.0.0.1:5000/research"
data = {"query": "Latest AI research papers 2025"} 
response = requests.post(url, json=data) 
print(response.json())