import React from 'react';
import { ChevronLeft, ChevronRight, RotateCcw } from 'lucide-react';

const StepControls = ({ currentStep, totalSteps, onNext, onPrev, onReset }) => {
  return (
    <div className="space-y-6">
      {/* Progress Bar */}
      <div className="w-full h-1 bg-white/5 rounded-full overflow-hidden">
        <div 
          className="h-full bg-cyan-accent transition-all duration-500"
          style={{ width: `${((currentStep + 1) / totalSteps) * 100}%` }}
        />
      </div>

      <div className="flex items-center justify-between">
        <span className="text-xs font-mono text-gray-500 uppercase tracking-widest">
          Step {currentStep + 1} of {totalSteps}
        </span>
        
        <div className="flex gap-2">
          <button
            onClick={onPrev}
            disabled={currentStep === 0}
            className="p-2 bg-white/5 rounded-lg text-white disabled:opacity-30 hover:bg-white/10 transition-all"
          >
            <ChevronLeft size={20} />
          </button>
          
          <button
            onClick={onReset}
            className="p-2 bg-white/5 rounded-lg text-white hover:bg-white/10 transition-all"
            title="Reset"
          >
            <RotateCcw size={18} />
          </button>

          <button
            onClick={onNext}
            disabled={currentStep === totalSteps - 1}
            className="p-2 bg-cyan-accent text-dark rounded-lg disabled:opacity-30 hover:scale-105 active:scale-95 transition-all"
          >
            <ChevronRight size={20} />
          </button>
        </div>
      </div>
    </div>
  );
};

export default StepControls;
