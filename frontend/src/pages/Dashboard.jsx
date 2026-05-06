import { useState, useEffect } from "react";
import { describeTraining, recommendTraining } from "../services/aiService";
import { getDashboardMetrics } from "../services/trainingService";

export default function Dashboard() {
  const [input, setInput] = useState("");
  const [desc, setDesc] = useState("");
  const [rec, setRec] = useState("");
  const [loading, setLoading] = useState(false);
  const [metrics, setMetrics] = useState({ totalTrainings: 0, pendingTrainings: 0, completedTrainings: 0 });

  useEffect(() => {
    getDashboardMetrics()
      .then(res => setMetrics(res.data))
      .catch(console.error);
  }, []);

  const handleAI = async () => {
    setLoading(true);
    try {
      const d = await describeTraining(input);
      const r = await recommendTraining(input);
      setDesc(d.data?.description || "No description generated.");
      setRec(r.data?.recommendations || "No recommendations generated.");
    } catch (e) {
      setDesc("AI service unavailable.");
      setRec("");
    }
    setLoading(false);
  };

  return (
    <div className="max-w-6xl mx-auto p-6 space-y-8">
      <h2 className="text-3xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-blue-400 to-indigo-500">
        Analytics & Insights
      </h2>

      {/* Metrics Row */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="glass-card p-6 flex flex-col items-center justify-center">
          <p className="text-sm text-slate-400 mb-1">Total Trainings</p>
          <p className="text-4xl font-bold text-blue-400">{metrics.totalTrainings}</p>
        </div>
        <div className="glass-card p-6 flex flex-col items-center justify-center">
          <p className="text-sm text-slate-400 mb-1">Pending</p>
          <p className="text-4xl font-bold text-yellow-400">{metrics.pendingTrainings}</p>
        </div>
        <div className="glass-card p-6 flex flex-col items-center justify-center">
          <p className="text-sm text-slate-400 mb-1">Completed</p>
          <p className="text-4xl font-bold text-green-400">{metrics.completedTrainings}</p>
        </div>
      </div>

      <div className="glass-card p-8">
        <h3 className="text-xl font-semibold mb-4 text-white">AI Assistant</h3>
        <label className="block text-sm font-medium text-slate-400 mb-2">Describe a compliance gap or situation</label>
        <textarea 
          className="input-field mb-4 h-32 resize-none"
          placeholder="e.g. We need a new training module for GDPR compliance in Europe..."
          value={input}
          onChange={(e) => setInput(e.target.value)} 
        />
        <button 
          onClick={handleAI}
          disabled={loading || !input}
          className="btn-primary w-full md:w-auto px-8"
        >
          {loading ? "Analyzing..." : "Generate AI Insights"}
        </button>
      </div>

      {(desc || rec) && (
        <div className="grid md:grid-cols-2 gap-6">
          <div className="glass-card p-6">
            <h3 className="text-lg font-semibold text-blue-400 mb-4 flex items-center">
              <span className="mr-2">📝</span> AI Description
            </h3>
            <div className="prose prose-invert prose-sm max-w-none text-slate-300">
              <p className="whitespace-pre-wrap">{desc}</p>
            </div>
          </div>

          <div className="glass-card p-6">
            <h3 className="text-lg font-semibold text-green-400 mb-4 flex items-center">
              <span className="mr-2">💡</span> Recommendations
            </h3>
            <div className="prose prose-invert prose-sm max-w-none text-slate-300">
              <p className="whitespace-pre-wrap">{rec}</p>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}