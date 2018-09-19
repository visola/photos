import { inject, observer } from 'mobx-react';
import PropTypes from 'prop-types';
import React from 'react';

import JournalEntryForm from './JournalEntryForm';
import Login from './Login';
import TopMenu from './TopMenu';

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

    return <React.Fragment>
      <TopMenu />
      <div id="content">
        <JournalEntryForm />
      </div>
    </React.Fragment>;
  }
}
