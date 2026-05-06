from flask import Flask, jsonify
from flask_cors import CORS
from routes.describe import describe_bp
from routes.recommend import recommend_bp
from routes.categorise import categorise_bp
from routes.generate_report import generate_report_bp

app = Flask(__name__)
CORS(app)

# Register routes
app.register_blueprint(describe_bp, url_prefix="/api")
app.register_blueprint(recommend_bp, url_prefix="/api")
app.register_blueprint(categorise_bp, url_prefix="/api")
app.register_blueprint(generate_report_bp, url_prefix="/api")

@app.route("/health")
def health():
    return jsonify({
        "status": "UP",
        "service": "AI Service"
    })

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)