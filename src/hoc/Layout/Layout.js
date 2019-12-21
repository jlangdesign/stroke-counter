// import React, { useState, useEffect } from 'react';
import React, { useState } from 'react';

import Input from '../../components/Input/Input';
import Table from '../../components/Table/Table';

import './Layout.css';

// Trying to learn React Hooks, so I'm using a React
// function component here.
function Layout() {
  // Updating a state var like this always replaces it
  // instead of merging it
  const [orig, setOrig] = useState('');
  const [simp, setSimp] = useState('');
  const [trad, setTrad] = useState('');

  // Combines componentDidMount() and componentDidUpdate(),
  // which are called after render()
  // useEffect(() => {

  // }, [orig, simp, trad]);

  // Convert all Chinese chars (if not Chinese, display
  // error message)
  const updateDisplayedText = (event) => {
    // Set original text and get number of stokes
    setOrig(event.target.value);

    // Convert to all simplified and get number of stokes
    // If multiple simplified versions, display all
    setSimp(event.target.value);

    // Convert to all traditional and get number of stokes
    // If multiple traditional versions, display all
    setTrad(event.target.value);

  };

  return (
    <div className="layout">
      <Input
        original={orig}
        changed={(event) => updateDisplayedText(event)}/>
      <Table
        original={orig}
        simplified={simp}
        traditional={trad} />
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
