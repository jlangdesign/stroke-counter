import React from 'react';

import './TableRow.css';

const tableRow = (props) => (
  <div className="tableRow">
    <p><strong>{props.heading}</strong></p>
    <p>{props.text}</p>
    <p>{props.numStrokes}</p>
  </div>
);

export default tableRow;
