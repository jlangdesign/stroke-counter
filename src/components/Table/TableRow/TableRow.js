import React from 'react';

import './TableRow.css';

const tableRow = (props) => {
  const textStyle = props.text ? "tableRowText" : "tableRowPlaceholder";

  return (
    <div className="tableRow">
      <p className="tableRowTitle"><strong>{props.heading}</strong></p>
      <p className={textStyle}>{props.text || 'Characters appear here'}</p>
      <p className="tableRowStrokes">Total strokes: {props.numStrokes || 0}</p>
    </div>
  );
};

export default tableRow;
