import { Link } from 'react-router-dom'
import { Menu } from 'semantic-ui-react';
import React from 'react';

import JournalEntryForm from './JournalEntryForm';

export default class TopMenu extends React.Component {
  render() {
    return <Menu stackable>
      <Menu.Item>
        <Link to="/">Life Booster</Link>
      </Menu.Item>

      <Menu.Item>
        <JournalEntryForm />
      </Menu.Item>

      <Menu.Item>
        <Link to="/projects">Projects</Link>
      </Menu.Item>
    </Menu>
  }
}
