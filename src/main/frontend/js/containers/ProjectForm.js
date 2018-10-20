import { Button, Input } from 'semantic-ui-react';
import { inject, observer } from 'mobx-react';
import React from 'react';

@inject('projects')
@observer
export default class ProjectForm extends React.Component {
  render() {
    return <p>This is the project form.</p>;
  }
}