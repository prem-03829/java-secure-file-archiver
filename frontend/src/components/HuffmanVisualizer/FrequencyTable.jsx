import React from 'react';
import { motion } from 'framer-motion';

const FrequencyTable = ({ data }) => {
  return (
    <div className="w-full max-w-lg grid grid-cols-2 md:grid-cols-3 gap-4">
      {data.map((item, index) => (
        <motion.div
          key={item.char}
          initial={{ opacity: 0, scale: 0.8, y: 20 }}
          animate={{ opacity: 1, scale: 1, y: 0 }}
          transition={{ delay: index * 0.05 }}
          className="bg-white/5 border border-white/10 p-4 rounded-xl text-center hover:border-cyan-accent/50 transition-colors"
        >
          <div className="text-3xl font-bold text-white mb-2 font-mono">
            {item.char}
          </div>
          <div className="text-sm text-cyan-accent font-medium">
            Freq: {item.freq}
          </div>
        </motion.div>
      ))}
    </div>
  );
};

export default FrequencyTable;
