import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleLogin = (e) => {
    e.preventDefault();
    // replace with real API in future
    localStorage.setItem("token", "dummy-jwt");
    navigate("/");
  };

  return (
    <div className="flex items-center justify-center min-h-[80vh]">
      <div className="glass-card p-10 w-full max-w-md">
        <div className="text-center mb-8">
          <span className="text-4xl mb-4 block">🛡️</span>
          <h2 className="text-3xl font-bold text-white mb-2">Welcome Back</h2>
          <p className="text-slate-400">Sign in to manage compliance trainings</p>
        </div>

        <form onSubmit={handleLogin} className="space-y-6">
          <div>
            <label className="block text-sm font-medium text-slate-300 mb-2">Username</label>
            <input 
              required
              className="input-field" 
              placeholder="admin" 
              value={username}
              onChange={(e) => setUsername(e.target.value)} 
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-slate-300 mb-2">Password</label>
            <input 
              required
              type="password" 
              className="input-field" 
              placeholder="••••••••" 
              value={password}
              onChange={(e) => setPassword(e.target.value)} 
            />
          </div>
          <button type="submit" className="btn-primary w-full py-3 mt-4 text-lg">
            Sign In
          </button>
        </form>
      </div>
    </div>
  );
}