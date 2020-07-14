// import React, { useState, useEffect } from 'react';
import React, { useState } from 'react';

import Input from '../../components/Input/Input';
import Table from '../../components/Table/Table';

// import charData from '../../input/charData.json';

import './Layout.css';

// Trying to learn React Hooks, so I'm using a React
// function component here.
function Layout() {
  // Updating a state var like this always replaces it
  // instead of merging it
  // Original input and initial simp and trad versions
  const [origChars, setOrigChars] = useState({
    chars: '', strokeCount: 0
  });
  const [simpChars, setSimpChars] = useState({
    chars: '', strokeCount: 0
  });
  const [tradChars, setTradChars] = useState({
    chars: '', strokeCount: 0
  });

  // Save extra simp and trad char variants here for
  // swapping later
  // Format: [char_idx_in_string]: [array of chars]
  const [simpVar, setSimpVar] = useState({});
  const [tradVar, setTradVar] = useState({});

  // Calculates the number of strokes in the text passed in
  const getStrokeCount = (text) => {
    let count = 0;
  };

  // Convert all Chinese chars
  const updateDisplayedText = (event) => {
    // Set original text
    let orig = { chars: event.target.value, strokeCount: 0 };
    let simp = { ...orig };
    let trad = { ...orig };

    // While iterating through chars to get strokeCount,
    // also build simplified and traditional strings
    for (let i = 0; i < orig.chars.length; i++) {
      // If character is in the DB (is Chinese), then add
      // to stroke count
      orig.strokeCount++;

      // Check if char has simp variants
      // If so, add first variant to simp.chars and add
      // any remaining variants to simpVar
      // Also get stroke count of simp to add
      // Else, use OG character and stroke count

      // Check if char has trad variants
      // If so, add first variant to trad.chars and add
      // any remaining variants to tradVar
      // Also get stroke count of trad to add
      // Else, use OG character and stroke count
    }


    setOrigChars(orig);

    // Convert to all simplified and get number of stokes
    // If multiple simplified versions, add to char picker popup
    setSimpChars(simp);

    // Convert to all traditional and get number of stokes
    // If multiple traditional versions, add to char picker popup
    setTradChars(trad);

  };

  return (
    <div className="layout">
      <Input
        original={origChars.chars}
        changed={(event) => updateDisplayedText(event)}/>
      <Table
        original={origChars}
        simplified={simpChars}
        traditional={tradChars} />
    </div>
  );
}

export default Layout;

// You can use Hooks to add state to a function component
// without having to convert it into a class and make it
// verbose.
// But they should not be in loops, conditions, or nested
// functions - only called at the top level, and Hook calls
// must be called in the same order (conditions can be inside
// of Hooks though).

// Old functional component that Hooks don't work in
// const layout = (props) => (
//   <div className="layout">
//     <Input />
//     <Table />
//   </div>
// );

// Combines componentDidMount() and componentDidUpdate(),
// which are called after render()
// useEffect(() => {

// }, [origChars, simpChars, tradChars]);
