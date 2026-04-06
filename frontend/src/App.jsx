import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AnimatePresence } from 'framer-motion';
import { Cpu } from 'lucide-react';
import PillNav from './components/PillNav';
import Footer from './components/Footer';
import CustomCursor from './components/Cursor/CustomCursor';
import Home from './pages/Home';
import Compress from './pages/Compress';
import Decompress from './pages/Decompress';
import Download from './pages/Download';

function App() {
  const navItems = [
    { label: 'Compress', href: '/compress' },
    { label: 'Decompress', href: '/decompress' },
    { label: 'Download', href: '/download' },
  ];

  return (
    <Router>
      <div className="relative min-h-screen overflow-hidden selection:bg-cyan-accent selection:text-dark">
        <CustomCursor />
        <PillNav 
          logo={<Cpu className="text-dark w-full h-full p-1" />}
          logoAlt="BytePress"
          items={navItems}
          baseColor="#00f3ff"
          pillColor="#050505"
          pillTextColor="#fff"
          hoveredPillTextColor="#050505"
        />
        <main>
          <AnimatePresence mode="wait">
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/compress" element={<Compress />} />
              <Route path="/decompress" element={<Decompress />} />
              <Route path="/download" element={<Download />} />
            </Routes>
          </AnimatePresence>
        </main>
        <Footer />
      </div>
    </Router>
  );
}

export default App;
