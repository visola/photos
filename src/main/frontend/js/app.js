import { Provider } from 'mobx-react';
import React from 'react';
import { render } from 'react-dom';
import stores from './stores';

import Routes from './containers/Routes';

const ApplicationWithState = () => (
  <Provider {...stores}>
    <Routes />
  </Provider>
);

render(<ApplicationWithState />, document.getElementById('container'));
