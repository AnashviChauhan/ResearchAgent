# # portia_client.py
# # This is a SAFE hook for Portia. If you have a Portia endpoint + API key,
# # set env vars and it will call Portia. Otherwise, it returns a deterministic,
# # structured summary so your pipeline still works today.

import os
import requests
from typing import List, Dict

PORTIA_BASE_URL = os.getenv("PORTIA_BASE_URL")  # e.g. https://api.portia.ai
PORTIA_API_KEY  = os.getenv("PORTIA_API_KEY")   # provided by hackathon
TIMEOUT = 30

def _fallback_summary(query: str, papers: List[Dict]):
    # Deterministic, rule-based summary (no LLM), good enough for Day 2 demos.
    intro = f"This brief covers {min(len(papers), 5)} papers related to: {query}."
    key_points = []
    citations = []
    for p in papers[:7]:
        title = p.get("title","").strip()
        link = p.get("link","")
        pub = (p.get("published","") or "")[:4]
        if title:
            key_points.append(f"{title} ({pub}).")
        citations.append({
            "title": title,
            "link": link,
            "year": int(pub) if pub.isdigit() else None
        })
    conclusion = "These sources collectively outline recent directions and methods; see citations for details."

    return {
        "introduction": intro,
        "key_points": key_points[:7],
        "conclusion": conclusion,
        "citations": citations
    }

def summarize_with_portia(query: str, papers: List[Dict]):
    """
    If PORTIA_* env vars are set, call Portia. Otherwise, return fallback.
    """
    if not PORTIA_BASE_URL or not PORTIA_API_KEY:
        return _fallback_summary(query, papers)

    # Generic POST to your Portia agent (adjust path/payload to your account).
    # We keep it generic on purpose since hackathon accounts may differ.
    url = f"{PORTIA_BASE_URL.rstrip('/')}/v1/agents/run"
    payload = {
        "agent": "research-summarizer",     # your agent name/id in Portia
        "inputs": {
            "user_query": query,
            "papers": papers,
        },
        "output_schema": {
            "type": "object",
            "properties": {
                "introduction": {"type": "string"},
                "key_points": {"type": "array", "items": {"type": "string"}},
                "conclusion": {"type": "string"},
                "citations": {
                    "type": "array",
                    "items": {
                        "type": "object",
                        "properties": {
                            "title": {"type": "string"},
                            "link": {"type": "string"},
                            "year": {"type": ["integer","null"]}
                        },
                        "required": ["title","link"]
                    }
                }
            },
            "required": ["introduction","key_points","conclusion","citations"]
        }
    }
    headers = {
        "Authorization": f"Bearer {PORTIA_API_KEY}",
        "Content-Type": "application/json"
    }
    try:
        resp = requests.post(url, json=payload, headers=headers, timeout=TIMEOUT)
        resp.raise_for_status()
        data = resp.json()
        # Expect Portia to return structured JSON compatible with the schema above.
        # If shape differs, just map fields here once you know your exact Portia response.
        return data.get("output") or data
    except Exception as e:
        # Never crash your pipeline because of external service. Fallback instead.
        return _fallback_summary(query, papers)
