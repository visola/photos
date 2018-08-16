import AppBar from '@material-ui/core/AppBar';
import Description from '@material-ui/icons/Description';
import React from 'react';
import Tab from '@material-ui/core/Tab';
import Tabs from '@material-ui/core/Tabs';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';

export default class TopMenu extends React.Component {
  render() {
    return <AppBar position="static">
      <Toolbar>
        <Typography color="inherit" variant="title">Life Booster</Typography>
      </Toolbar>
    </AppBar>
  }
}