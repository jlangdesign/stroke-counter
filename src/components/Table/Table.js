import React from 'react';

// import Auxilliary from '../../hoc/Auxilliary/Auxilliary';
import TableRow from './TableRow/TableRow';

import './Table.css';

const table = (props) => (
  <div className="tableSection">
    <TableRow
      heading="Original text"
      text={props.original}
      numStrokes="(numStrokes)" />
    <TableRow
      heading="Simplified Chinese"
      text={props.simplified}
      numStrokes="(numStrokes)" />
    <TableRow
      heading="Traditional Chinese"
      text={props.traditional}
      numStrokes="(numStrokes)" />
  </div>
);

export default table;
