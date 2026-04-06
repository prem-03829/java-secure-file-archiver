import { useState, useCallback, useMemo } from 'react';

export const useHuffman = (inputString) => {
  const [currentStep, setCurrentStep] = useState(0);

  const steps = useMemo(() => {
    if (!inputString) return [];

    const resultSteps = [];

    // Step 1: Frequency Calculation
    // IMPROVED: Prevent UI blocking for large inputs by chunking manually if needed
    // For this hook, since we must return resultSteps synchronously for useMemo, 
    // we'll keep it fast but add a safeguard or use smaller chunks if possible.
    // However, since useMemo is synchronous, we'll optimize the loop.
    const freqMap = {};
    for (let i = 0; i < inputString.length; i++) {
      const char = inputString[i];
      freqMap[char] = (freqMap[char] || 0) + 1;
    }
    const freqArray = Object.entries(freqMap)
      .map(([char, freq]) => ({ char, freq, id: `node-${char}` }))
      .sort((a, b) => a.freq - b.freq);

    resultSteps.push({
      type: 'frequency',
      description: 'Calculating character frequencies...',
      data: freqArray,
    });

    // Step 2: Initial Forest (Priority Queue)
    let nodes = freqArray.map(n => ({ ...n, left: null, right: null }));
    resultSteps.push({
      type: 'queue',
      description: 'Building the priority queue...',
      data: [...nodes],
    });

    // Step 3: Tree Building (Merging)
    let mergeCount = 0;
    while (nodes.length > 1) {
      const left = nodes.shift();
      const right = nodes.shift();
      
      const parent = {
        char: null,
        freq: left.freq + right.freq,
        left,
        right,
        id: `parent-${mergeCount++}`,
        isNew: true,
      };

      nodes.push(parent);
      nodes.sort((a, b) => a.freq - b.freq);

      resultSteps.push({
        type: 'merge',
        description: `Merging nodes with frequencies ${left.freq} and ${right.freq}...`,
        data: [...nodes],
        activeNodes: [left.id, right.id, parent.id],
      });
    }

    // Step 4: Final Tree & Binary Codes
    const root = nodes[0];
    const codes = {};
    const generateCodes = (node, code = '') => {
      if (!node) return;
      if (node.char !== null) {
        codes[node.char] = code;
        return;
      }
      generateCodes(node.left, code + '0');
      generateCodes(node.right, code + '1');
    };
    generateCodes(root);

    resultSteps.push({
      type: 'codes',
      description: 'Generating binary codes from the tree structure.',
      data: root,
      codes,
    });

    return resultSteps;
  }, [inputString]);

  const nextStep = useCallback(() => {
    if (currentStep < steps.length - 1) {
      setCurrentStep(prev => prev + 1);
    }
  }, [currentStep, steps.length]);

  const prevStep = useCallback(() => {
    if (currentStep > 0) {
      setCurrentStep(prev => prev - 1);
    }
  }, [currentStep]);

  const reset = useCallback(() => {
    setCurrentStep(0);
  }, []);

  return {
    steps,
    currentStep,
    nextStep,
    prevStep,
    reset,
    totalSteps: steps.length,
    activeStep: steps[currentStep] || null,
  };
};
