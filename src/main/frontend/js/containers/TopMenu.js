import { Menu } from 'semantic-ui-react';
import React from 'react';

import JournalEntryForm from './JournalEntryForm';

export default class TopMenu extends React.Component {
  render() {
    return <Menu stackable>
      <Menu.Item>Life Booster</Menu.Item>

      <Menu.Item>
        <JournalEntryForm />
      </Menu.Item>
    </Menu>
  }
}