import React from 'react';

// import Auxilliary from '../../hoc/Auxilliary/Auxilliary';
import TableRow from './TableRow/TableRow';

import './Table.css';

const table = (props) => (
  <div className="tableSection">
    <TableRow
      heading="Original text"
      text="(Original text)"
      numStrokes="(numStrokes)" />
    <TableRow
      heading="Simplified Chinese"
      text="(Simplified text)"
      numStrokes="(numStrokes)" />
    <TableRow
      heading="Traditional Chinese"
      text="(Traditional text)"
      numStrokes="(numStrokes)" />
  </div>
);

export default table;
