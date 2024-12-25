import React, { useEffect } from 'react';
import '../CustomToast.css';  

const CustomToast = ({ message, duration, onClose }) => {
  useEffect(() => {
    const timer = setTimeout(() => {
      onClose();
    }, duration);

    return () => clearTimeout(timer);
  }, [duration, onClose]);

  return (
    <div className="custom-toast">
      <div className="toast-message">{message}</div>
      <button className="toast-close-button" onClick={onClose}>X</button> 
    </div>
  );
};

export default CustomToast;
