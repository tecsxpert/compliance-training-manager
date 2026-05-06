from flask import Blueprint, request, jsonify
from services.groq_client import call_groq

describe_bp = Blueprint("describe", __name__)

def load_prompt():
    with open("prompts/describe.txt") as f:
        return f.read()

@describe_bp.route("/describe", methods=["POST"])
def describe():
    data = request.get_json()

    if not data or "text" not in data:
        return jsonify({"error": "text required"}), 400

    prompt = load_prompt().replace("{input}", data["text"])
    result = call_groq(prompt)

    return jsonify({
        "success": result["success"],
        "description": result["data"]
    })