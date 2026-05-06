import { useEffect, useState } from "react";
import { getAllTrainings } from "../services/trainingService";
import { Link } from "react-router-dom";

export default function List() {
  const [data, setData] = useState([]);

  useEffect(() => {
    getAllTrainings().then(res => setData(res.data)).catch(console.error);
  }, []);

  return (
    <div className="max-w-6xl mx-auto p-6">
      <div className="flex justify-between items-center mb-8">
        <h2 className="text-3xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-blue-400 to-indigo-500">
          Compliance Trainings
        </h2>
        <Link to="/create" className="btn-primary">
          + New Training
        </Link>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {data.map(item => (
          <div key={item.id} className="glass-card p-6">
            <h3 className="text-xl font-semibold mb-2">{item.title}</h3>
            <p className="text-slate-400 text-sm mb-4 line-clamp-2">
              {item.description || "No description provided."}
            </p>
            <div className="flex justify-between items-center">
              <span className={`px-3 py-1 rounded-full text-xs font-medium ${
                item.status === 'COMPLETED' ? 'bg-green-500/20 text-green-400' : 'bg-yellow-500/20 text-yellow-400'
              }`}>
                {item.status || "PENDING"}
              </span>
              <Link to={`/detail/${item.id}`} className="text-blue-400 hover:text-blue-300 text-sm font-medium">
                View Details →
              </Link>
            </div>
          </div>
        ))}
      </div>
      {data.length === 0 && (
        <div className="text-center text-slate-500 py-12 glass-card">
          <p>No training records found. Create one to get started!</p>
        </div>
      )}
    </div>
  );
}