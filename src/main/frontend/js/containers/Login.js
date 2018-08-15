import React from 'react';

import Button from '@material-ui/core/Button';

export default class Login extends React.Component {
  render() {
    return <p className="login">
      <Button color="primary" href="/authenticate/google" variant="contained">
        Login with Google
      </Button>
    </p>;
  }
}