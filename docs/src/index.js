import './polyfills';

import React from 'react';
import ReactDOM from 'react-dom';

import Index from './components/Index.jsx';

const INIT = document.getElementById('app');

console.info("asd", INIT);
ReactDOM.render(
    <Index
    />,
    INIT
);
