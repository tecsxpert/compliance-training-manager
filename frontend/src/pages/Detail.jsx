import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import { getTrainingById } from "../services/trainingService";
import { describeTraining } from "../services/aiService";

export default function Detail() {
  const { id } = useParams();
  const [data, setData] = useState(null);
  const [aiDesc, setAiDesc] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    getTrainingById(id).then(res => setData(res.data)).catch(console.error);
  }, [id]);

  const handleAiDescription = async () => {
    setLoading(true);
    try {
      const res = await describeTraining(`Generate a detailed description for compliance training titled '${data.title}'. Context: ${data.description}`);
      setAiDesc(res.data?.description || "No description generated.");
    } catch (e) {
      setAiDesc("AI service unavailable.");
    }
    setLoading(false);
  };

  if (!data) return <div className="text-center mt-20 text-slate-400">Loading...</div>;

  return (
    <div className="max-w-4xl mx-auto p-6 space-y-8">
      <Link to="/list" className="text-blue-400 hover:text-blue-300 transition-colors">← Back to Trainings</Link>
      
      <div className="glass-card p-8">
        <div className="flex justify-between items-start mb-6">
          <h2 className="text-3xl font-bold text-white">{data.title}</h2>
          <span className={`px-3 py-1 rounded-full text-xs font-medium ${
                data.status === 'COMPLETED' ? 'bg-green-500/20 text-green-400' : 'bg-yellow-500/20 text-yellow-400'
              }`}>
            {data.status || "PENDING"}
          </span>
        </div>
        
        <div className="mb-8 text-slate-300">
          <h3 className="text-sm font-semibold text-slate-400 uppercase tracking-wider mb-2">Description</h3>
          <p className="whitespace-pre-wrap">{data.description || "No description provided."}</p>
        </div>

        <div className="border-t border-slate-700 pt-6">
          <h3 className="text-lg font-semibold text-blue-400 mb-4 flex items-center">
            <span className="mr-2">✨</span> AI Enhanced Description
          </h3>
          
          {!aiDesc ? (
             <button 
                onClick={handleAiDescription}
                disabled={loading}
                className="btn-primary"
              >
                {loading ? "Generating..." : "Generate with AI"}
              </button>
          ) : (
            <div className="bg-slate-800/50 rounded-lg p-6 border border-slate-700">
               <p className="text-slate-300 whitespace-pre-wrap">{aiDesc}</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}