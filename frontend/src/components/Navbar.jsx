import { Link } from "react-router-dom";

export default function Navbar() {
  return (
    <nav className="bg-slate-900/80 backdrop-blur-md border-b border-white/10 sticky top-0 z-50">
      <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          <div className="flex items-center">
            <Link to="/" className="font-bold text-xl tracking-tight bg-clip-text text-transparent bg-gradient-to-r from-blue-400 to-indigo-500 flex items-center">
              <span className="mr-2 text-2xl">🛡️</span> Compliance Manager
            </Link>
            <div className="hidden md:block ml-10">
              <div className="flex items-baseline space-x-2">
                <Link to="/" className="px-3 py-2 rounded-lg text-sm font-medium text-slate-300 hover:text-white hover:bg-white/5 transition-colors">Dashboard</Link>
                <Link to="/list" className="px-3 py-2 rounded-lg text-sm font-medium text-slate-300 hover:text-white hover:bg-white/5 transition-colors">Trainings</Link>
                <Link to="/create" className="px-3 py-2 rounded-lg text-sm font-medium text-blue-400 hover:text-blue-300 hover:bg-blue-500/10 transition-colors">+ New</Link>
              </div>
            </div>
          </div>
          <div>
            <Link to="/login" className="px-4 py-2 text-sm font-medium border border-slate-700 text-slate-300 rounded-lg hover:bg-slate-800 hover:text-white transition-colors">Login</Link>
          </div>
        </div>
      </div>
    </nav>
  );
}