import React, { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { useHuffman } from '../../hooks/useHuffman';
import InputSection from './InputSection';
import FrequencyTable from './FrequencyTable';
import TreeView from './TreeView';
import StepControls from './StepControls';
import GlassCard from '../UI/GlassCard';

const VisualizerContainer = () => {
  const [inputText, setInputText] = useState('BYTEPRESS');
  const { steps, currentStep, nextStep, prevStep, reset, activeStep, totalSteps } = useHuffman(inputText);

  return (
    <div className="w-full max-w-6xl mx-auto py-20 px-6">
      <div className="text-center mb-16">
        <motion.h2 
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          className="text-4xl md:text-5xl font-bold mb-6 text-gradient"
        >
          Interactive Huffman Demo
        </motion.h2>
        <motion.p 
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.1 }}
          className="text-gray-400 text-lg max-w-2xl mx-auto"
        >
          Watch how characters are transformed into optimized binary structures. 
          Input your own text and follow the step-by-step logic.
        </motion.p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-12 gap-8">
        {/* Controls & Input */}
        <div className="lg:col-span-4 space-y-6">
          <InputSection 
            inputText={inputText} 
            setInputText={(text) => {
              setInputText(text);
              reset();
            }} 
          />
          
          <GlassCard className="h-fit">
            <h3 className="text-xl font-bold mb-4 text-white">Current Step</h3>
            <p className="text-cyan-accent mb-6 font-medium">
              {activeStep?.description || 'Ready to begin?'}
            </p>
            <StepControls 
              currentStep={currentStep} 
              totalSteps={totalSteps} 
              onNext={nextStep} 
              onPrev={prevStep} 
              onReset={reset} 
            />
          </GlassCard>

          <AnimatePresence mode="wait">
            {activeStep?.type === 'codes' && (
              <motion.div
                initial={{ opacity: 0, x: -20 }}
                animate={{ opacity: 1, x: 0 }}
                exit={{ opacity: 0, x: -20 }}
              >
                <GlassCard>
                  <h3 className="text-xl font-bold mb-4 text-white">Resulting Codes</h3>
                  <div className="grid grid-cols-2 gap-2">
                    {Object.entries(activeStep.codes).map(([char, code]) => (
                      <div key={char} className="flex justify-between p-2 bg-white/5 rounded border border-white/10">
                        <span className="text-cyan-accent font-mono">'{char}'</span>
                        <span className="text-white font-mono">{code}</span>
                      </div>
                    ))}
                  </div>
                </GlassCard>
              </motion.div>
            )}
          </AnimatePresence>
        </div>

        {/* Visualization Area */}
        <div className="lg:col-span-8">
          <GlassCard className="min-h-[600px] flex items-center justify-center relative overflow-hidden bg-dark-lighter/50" hover={false}>
            <AnimatePresence mode="wait">
              {activeStep?.type === 'frequency' && (
                <FrequencyTable key="freq" data={activeStep.data} />
              )}
              {(activeStep?.type === 'merge' || activeStep?.type === 'queue' || activeStep?.type === 'codes') && (
                <TreeView 
                  key="tree" 
                  data={activeStep.data} 
                  type={activeStep.type}
                  activeNodes={activeStep.activeNodes}
                />
              )}
            </AnimatePresence>
            
            {/* Background Decoration */}
            <div className="absolute top-0 right-0 w-64 h-64 bg-cyan-accent/5 blur-[120px] pointer-events-none" />
            <div className="absolute bottom-0 left-0 w-64 h-64 bg-violet-accent/5 blur-[120px] pointer-events-none" />
          </GlassCard>
        </div>
      </div>
    </div>
  );
};

export default VisualizerContainer;
