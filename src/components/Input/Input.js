import React from 'react';

import './Input.css';

const input = (props) => (
  <div className="inputSection">
    <p className="instructions"><strong>Input your text below (in Chinese characters):</strong></p>
    <p className="note">Note: Only Chinese characters are factored into the total stroke counts.</p>
    <textarea
      className="charInput"
      name="text-to-display"
      type="text"
      placeholder="Enter Chinese text here..."
      onChange={props.changed}
      value={props.original} />
  </div>
);

export default input;
