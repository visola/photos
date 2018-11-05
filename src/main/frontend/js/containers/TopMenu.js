import { Link } from 'react-router-dom'
import { Menu } from 'semantic-ui-react';
import React from 'react';

import JournalEntryForm from './JournalEntryForm';

export default class TopMenu extends React.Component {
  render() {
    const items = [
      { path: "/", label: "Life Booster" },
      { path: "/journal-entries", label: "Journal Entries" },
      { path: "/projects", label: "Projects" },
    ];

    return <Menu stackable>
      {items.map((item) => {
        const active = location.pathname === item.path;
        return <Menu.Item key={item.path} active={active}>
          <Link to={item.path}>{item.label}</Link>
        </Menu.Item>
      })}
    </Menu>
  }
}
