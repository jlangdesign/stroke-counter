// import React, { useState, useEffect } from 'react';
import React, { useState } from 'react';

import Axios from '../../axios';

import Input from '../../components/Input/Input';
import Table from '../../components/Table/Table';

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
  // const getStrokeCount = (text) => {
  //   let count = 0;
  // };

  // Checks if the char is in the general range of the DB characters
  // (To help reduce number of API calls)
  const isInRange = (char) => {
    // Ranges: 3400 - 9FFC, F900 - FAD9, 20000 - 2FA1D, 30000 - 3134A
    const dec = char.charCodeAt(0); // Unicode val in decimal (not hex)
    return (
      (dec >= parseInt('3400', 16) && dec <= parseInt('9FFC', 16))
      || (dec >= parseInt('F900', 16) && dec <= parseInt('FAD9', 16))
      || (dec >= parseInt('20000', 16) && dec <= parseInt('2FA1D', 16))
      || (dec >= parseInt('30000', 16) && dec <= parseInt('3134A', 16))
    );
  };

  // Convert all Chinese chars
  const updateDisplayedText = (event) => {
    // Set original text
    // Cannot use { ...orig } because changing simp or trad chars changes OG!
    let orig = { chars: event.target.value.split(''), strokeCount: 0 };
    let simp = { chars: [...orig.chars], strokeCount: 0 };
    let trad = { chars: [...orig.chars], strokeCount: 0 };

    let simpVar = {};
    let tradVar = {};

    let promises = []; // To wait for all results with Promise.all()

    // While iterating through chars to get strokeCount,
    // also build simplified and traditional strings
    orig.chars.forEach((c, i) => {
      // If character is in the DB (is Chinese), then add
      // to stroke count
      if (isInRange(c)) {
        promises.push(
          Axios.get(`/char-data/${encodeURIComponent(c)}.json`)
            .then(res => {
              // console.log(res);
              const data = res.data;
              orig.strokeCount += data.strokes;
              // Check if char has simp variants
              if (!data.simp) {
                // If it doesn't (most likely), use OG character and stroke count
                simp.strokeCount += data.strokes;
              } else {
                // If it does, add first variant to simp.chars and add
                // any remaining variants to simpVar
                // Also get stroke count of simp to add
                const simpCh = Object.keys(data.simp[0])[0];
                simp.strokeCount += data.simp[0][simpCh];

                simp.chars[i] = simpCh;
                simpVar[i] = data.simp.filter(ch => ch !== simpCh);
              }

              if (!data.trad) {
                // If it doesn't (most likely), use OG character and stroke count
                trad.strokeCount += data.strokes;
              } else {
                // If it does, add first variant to trad.chars and add
                // any remaining variants to tradVar
                // Also get stroke count of trad to add
                const tradCh = Object.keys(data.trad[0])[0];
                trad.strokeCount += data.trad[0][tradCh];

                trad.chars[i] = tradCh;
                tradVar[i] = data.trad.filter(ch => ch !== tradCh);
              }
            })
            .catch(err => {
              console.log(`Issue retrieving char data for: ${c}`);
            })
        );
      }
    });

    Promise.all(promises).then(() => {
      setOrigChars({ chars: orig.chars.join(''), strokeCount: orig.strokeCount });

      // Convert to all simplified and get number of stokes
      // If multiple simplified versions, add to char picker popup
      setSimpChars({ chars: simp.chars.join(''), strokeCount: simp.strokeCount });

      // Convert to all traditional and get number of stokes
      // If multiple traditional versions, add to char picker popup
      setTradChars({ chars: trad.chars.join(''), strokeCount: trad.strokeCount });

      setSimpVar(simpVar);
      setTradVar(tradVar);
    });
  };

  return (
    <div className="layout">
      <Input
        original={origChars.chars}
        changed={(event) => updateDisplayedText(event)}/>
      <hr className="mobile-layout-divider" />
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
