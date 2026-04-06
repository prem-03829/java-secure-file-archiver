import React, { useState, useRef } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { Archive, FileText, X, RefreshCw, Layers, CheckCircle, Download } from 'lucide-react';
import NeonButton from '../components/UI/NeonButton';
import GlassCard from '../components/UI/GlassCard';
import StarBorder from '../components/UI/StarBorder';
import GlareHover from '../components/UI/GlareHover';
import ShapeGrid from '../components/ShapeGrid';
import { decompressFile } from '../services/api';

const Decompress = () => {
  const [file, setFile] = useState(null);
  const [isDecompressing, setIsDecompressing] = useState(false);
  const [progress, setProgress] = useState(0);
  const [isDone, setIsDone] = useState(false);
  const [downloadUrl, setDownloadUrl] = useState(null);
  const abortControllerRef = useRef(null);

  const handleFileChange = (e) => {
    const selectedFile = e.target.files[0];
    if (selectedFile) {
      setFile(selectedFile);
      setIsDone(false);
      setDownloadUrl(null);
    }
  };

  const handleCancel = () => {
    if (abortControllerRef.current) {
      abortControllerRef.current.abort();
    }
    setIsDecompressing(false);
    setProgress(0);
    setFile(null);
    setIsDone(false);
  };

  const handleDecompress = async () => {
    if (!file) return;
    setIsDecompressing(true);
    setProgress(20);
    
    abortControllerRef.current = new AbortController();

    try {
      const decompressedBlob = await decompressFile(file, abortControllerRef.current.signal);
      
      // FIXED: Release memory by revoking previous URLs
      if (downloadUrl) window.URL.revokeObjectURL(downloadUrl);

      const url = window.URL.createObjectURL(decompressedBlob);
      setDownloadUrl(url);
      setProgress(100);
      setIsDone(true);
    } catch (error) {
      if (error.name === 'AbortError') {
        console.log('Decompression aborted');
      } else {
        console.error('Decompression failed:', error);
        alert('Decompression failed. Make sure the backend server is running.');
      }
    } finally {
      setIsDecompressing(false);
      abortControllerRef.current = null;
    }
  };

  const handleDownload = () => {
    if (downloadUrl) {
      const link = document.createElement('a');
      link.href = downloadUrl;
      const originalName = file.name.replace('.huff', '');
      link.setAttribute('download', `${originalName}.decoded.txt`);
      document.body.appendChild(link);
      link.click();
      link.remove();
    }
  };

  return (
    <div className="pt-32 pb-20 px-6 min-h-screen relative overflow-hidden">
      {/* Background Section */}
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

      <div className="max-w-4xl mx-auto relative z-10">
        <div className="text-center mb-12">
          <h1 className="text-4xl md:text-5xl font-bold mb-4 text-white">Restore Your Files</h1>
          <p className="text-gray-400">Upload a .huff or compressed archive to decompress instantly.</p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-12 gap-8">
          <div className="md:col-span-8">
            <GlareHover
              glareColor="#7000ff"
              glareOpacity={0.2}
              glareAngle={-30}
              glareSize={200}
              className="h-full"
            >
              <GlassCard className="min-h-[400px] flex flex-col items-center justify-center border-dashed border-2 border-white/10 relative">
                <input 
                  type="file" 
                  onChange={handleFileChange}
                  className="absolute inset-0 w-full h-full opacity-0 cursor-pointer z-20"
                />
                
                {!file ? (
                  <div className="text-center pointer-events-none">
                    <div className="w-20 h-20 bg-white/5 rounded-full flex items-center justify-center mx-auto mb-6 border border-white/10">
                      <Archive className="text-violet-accent" size={32} />
                    </div>
                    <h3 className="text-xl font-bold mb-2 text-white">Upload Compressed File</h3>
                    <p className="text-gray-500 text-sm">Drag and drop your .bp file here</p>
                  </div>
                ) : (
                  <div className="text-center w-full px-12 relative z-30">
                    <motion.div 
                      animate={isDecompressing ? { rotate: 360 } : {}}
                      transition={{ repeat: Infinity, duration: 2, ease: "linear" }}
                      className="w-16 h-16 bg-violet-accent/10 rounded-xl flex items-center justify-center mx-auto mb-4"
                    >
                      <Layers className="text-violet-accent" size={28} />
                    </motion.div>
                    <h3 className="text-lg font-bold mb-1 text-white">{file.name}</h3>
                    <p className="text-gray-500 text-sm mb-8">Target: Original Quality</p>

                    {isDecompressing ? (
                      <div className="w-full max-w-xs mx-auto">
                        <div className="w-full h-1.5 bg-white/5 rounded-full overflow-hidden mb-4">
                          <motion.div 
                            className="h-full bg-violet-accent"
                            initial={{ width: 0 }}
                            animate={{ width: `${progress}%` }}
                          />
                        </div>
                        <p className="text-xs text-gray-500 mb-6 font-mono uppercase">RECONSTRUCTING BITS... {progress}%</p>
                        <button 
                          onClick={handleCancel}
                          className="text-gray-500 hover:text-white text-sm flex items-center gap-1 mx-auto"
                        >
                          <X size={14} /> Cancel
                        </button>
                      </div>
                    ) : isDone ? (
                      <motion.div 
                        initial={{ scale: 0.9, opacity: 0 }}
                        animate={{ scale: 1, opacity: 1 }}
                        className="flex flex-col items-center gap-4"
                      >
                        <div className="flex items-center gap-2 text-green-400 font-bold">
                          <CheckCircle size={20} /> Restoration Successful
                        </div>
                        <NeonButton variant="violet" onClick={handleDownload}>
                          Download Restored File
                        </NeonButton>
                        <button 
                          onClick={handleCancel}
                          className="text-gray-500 hover:text-white text-sm flex items-center gap-1"
                        >
                          <X size={14} /> Clear
                        </button>
                      </motion.div>
                    ) : (
                      <div className="flex flex-col items-center gap-4">
                        <StarBorder onClick={handleDecompress} color="#7000ff" speed="4s">
                          Start Decompression
                        </StarBorder>
                        <button 
                          onClick={handleCancel}
                          className="text-gray-500 hover:text-white text-sm flex items-center gap-1"
                        >
                          <X size={14} /> Cancel
                        </button>
                      </div>
                    )}
                  </div>
                )}
              </GlassCard>
            </GlareHover>
          </div>

          <div className="md:col-span-4 space-y-6">
            <GlassCard>
              <h3 className="text-lg font-bold mb-4 flex items-center gap-2 text-violet-accent">
                <RefreshCw size={18} /> Process
              </h3>
              <p className="text-gray-400 text-sm leading-relaxed">
                Our decompressor reads the embedded Huffman Tree from your file header 
                and reconstructs the original bitstream with 100% fidelity.
              </p>
            </GlassCard>

            <GlassCard className="bg-gradient-to-br from-violet-accent/10 to-transparent border-violet-accent/20">
              <h3 className="text-lg font-bold mb-4">Integrity Check</h3>
              <ul className="space-y-3">
                <li className="flex items-center gap-2 text-xs text-gray-400">
                  <div className="w-1.5 h-1.5 rounded-full bg-green-500" />
                  CRC32 Validation
                </li>
                <li className="flex items-center gap-2 text-xs text-gray-400">
                  <div className="w-1.5 h-1.5 rounded-full bg-green-500" />
                  Header Verification
                </li>
                <li className="flex items-center gap-2 text-xs text-gray-400">
                  <div className="w-1.5 h-1.5 rounded-full bg-green-500" />
                  Bitstream Alignment
                </li>
              </ul>
            </GlassCard>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Decompress;
