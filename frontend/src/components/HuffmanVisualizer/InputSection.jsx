import React from 'react';
import GlassCard from '../UI/GlassCard';

const InputSection = ({ inputText, setInputText }) => {
  const presets = ['BYTEPRESS', 'HUFFMAN', 'SAAS', 'COMPRESS'];

  return (
    <GlassCard>
      <h3 className="text-xl font-bold mb-4 text-white">Input String</h3>
      <div className="relative mb-6">
        <input
          type="text"
          maxLength={15}
          value={inputText}
          onChange={(e) => setInputText(e.target.value.toUpperCase())}
          className="w-full bg-white/5 border border-white/10 rounded-lg px-4 py-3 text-white focus:outline-none focus:border-cyan-accent/50 transition-colors uppercase font-mono tracking-widest"
          placeholder="ENTER TEXT..."
        />
        <div className="absolute right-3 top-3 text-xs text-gray-500 font-mono">
          {inputText.length}/15
        </div>
      </div>
      
      <div className="flex flex-wrap gap-2">
        {presets.map((preset) => (
          <button
            key={preset}
            onClick={() => setInputText(preset)}
            className={`px-3 py-1 rounded-full text-xs font-semibold transition-all ${
              inputText === preset 
                ? 'bg-cyan-accent text-dark' 
                : 'bg-white/5 text-gray-400 hover:bg-white/10 hover:text-white'
            }`}
          >
            {preset}
          </button>
        ))}
      </div>
    </GlassCard>
  );
};

export default InputSection;
