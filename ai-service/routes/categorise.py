from flask import Blueprint, request, jsonify
from services.groq_client import call_groq
import json

categorise_bp = Blueprint("categorise", __name__)

@categorise_bp.route("/categorise", methods=["POST"])
def categorise():
    data = request.get_json()
    if not data or "text" not in data:
        return jsonify({"error": "text required"}), 400

    prompt = f"Categorise the following compliance training description into one of: 'Security', 'Ethics', 'Data Privacy', 'HR', 'Safety'. Return only a JSON object like {{'category': '...', 'confidence': 0.95, 'reasoning': '...'}}. Description: {data['text']}"
    
    result = call_groq(prompt)
    if not result["success"]:
        return jsonify(result), 500
        
    try:
        parsed = json.loads(result["data"])
        return jsonify({"success": True, **parsed})
    except:
        return jsonify({"success": True, "category": "General", "confidence": 0.5, "reasoning": "Fallback category parsing failed"})
