import { Provider } from 'mobx-react';
import React from 'react';
import { render } from 'react-dom';
import stores from './stores';

const ApplicationWithState = () => (
  <Provider {...stores}>
    <p>Hello world!</p>
  </Provider>
);

console.log(`Is logged in: ${stores.security.isLoggedIn}`);

render(<ApplicationWithState />, document.getElementById('container'));
