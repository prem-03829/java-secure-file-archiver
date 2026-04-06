import React, { useState, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { motion, AnimatePresence } from 'framer-motion';
import { Menu, X, Cpu, Download, ArrowUpCircle, ArrowDownCircle } from 'lucide-react';

const Navbar = () => {
  const [isOpen, setIsOpen] = useState(false);
  const [scrolled, setScrolled] = useState(false);
  const location = useLocation();

  useEffect(() => {
    const handleScroll = () => {
      setScrolled(window.scrollY > 20);
    };
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  const navLinks = [
    { name: 'Compress', path: '/compress', icon: <ArrowDownCircle size={18} /> },
    { name: 'Decompress', path: '/decompress', icon: <ArrowUpCircle size={18} /> },
    { name: 'Download App', path: '/download', icon: <Download size={18} /> },
  ];

  return (
    <nav className={`fixed top-0 left-0 right-0 z-50 transition-all duration-300 px-6 py-4 ${
      scrolled ? 'bg-dark/80 backdrop-blur-lg border-b border-white/10' : 'bg-transparent'
    }`}>
      <div className="max-w-7xl mx-auto flex items-center justify-between">
        <Link to="/" className="flex items-center gap-2 group">
          <div className="w-10 h-10 bg-gradient-to-br from-cyan-accent to-violet-accent rounded-lg flex items-center justify-center neon-shadow-cyan group-hover:scale-110 transition-transform">
            <Cpu className="text-white" />
          </div>
          <span className="text-xl font-bold tracking-tight text-white group-hover:text-cyan-accent transition-colors">
            BytePress
          </span>
        </Link>

        {/* Desktop Nav */}
        <div className="hidden md:flex items-center gap-8">
          {navLinks.map((link) => (
            <Link
              key={link.name}
              to={link.path}
              className={`flex items-center gap-2 text-sm font-medium transition-colors hover:text-cyan-accent ${
                location.pathname === link.path ? 'text-cyan-accent' : 'text-gray-400'
              }`}
            >
              {link.icon}
              {link.name}
              {location.pathname === link.path && (
                <motion.div
                  layoutId="activeNav"
                  className="absolute -bottom-1 left-0 right-0 h-0.5 bg-cyan-accent"
                />
              )}
            </Link>
          ))}
          <Link
            to="/compress"
            className="px-5 py-2.5 bg-white text-dark rounded-full font-semibold text-sm hover:bg-cyan-accent hover:scale-105 active:scale-95 transition-all"
          >
            Get Started
          </Link>
        </div>

        {/* Mobile Toggle */}
        <button className="md:hidden text-white" onClick={() => setIsOpen(!isOpen)}>
          {isOpen ? <X /> : <Menu />}
        </button>
      </div>

      {/* Mobile Menu */}
      <AnimatePresence>
        {isOpen && (
          <motion.div
            initial={{ opacity: 0, y: -20 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -20 }}
            className="absolute top-full left-0 right-0 bg-dark-lighter border-b border-white/10 p-6 md:hidden flex flex-col gap-4"
          >
            {navLinks.map((link) => (
              <Link
                key={link.name}
                to={link.path}
                onClick={() => setIsOpen(false)}
                className="flex items-center gap-3 text-gray-300 hover:text-cyan-accent text-lg"
              >
                {link.icon}
                {link.name}
              </Link>
            ))}
          </motion.div>
        )}
      </AnimatePresence>
    </nav>
  );
};

export default Navbar;
