import React from 'react';

import './Input.css';

const input = (props) => (
  <div className="inputSection">
    <p className="instructions"><strong>Input your text below (only Chinese characters):</strong></p>
    <input
      name="text-to-display"
      type="text"
      placeholder="Enter Chinese text here..."
      onChange={props.changed}
      value={props.original} />
  </div>
);

export default input;
