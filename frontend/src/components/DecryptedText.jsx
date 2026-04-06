import React, { useState, useEffect, useRef } from 'react';

const CHARS = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+=-{}[]|;:,.<>?';

const DecryptedText = ({ 
  text, 
  scrambleSpeed = 30, 
  className = "",
  revealSpeed = 100,
  maxIterations = 10 
}) => {
  const [displayText, setDisplayText] = useState("");
  const [isHovering, setIsHovering] = useState(false);
  const intervalRef = useRef(null);

  const startAnimation = () => {
    let iteration = 0;
    clearInterval(intervalRef.current);

    intervalRef.current = setInterval(() => {
      setDisplayText(() => 
        text
          .split("")
          .map((char, index) => {
            // Characters already "decrypted"
            if (index < iteration / maxIterations) {
              return text[index];
            }
            // Space stays as space
            if (text[index] === " ") return " ";
            // Random character for the rest
            return CHARS[Math.floor(Math.random() * CHARS.length)];
          })
          .join("")
      );

      if (iteration >= text.length * maxIterations) {
        clearInterval(intervalRef.current);
      }

      iteration += 1;
    }, scrambleSpeed);
  };

  useEffect(() => {
    // Initialize with scrambled text immediately
    setDisplayText(
      text
        .split("")
        .map((char) => (char === " " ? " " : CHARS[Math.floor(Math.random() * CHARS.length)]))
        .join("")
    );
    
    startAnimation();
    return () => clearInterval(intervalRef.current);
  }, [text]);

  const handleMouseEnter = () => {
    if (!isHovering) {
      setIsHovering(true);
      startAnimation();
    }
  };

  const handleMouseLeave = () => {
    setIsHovering(false);
  };

  return (
    <span 
      className={`inline-block font-mono ${className}`}
      onMouseEnter={handleMouseEnter}
      onMouseLeave={handleMouseLeave}
    >
      {displayText}
    </span>
  );
};

export default DecryptedText;
