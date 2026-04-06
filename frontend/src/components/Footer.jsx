import React from 'react';
import { Link } from 'react-router-dom';
import { Cpu, Github, Twitter, Linkedin, Mail } from 'lucide-react';

const Footer = () => {
  return (
    <footer className="bg-dark-lighter border-t border-white/10 pt-16 pb-8 px-6">
      <div className="max-w-7xl mx-auto grid grid-cols-1 md:grid-cols-4 gap-12 mb-16">
        <div className="col-span-1 md:col-span-2">
          <Link to="/" className="flex items-center gap-2 mb-6">
            <div className="w-8 h-8 bg-gradient-to-br from-cyan-accent to-violet-accent rounded-md flex items-center justify-center">
              <Cpu size={18} className="text-white" />
            </div>
            <span className="text-lg font-bold text-white">BytePress</span>
          </Link>
          <p className="text-gray-400 max-w-sm leading-relaxed mb-6">
            Next-generation data compression powered by advanced Huffman algorithms. 
            Compress intelligence into every byte with our premium SaaS tools.
          </p>
          <div className="flex gap-4">
            <a href="#" className="p-2 bg-white/5 rounded-full hover:bg-cyan-accent hover:text-dark transition-all">
              <Github size={20} />
            </a>
            <a href="#" className="p-2 bg-white/5 rounded-full hover:bg-cyan-accent hover:text-dark transition-all">
              <Twitter size={20} />
            </a>
            <a href="#" className="p-2 bg-white/5 rounded-full hover:bg-cyan-accent hover:text-dark transition-all">
              <Linkedin size={20} />
            </a>
          </div>
        </div>

        <div>
          <h4 className="text-white font-semibold mb-6">Product</h4>
          <ul className="space-y-4 text-gray-400">
            <li><Link to="/compress" className="hover:text-cyan-accent transition-colors">Compressor</Link></li>
            <li><Link to="/decompress" className="hover:text-cyan-accent transition-colors">Decompressor</Link></li>
            <li><Link to="/download" className="hover:text-cyan-accent transition-colors">Desktop App</Link></li>
            <li><a href="#" className="hover:text-cyan-accent transition-colors">API Reference</a></li>
          </ul>
        </div>

        <div>
          <h4 className="text-white font-semibold mb-6">Company</h4>
          <ul className="space-y-4 text-gray-400">
            <li><a href="#" className="hover:text-cyan-accent transition-colors">About Us</a></li>
            <li><a href="#" className="hover:text-cyan-accent transition-colors">Privacy Policy</a></li>
            <li><a href="#" className="hover:text-cyan-accent transition-colors">Terms of Service</a></li>
            <li><a href="#" className="hover:text-cyan-accent transition-colors flex items-center gap-2">
              <Mail size={16} /> Contact Support
            </a></li>
          </ul>
        </div>
      </div>
      
      <div className="max-w-7xl mx-auto border-t border-white/5 pt-8 flex flex-col md:flex-row justify-between items-center gap-4 text-sm text-gray-500">
        <p>© 2026 BytePress Inc. All rights reserved.</p>
        <p>Built for the future of efficient computing.</p>
      </div>
    </footer>
  );
};

export default Footer;
