import { BrowserRouter, Route, withRouter } from 'react-router-dom'
import { inject, observer } from 'mobx-react';
import PropTypes from 'prop-types';
import React from 'react';

import HabitForm from './HabitForm';
import Habits from './Habits';
import Home from './Home';
import Login from './Login';
import JournalEntries from './JournalEntries';
import ProjectForm from './ProjectForm';
import Projects from './Projects';
import TopMenu from './TopMenu';

@inject('security')
@observer
export default class Application extends React.Component {
  static propTypes = {
    security: PropTypes.object.isRequired,
  }

  render() {
    if (!this.props.security.isLoggedIn) {
      return <Login />;
    }

    return <BrowserRouter>
      <React.Fragment>
        <TopMenu />
        <div id="content">
          <Route exact path="/" component={Home} />
          <Route exact path="/habits" component={Habits} />
          <Route exact path="/habits/:habitId" component={withRouter(HabitForm)} />
          <Route exact path="/journal-entries" component={JournalEntries} />
          <Route exact path="/projects" component={Projects} />
          <Route exact path="/projects/:projectId" component={withRouter(ProjectForm)} />
        </div>
      </React.Fragment>
    </BrowserRouter>;
  }
}
