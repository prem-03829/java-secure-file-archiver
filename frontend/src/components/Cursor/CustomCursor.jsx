import React, { useEffect, useState } from 'react';
import { motion, useSpring } from 'framer-motion';

const CustomCursor = () => {
  const [isMobile, setIsMobile] = useState(false);
  const [cursorState, setCursorState] = useState('default');
  
  const mouseX = useSpring(0, { damping: 30, stiffness: 200 });
  const mouseY = useSpring(0, { damping: 30, stiffness: 200 });

  useEffect(() => {
    const checkMobile = () => {
      setIsMobile(window.innerWidth <= 768);
    };
    checkMobile();
    window.addEventListener('resize', checkMobile);

    const handleMouseMove = (e) => {
      mouseX.set(e.clientX);
      mouseY.set(e.clientY);
    };

    const handleMouseOver = (e) => {
      const target = e.target;
      if (
        target.tagName === 'A' || 
        target.tagName === 'BUTTON' || 
        target.closest('button') || 
        target.closest('a') ||
        target.classList.contains('cursor-pointer')
      ) {
        setCursorState('hover');
      } else if (target.closest('.drag-zone')) {
        setCursorState('drag');
      } else {
        setCursorState('default');
      }
    };

    const handleMouseDown = () => setCursorState('click');
    const handleMouseUp = () => setCursorState('default');

    window.addEventListener('mousemove', handleMouseMove);
    window.addEventListener('mouseover', handleMouseOver);
    window.addEventListener('mousedown', handleMouseDown);
    window.addEventListener('mouseup', handleMouseUp);

    return () => {
      window.removeEventListener('resize', checkMobile);
      window.removeEventListener('mousemove', handleMouseMove);
      window.removeEventListener('mouseover', handleMouseOver);
      window.removeEventListener('mousedown', handleMouseDown);
      window.removeEventListener('mouseup', handleMouseUp);
    };
  }, [mouseX, mouseY]);

  if (isMobile) return null;

  const cursorVariants = {
    default: {
      scale: 1,
      backgroundColor: 'rgba(0, 243, 255, 0.3)',
      border: '1px solid rgba(0, 243, 255, 0.5)',
    },
    hover: {
      scale: 2,
      backgroundColor: 'rgba(112, 0, 255, 0.4)',
      border: '1px solid rgba(112, 0, 255, 0.8)',
    },
    click: {
      scale: 0.8,
      backgroundColor: 'rgba(255, 255, 255, 0.8)',
      border: 'none',
    },
    drag: {
      scale: 1.5,
      backgroundColor: 'rgba(0, 243, 255, 0.1)',
      border: '2px dashed rgba(0, 243, 255, 0.8)',
    }
  };

  return (
    <>
      {/* Main Cursor Dot */}
      <motion.div
        className="fixed top-0 left-0 w-4 h-4 rounded-full pointer-events-none z-[9999] flex items-center justify-center"
        style={{ x: mouseX, y: mouseY, translateX: '-50%', translateY: '-50%' }}
        animate={cursorState}
        variants={cursorVariants}
        transition={{ type: 'spring', damping: 20, stiffness: 250 }}
      />
      
      {/* Trailing Glow */}
      <motion.div
        className="fixed top-0 left-0 w-24 h-24 rounded-full pointer-events-none z-[9998] blur-2xl opacity-20"
        style={{ 
          x: mouseX, 
          y: mouseY, 
          translateX: '-50%', 
          translateY: '-50%',
          backgroundColor: cursorState === 'hover' ? '#7000ff' : '#00f3ff'
        }}
        transition={{ type: 'spring', damping: 30, stiffness: 150 }}
      />
    </>
  );
};

export default CustomCursor;
