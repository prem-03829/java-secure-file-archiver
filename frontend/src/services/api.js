// FIXED: Hardcoded API URL (moved to environment variable with fallback)
const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:5000';

export const compressFile = async (file, signal) => {
  const formData = new FormData();
  formData.append('file', file);

  const response = await fetch(`${API_BASE_URL}/compress`, {
    method: 'POST',
    body: formData,
    signal
  });

  if (!response.ok) {
    throw new Error('Compression failed');
  }

  return response.blob();
};

export const decompressFile = async (file, signal) => {
  const formData = new FormData();
  formData.append('file', file);

  const response = await fetch(`${API_BASE_URL}/decompress`, {
    method: 'POST',
    body: formData,
    signal
  });

  if (!response.ok) {
    throw new Error('Decompression failed');
  }

  return response.blob();
};
