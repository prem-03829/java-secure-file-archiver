import React from 'react';
import { motion, AnimatePresence } from 'framer-motion';

const TreeView = ({ data, type, activeNodes = [] }) => {
  // Simple recursive layout for the tree
  const renderNode = (node, x, y, level, parentX = null, parentY = null, direction = '') => {
    if (!node) return null;

    const isActive = activeNodes.includes(node.id);
    const spacing = 400 / (level + 1);
    
    return (
      <g key={node.id}>
        {/* Connection Line */}
        {parentX !== null && (
          <motion.line
            initial={{ pathLength: 0, opacity: 0 }}
            animate={{ pathLength: 1, opacity: 1 }}
            x1={parentX}
            y1={parentY}
            x2={x}
            y2={y}
            stroke={isActive ? '#00f3ff' : 'rgba(255,255,255,0.1)'}
            strokeWidth={isActive ? "2" : "1"}
            className="transition-colors duration-500"
          />
        )}
        
        {/* Connection Label (0 or 1) */}
        {direction && (
          <text 
            x={(parentX + x) / 2} 
            y={(parentY + y) / 2 - 10} 
            className="fill-gray-500 text-[10px] font-mono text-center"
            textAnchor="middle"
          >
            {direction}
          </text>
        )}

        {/* The Node */}
        <motion.g
          layoutId={node.id}
          initial={{ scale: 0, opacity: 0 }}
          animate={{ scale: 1, opacity: 1 }}
          transition={{ type: 'spring', damping: 20, stiffness: 200 }}
        >
          <circle
            cx={x}
            cy={y}
            r={node.char ? "24" : "18"}
            className={`${
              isActive 
                ? 'fill-cyan-accent stroke-white' 
                : 'fill-dark-lighter stroke-white/20'
            } transition-colors duration-500`}
            strokeWidth="2"
          />
          <text
            x={x}
            y={y}
            dy={node.char ? "6" : "4"}
            textAnchor="middle"
            className={`${isActive ? 'fill-dark' : 'fill-white'} font-bold ${node.char ? 'text-lg' : 'text-xs'} pointer-events-none`}
          >
            {node.char || node.freq}
          </text>
          
          {node.char && (
            <text x={x} y={y + 40} textAnchor="middle" className="fill-cyan-accent text-[10px] font-mono">
              {node.freq}
            </text>
          )}
        </motion.g>

        {/* Recursive Children */}
        {renderNode(node.left, x - spacing / 2, y + 100, level + 1, x, y, '0')}
        {renderNode(node.right, x + spacing / 2, y + 100, level + 1, x, y, '1')}
      </g>
    );
  };

  if (type === 'codes') {
    return (
      <svg width="100%" height="100%" viewBox="0 0 800 600" className="w-full h-full max-h-[600px]">
        {renderNode(data, 400, 80, 0)}
      </svg>
    );
  }

  // Queue View (Linear nodes)
  return (
    <div className="flex flex-wrap items-center justify-center gap-6 p-10">
      <AnimatePresence mode="popLayout">
        {data.map((node, index) => {
          const isActive = activeNodes.includes(node.id);
          return (
            <motion.div
              key={node.id}
              layoutId={node.id}
              initial={{ opacity: 0, scale: 0.5, y: 20 }}
              animate={{ 
                opacity: 1, 
                scale: 1, 
                y: 0,
                borderColor: isActive ? '#00f3ff' : 'rgba(255,255,255,0.1)',
                backgroundColor: isActive ? 'rgba(0, 243, 255, 0.1)' : 'rgba(17, 17, 17, 0.7)'
              }}
              exit={{ opacity: 0, scale: 0.5, y: -20 }}
              className="w-16 h-16 rounded-2xl border-2 flex flex-col items-center justify-center relative shadow-lg transition-colors"
            >
              <span className="text-white font-bold text-xl">{node.char || 'Σ'}</span>
              <span className="text-cyan-accent text-[10px] font-mono">{node.freq}</span>
              {node.isNew && (
                <div className="absolute -top-1 -right-1 w-3 h-3 bg-cyan-accent rounded-full animate-ping" />
              )}
            </motion.div>
          );
        })}
      </AnimatePresence>
    </div>
  );
};

export default TreeView;
