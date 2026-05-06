from flask import Blueprint, request, jsonify
from services.groq_client import call_groq

recommend_bp = Blueprint("recommend", __name__)

def load_prompt():
    with open("prompts/recommend.txt") as f:
        return f.read()

@recommend_bp.route("/recommend", methods=["POST"])
def recommend():
    data = request.get_json()

    if not data or "text" not in data:
        return jsonify({"error": "text required"}), 400

    prompt = load_prompt().replace("{input}", data["text"])
    result = call_groq(prompt)

    return jsonify({
        "success": result["success"],
        "recommendations": result["data"]
    })