import React from 'react';
import { motion } from 'framer-motion';
import { ArrowRight, Zap, Shield, Maximize2, Layers, Download as DownloadIcon } from 'lucide-react';
import { Link } from 'react-router-dom';
import VisualizerContainer from '../components/HuffmanVisualizer/VisualizerContainer';
import NeonButton from '../components/UI/NeonButton';
import DecryptedText from '../components/DecryptedText';
import ShapeGrid from '../components/ShapeGrid';
import StarBorder from '../components/UI/StarBorder';
import GlassCard from '../components/UI/GlassCard';

const Home = () => {
  const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: { staggerChildren: 0.2 }
    }
  };

  const itemVariants = {
    hidden: { opacity: 0, y: 20 },
    visible: { opacity: 1, y: 0 }
  };

  const features = [
    { 
      icon: <Zap size={24} className="text-cyan-accent" />, 
      title: "LZ77 + Huffman", 
      desc: "Dual-pass hybrid engine combining sliding window dictionaries with frequency coding."
    },
    { 
      icon: <Shield size={24} className="text-cyan-accent" />, 
      title: "Lossless Trust", 
      desc: "Bit-perfect reconstruction using Canonical Huffman tables for 100% data fidelity."
    },
    { 
      icon: <Maximize2 size={24} className="text-cyan-accent" />, 
      title: "Smart Scaling", 
      desc: "Intelligent frequency analysis that adapts to any binary or text file structure."
    },
    { 
      icon: <Layers size={24} className="text-cyan-accent" />, 
      title: "Java Backend", 
      desc: "High-performance multi-threaded core for enterprise-grade throughput."
    }
  ];

  return (
    <motion.div 
      initial="hidden"
      animate="visible"
      variants={containerVariants}
      className="pt-32"
    >
      {/* Hero Section */}
      <section className="relative mb-32 min-h-screen flex items-center -mt-32">
        <div className="absolute inset-0 z-0">
          <ShapeGrid 
            speed={0.5}
            squareSize={40}
            direction="diagonal"
            borderColor="rgba(0, 243, 255, 0.1)"
            hoverFillColor="rgba(0, 243, 255, 0.3)"
            shape="square"
            hoverTrailAmount={0}
          />
        </div>
        <div className="max-w-7xl mx-auto px-6 flex flex-col items-center text-center relative z-10 w-full">
          <motion.div 
            variants={itemVariants}
            className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-white/5 border border-white/10 text-cyan-accent text-xs font-bold uppercase tracking-widest mb-8"
          >
            <span className="w-2 h-2 rounded-full bg-cyan-accent animate-pulse" />
            Empowering the Future of Storage
          </motion.div>
          
          <motion.h1 
            variants={itemVariants}
            className="text-6xl md:text-8xl font-extrabold mb-8 tracking-tighter"
          >
            Compress Intelligence <br />
            <span className="text-gradient">
              into <DecryptedText text="Every Byte" />
            </span>
          </motion.h1>
          
          <motion.p 
            variants={itemVariants}
            className="text-gray-400 text-lg md:text-xl max-w-2xl mb-12 leading-relaxed"
          >
            Experience the pinnacle of data efficiency with BytePress. 
            Our hybrid LZ77 and Canonical Huffman implementation delivers 
            lossless compression with a premium, high-performance interface.
          </motion.p>
          
          <motion.div variants={itemVariants} className="flex flex-row flex-wrap justify-center gap-4">
            <Link to="/compress">
              <NeonButton variant="cyan" className="px-8 py-3">
                Start Compressing <ArrowRight size={18} className="ml-2" />
              </NeonButton>
            </Link>
            <Link to="/download">
              <StarBorder as="div" color="#00f3ff" speed="5s">
                <span className="flex items-center gap-2 px-6 py-1">
                   Desktop App <DownloadIcon size={16} />
                </span>
              </StarBorder>
            </Link>
          </motion.div>
        </div>
      </section>

      {/* Centerpiece: Visualizer */}
      <section className="bg-dark-lighter py-10">
        <VisualizerContainer />
      </section>

      {/* Features Grid */}
      <section className="max-w-7xl mx-auto px-6 py-32">
        <div className="text-center mb-20">
          <h2 className="text-4xl font-bold mb-4">Unrivaled Performance</h2>
          <p className="text-gray-400">Why BytePress leads the industry in data efficiency.</p>
        </div>
        
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          {features.map((feature, i) => (
            <GlassCard key={i} className="flex flex-col gap-6">
              <div className="w-12 h-12 flex items-center justify-center bg-white/5 border border-white/10 rounded-xl">
                {feature.icon}
              </div>
              <div>
                <h3 className="text-xl font-bold text-white mb-2">{feature.title}</h3>
                <p className="text-gray-400 text-sm leading-relaxed">{feature.desc}</p>
              </div>
            </GlassCard>
          ))}
        </div>
      </section>

      {/* Tech Stack Banner */}
      <section className="border-y border-white/10 bg-white/5 py-12 px-6">
        <div className="max-w-7xl mx-auto flex flex-wrap justify-center items-center gap-12 md:gap-24 opacity-50 grayscale hover:grayscale-0 transition-all duration-700">
          <span className="text-2xl font-black italic tracking-widest">REACT</span>
          <span className="text-2xl font-black italic tracking-widest">TAILWIND</span>
          <span className="text-2xl font-black italic tracking-widest">FRAMER</span>
          <span className="text-2xl font-black italic tracking-widest">LUCIDE</span>
          <span className="text-2xl font-black italic tracking-widest">VITE</span>
        </div>
      </section>
    </motion.div>
  );
};

export default Home;
