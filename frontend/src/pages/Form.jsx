import { useState } from "react";
import { createTraining } from "../services/trainingService";
import { useNavigate } from "react-router-dom";

export default function Form() {
  const [title, setTitle] = useState("");
  const [desc, setDesc] = useState("");
  const [status, setStatus] = useState("PENDING");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await createTraining({ title, description: desc, status });
      navigate("/");
    } catch (err) {
      console.error(err);
      alert("Failed to create training");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-2xl mx-auto p-6">
      <h2 className="text-3xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-blue-400 to-indigo-500 mb-8">
        Create New Training
      </h2>
      <form onSubmit={handleSubmit} className="glass-card p-8 space-y-6">
        <div>
          <label className="block text-sm font-medium text-slate-300 mb-2">Title</label>
          <input 
            required
            className="input-field"
            placeholder="e.g., Annual Security Awareness" 
            value={title}
            onChange={(e) => setTitle(e.target.value)} 
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-slate-300 mb-2">Description</label>
          <textarea 
            rows="4"
            className="input-field"
            placeholder="Detailed description of the training module..." 
            value={desc}
            onChange={(e) => setDesc(e.target.value)} 
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-slate-300 mb-2">Status</label>
          <select 
            className="input-field"
            value={status}
            onChange={(e) => setStatus(e.target.value)}
          >
            <option value="PENDING">Pending</option>
            <option value="COMPLETED">Completed</option>
          </select>
        </div>
        <div className="pt-4">
          <button type="submit" disabled={loading} className="btn-primary w-full flex justify-center items-center">
            {loading ? "Creating..." : "Create Training"}
          </button>
        </div>
      </form>
    </div>
  );
}