import React from 'react';
import { motion } from 'framer-motion';

const GlassCard = ({ children, className = '', hover = true }) => {
  return (
    <motion.div
      whileHover={hover ? { y: -5, scale: 1.02, rotateX: 2, rotateY: 2 } : {}}
      className={`glass-card p-6 ${className}`}
      transition={{ type: 'spring', stiffness: 300, damping: 20 }}
    >
      {children}
    </motion.div>
  );
};

export default GlassCard;
