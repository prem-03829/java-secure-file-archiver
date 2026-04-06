import React from 'react';
import { motion } from 'framer-motion';

const NeonButton = ({ 
  children, 
  onClick, 
  variant = 'cyan', 
  className = '', 
  disabled = false,
  type = 'button'
}) => {
  const styles = {
    cyan: 'from-cyan-accent to-blue-500 neon-shadow-cyan text-dark',
    violet: 'from-violet-accent to-purple-500 neon-shadow-violet text-white',
    outline: 'border border-white/20 hover:border-cyan-accent text-white hover:text-cyan-accent bg-white/5'
  };

  return (
    <motion.button
      type={type}
      disabled={disabled}
      whileHover={{ scale: 1.05, boxShadow: '0 0 25px rgba(0, 243, 255, 0.5)' }}
      whileTap={{ scale: 0.95 }}
      onClick={onClick}
      className={`relative px-8 py-3 rounded-full font-bold text-sm transition-all overflow-hidden flex items-center justify-center gap-2 ${
        variant === 'outline' ? styles.outline : `bg-gradient-to-r ${styles[variant]}`
      } ${disabled ? 'opacity-50 cursor-not-allowed grayscale' : ''} ${className}`}
    >
      <span className="relative z-10">{children}</span>
    </motion.button>
  );
};

export default NeonButton;
