import React from 'react';

// import Auxilliary from '../../hoc/Auxilliary/Auxilliary';
import TableRow from './TableRow/TableRow';

import './Table.css';

const table = ({ original, simplified, traditional }) => (
  <div className="tableSection">
    <TableRow
      heading="Original text (原)"
      text={original.chars}
      numStrokes={original.strokeCount} />
    <TableRow
      heading="Simplified Chinese (简)"
      text={simplified.chars}
      numStrokes={simplified.strokeCount} />
    <TableRow
      heading="Traditional Chinese (繁)"
      text={traditional.chars}
      numStrokes={traditional.strokeCount} />
  </div>
);

export default table;
