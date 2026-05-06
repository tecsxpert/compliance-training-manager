from flask import Blueprint, request, jsonify, Response
from services.groq_client import call_groq
import json
import time

generate_report_bp = Blueprint("generate_report", __name__)

@generate_report_bp.route("/generate-report", methods=["POST"])
def generate_report():
    data = request.get_json()
    if not data or "items" not in data:
        return jsonify({"error": "items required"}), 400

    items_str = json.dumps(data["items"])
    prompt = f"Generate an executive compliance report for these training items. Return JSON with {{'title': '...', 'executive_summary': '...', 'top_items': []}}. Items: {items_str}"
    
    result = call_groq(prompt)
    if not result["success"]:
        return jsonify(result), 500
        
    try:
        parsed = json.loads(result["data"])
        return jsonify({"success": True, "report": parsed})
    except:
        return jsonify({"success": True, "report": {"title": "General Report", "executive_summary": result["data"]}})

# SSE Streaming version (stub)
@generate_report_bp.route("/generate-report/stream", methods=["GET"])
def generate_report_stream():
    def generate():
        for chunk in ["Generating", " executive", " report", "...", " Done!"]:
            yield f"data: {chunk}\n\n"
            time.sleep(0.5)
    return Response(generate(), mimetype="text/event-stream")
