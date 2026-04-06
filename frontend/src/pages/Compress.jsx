import React, { useState, useCallback } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { Upload, File, X, Download, BarChart3, Clock, CheckCircle } from 'lucide-react';
import NeonButton from '../components/UI/NeonButton';
import GlassCard from '../components/UI/GlassCard';
import StarBorder from '../components/UI/StarBorder';
import { compressFile } from '../services/api';

const Compress = () => {
  const [file, setFile] = useState(null);
  const [isCompressing, setIsCompressing] = useState(false);
  const [progress, setProgress] = useState(0);
  const [result, setResult] = useState(null);
  const [downloadUrl, setDownloadUrl] = useState(null);

  const handleFileChange = (e) => {
    const selectedFile = e.target.files[0];
    if (selectedFile) {
      setFile(selectedFile);
      setResult(null);
      setDownloadUrl(null);
    }
  };

  const handleCompress = async () => {
    if (!file) return;
    
    setIsCompressing(true);
    setProgress(20); // Initial progress
    
    try {
      const startTime = Date.now();
      const compressedBlob = await compressFile(file);
      const endTime = Date.now();
      
      // FIXED: Release memory by revoking previous URLs
      if (downloadUrl) window.URL.revokeObjectURL(downloadUrl);

      const url = window.URL.createObjectURL(compressedBlob);
      setDownloadUrl(url);
      
      const originalSize = file.size;
      const compressedSize = compressedBlob.size;
      const reduction = ((originalSize - compressedSize) / originalSize * 100).toFixed(1);
      
      setResult({
        originalSize: (originalSize / 1024).toFixed(2),
        compressedSize: (compressedSize / 1024).toFixed(2),
        reduction: reduction,
        time: ((endTime - startTime) / 1000).toFixed(2)
      });
      setProgress(100);
    } catch (error) {
      console.error('Compression failed:', error);
      alert('Compression failed. Make sure the backend server is running.');
    } finally {
      setIsCompressing(false);
    }
  };

  const handleDownload = () => {
    if (downloadUrl) {
      const link = document.createElement('a');
      link.href = downloadUrl;
      link.setAttribute('download', `${file.name}.huff`);
      document.body.appendChild(link);
      link.click();
      link.remove();
    }
  };

  return (
    <div className="pt-32 pb-20 px-6 min-h-screen">
      <div className="max-w-4xl mx-auto">
        <div className="text-center mb-12">
          <h1 className="text-4xl md:text-5xl font-bold mb-4">Compress Your Data</h1>
          <p className="text-gray-400">Drag and drop any file to optimize its storage footprint.</p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          {/* Upload Area */}
          <div className="md:col-span-2 space-y-6">
            <GlassCard className="h-full flex flex-col items-center justify-center min-h-[400px] border-dashed border-2 border-white/10 hover:border-cyan-accent/50 transition-all drag-zone relative">
              <input 
                type="file" 
                onChange={handleFileChange}
                className="absolute inset-0 w-full h-full opacity-0 cursor-pointer"
              />
              
              {!file ? (
                <div className="text-center pointer-events-none">
                  <div className="w-20 h-20 bg-white/5 rounded-full flex items-center justify-center mx-auto mb-6 border border-white/10">
                    <Upload className="text-cyan-accent" size={32} />
                  </div>
                  <h3 className="text-xl font-bold mb-2">Select a File</h3>
                  <p className="text-gray-500 text-sm">PDF, TXT, DOCX, or Images up to 50MB</p>
                </div>
              ) : (
                <div className="text-center w-full px-12">
                  <div className="w-16 h-16 bg-cyan-accent/10 rounded-xl flex items-center justify-center mx-auto mb-4">
                    <File className="text-cyan-accent" size={28} />
                  </div>
                  <h3 className="text-lg font-bold mb-1 truncate">{file.name}</h3>
                  <p className="text-gray-500 text-sm mb-8">{(file.size / 1024).toFixed(2)} KB</p>
                  
                  {isCompressing ? (
                    <div className="w-full max-w-xs mx-auto">
                      <div className="flex justify-between text-xs mb-2">
                        <span className="text-cyan-accent font-bold">Compressing...</span>
                        <span className="text-white">{progress}%</span>
                      </div>
                      <div className="w-full h-2 bg-white/5 rounded-full overflow-hidden">
                        <motion.div 
                          className="h-full bg-cyan-accent"
                          initial={{ width: 0 }}
                          animate={{ width: `${progress}%` }}
                        />
                      </div>
                    </div>
                  ) : result ? (
                    <div className="flex flex-col items-center gap-4">
                      <div className="flex items-center gap-2 text-green-400 font-bold">
                        <CheckCircle size={20} /> Compression Complete
                      </div>
                      <NeonButton variant="cyan" onClick={handleDownload}>
                        <Download size={18} /> Download .huff File
                      </NeonButton>
                    </div>
                  ) : (
                    <div className="flex flex-col items-center gap-4">
                      <StarBorder onClick={handleCompress} color="#00f3ff" speed="4s">
                        Start Compression
                      </StarBorder>
                      <button 
                        onClick={() => setFile(null)}
                        className="text-gray-500 hover:text-white text-sm flex items-center gap-1"
                      >
                        <X size={14} /> Clear
                      </button>
                    </div>
                  )}
                </div>
              )}
            </GlassCard>
          </div>

          {/* Stats / Sidebar */}
          <div className="space-y-6">
            <GlassCard>
              <h3 className="text-lg font-bold mb-6 flex items-center gap-2">
                <BarChart3 className="text-cyan-accent" size={20} />
                Insights
              </h3>
              
              <div className="space-y-6">
                <div>
                  <p className="text-gray-500 text-xs uppercase tracking-widest mb-1">Algorithm</p>
                  <p className="text-white font-medium">Huffman Adaptive</p>
                </div>
                <div>
                  <p className="text-gray-500 text-xs uppercase tracking-widest mb-1">Security</p>
                  <p className="text-white font-medium">End-to-End Encrypted</p>
                </div>
                <div>
                  <p className="text-gray-500 text-xs uppercase tracking-widest mb-1">Status</p>
                  <div className="flex items-center gap-2">
                    <span className="w-2 h-2 rounded-full bg-green-500" />
                    <span className="text-white font-medium text-sm">Ready to Optimize</span>
                  </div>
                </div>
              </div>
            </GlassCard>

            <AnimatePresence>
              {result && (
                <motion.div
                  initial={{ opacity: 0, scale: 0.9, y: 20 }}
                  animate={{ opacity: 1, scale: 1, y: 0 }}
                >
                  <GlassCard className="bg-cyan-accent/5 border-cyan-accent/20">
                    <h3 className="text-lg font-bold mb-6 text-cyan-accent">Results</h3>
                    <div className="space-y-4">
                      <div className="flex justify-between">
                        <span className="text-gray-400 text-sm">Original</span>
                        <span className="text-white font-mono">{result.originalSize} KB</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-gray-400 text-sm">Compressed</span>
                        <span className="text-white font-mono">{result.compressedSize} KB</span>
                      </div>
                      <div className="pt-4 border-t border-white/5 flex justify-between items-end">
                        <span className="text-cyan-accent text-sm font-bold">Reduction</span>
                        <span className="text-3xl font-black text-cyan-accent">-{result.reduction}%</span>
                      </div>
                      <div className="flex items-center gap-2 text-xs text-gray-500 mt-2">
                        <Clock size={12} /> Time: {result.time}s
                      </div>
                    </div>
                  </GlassCard>
                </motion.div>
              )}
            </AnimatePresence>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Compress;
