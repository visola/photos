import { inject, observer } from 'mobx-react';
import PropTypes from 'prop-types';
import React from 'react';

import Login from './Login';

@inject('security')
@observer
export default class Routes extends React.Component {
  static propTypes = {
    security: PropTypes.object.isRequired,
  }

  render() {
    if (!this.props.security.isLoggedIn) {
      return <Login />;
    }

    return <p>Hello world</p>;
  }
}
