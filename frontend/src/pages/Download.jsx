import React from 'react';
import { motion } from 'framer-motion';
import { Download as DownloadIcon, Monitor, Terminal, Cpu, ShieldCheck, Zap } from 'lucide-react';
import NeonButton from '../components/UI/NeonButton';
import GlassCard from '../components/UI/GlassCard';

const Download = () => {
  return (
    <div className="pt-32 pb-20 px-6 min-h-screen">
      <div className="max-w-7xl mx-auto">
        {/* Hero Section */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-16 items-center mb-32">
          <div>
            <motion.div 
              initial={{ opacity: 0, x: -20 }}
              animate={{ opacity: 1, x: 0 }}
              className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-cyan-accent/10 border border-cyan-accent/20 text-cyan-accent text-xs font-bold uppercase tracking-widest mb-6"
            >
              Desktop Application
            </motion.div>
            <motion.h1 
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              className="text-5xl md:text-7xl font-bold mb-8 tracking-tight"
            >
              BytePress <span className="text-gradient">Desktop</span>
            </motion.h1>
            <motion.p 
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.1 }}
              className="text-gray-400 text-lg mb-10 leading-relaxed"
            >
              Take the power of BytePress to your local machine. 
              Our Java-based GUI application offers blazing fast batch processing 
              and deeper integration with your file system.
            </motion.p>
            
            <div className="flex flex-row flex-wrap gap-4">
              <a href="/SecureArchiver.zip" download>
                <NeonButton variant="cyan" className="px-8 py-4">
                  <DownloadIcon size={20} className="mr-2" /> Download .EXE (Windows)
                </NeonButton>
              </a>
              <a href="/BytePress.jar" download>
                <NeonButton variant="violet" className="px-8 py-4">
                  <DownloadIcon size={20} className="mr-2" /> Download .JAR
                </NeonButton>
              </a>
              <div className="flex items-center gap-3 px-4 py-2 text-gray-400 text-sm">
                <ShieldCheck className="text-green-500" size={18} /> Verified Build v2.4.0
              </div>
            </div>
          </div>

          <div className="relative">
            <motion.div
              initial={{ opacity: 0, scale: 0.8, rotate: 5 }}
              animate={{ opacity: 1, scale: 1, rotate: 0 }}
              className="relative z-10"
            >
              <GlassCard className="aspect-video bg-dark-lighter border-white/20 p-2 shadow-2xl">
                <div className="w-full h-full bg-dark rounded-lg overflow-hidden border border-white/5 flex flex-col">
                  {/* Mock UI Toolbar */}
                  <div className="h-8 bg-white/5 border-b border-white/5 flex items-center px-4 gap-1.5">
                    <div className="w-2.5 h-2.5 rounded-full bg-red-500/50" />
                    <div className="w-2.5 h-2.5 rounded-full bg-yellow-500/50" />
                    <div className="w-2.5 h-2.5 rounded-full bg-green-500/50" />
                  </div>
                  {/* Mock App Content */}
                  <div className="flex-1 p-8 flex flex-col items-center justify-center gap-4 text-center">
                    <div className="w-24 h-24 bg-gradient-to-br from-cyan-accent/20 to-violet-accent/20 rounded-3xl flex items-center justify-center border border-white/10">
                      <Cpu size={48} className="text-cyan-accent" />
                    </div>
                    <div>
                      <h4 className="font-bold text-white">BytePress Desktop</h4>
                      <p className="text-xs text-gray-500">Awaiting file input...</p>
                    </div>
                  </div>
                </div>
              </GlassCard>
            </motion.div>
            
            {/* Decoration Circles */}
            <div className="absolute -top-10 -right-10 w-40 h-40 bg-cyan-accent/20 blur-[80px] -z-10" />
            <div className="absolute -bottom-10 -left-10 w-40 h-40 bg-violet-accent/20 blur-[80px] -z-10" />
          </div>
        </div>

        {/* Requirements & Info */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          {[
            {
              icon: <Terminal className="text-cyan-accent" />,
              title: "Java Required",
              desc: "Ensure OpenJDK 17 or higher is installed on your system to run the application."
            },
            {
              icon: <Monitor className="text-violet-accent" />,
              title: "Cross-Platform",
              desc: "Compatible with Windows, macOS (M1/M2/Intel), and all major Linux distributions."
            },
            {
              icon: <Zap className="text-yellow-400" />,
              title: "Batch Engine",
              desc: "Simultaneously compress hundreds of files with our multi-threaded processing engine."
            }
          ].map((item, i) => (
            <GlassCard key={i} className="space-y-4">
              <div className="p-3 w-fit bg-white/5 rounded-xl">{item.icon}</div>
              <h3 className="text-xl font-bold text-white">{item.title}</h3>
              <p className="text-gray-400 text-sm leading-relaxed">{item.desc}</p>
            </GlassCard>
          ))}
        </div>

        {/* Installation Instructions */}
        <div className="mt-32 max-w-3xl">
          <h2 className="text-3xl font-bold mb-8">Installation Guide</h2>
          <div className="space-y-8">
            {[
              { step: "01", title: "Download", text: "Choose the .EXE for Windows (portable) or the .JAR for cross-platform usage." },
              { step: "02", title: "Environment", text: "Windows users can run the EXE directly. JAR users require OpenJDK 17 installed." },
              { step: "03", title: "Execute", text: "Extract the ZIP and run 'SecureArchiver.exe' or use 'java -jar BytePress.jar'." }
            ].map((item, i) => (
              <div key={i} className="flex gap-6">
                <span className="text-4xl font-black text-white/10 font-mono">{item.step}</span>
                <div>
                  <h4 className="text-lg font-bold text-white mb-2">{item.title}</h4>
                  <p className="text-gray-400 text-sm">{item.text}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Download;
