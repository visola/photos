import { Button, Input } from 'semantic-ui-react';
import { inject, observer } from 'mobx-react';
import React from 'react';

@inject('journalEntries')
@observer
export default class JournalEntryForm extends React.Component {
  constructor(props) {
    super(props);
    this.handleEntryChange = this.handleEntryChange.bind(this);
    this.handleKeyPress = this.handleKeyPress.bind(this);
    this.handleSave = this.handleSave.bind(this);
    this.state = {
      entry: '',
      saving: false,
    };
  }

  handleEntryChange(e) {
    this.setState({ entry: e.target.value });
  }

  handleKeyPress(e) {
    if (e.key === 'Enter') {
      e.stopPropagation();
      e.preventDefault();
      this.handleSave();
    }
  }

  handleSave() {
    this.setState({ saving: true });
    this.props.journalEntries.create({entry: this.state.entry, date: Date.now()})
      .then(() => this.setState({ saving: false }));
    this.setState({ entry: '' });
  }

  render() {
    const { entry, saving } = this.state;
    const action = saving ? false : <Button onClick={this.handleSave}>Save</Button>;
    return <Input
      action={action}
      className="long"
      placeholder="Say something positive..."
      onChange={this.handleEntryChange}
      onKeyPress={this.handleKeyPress}
      loading={saving}
      value={entry}
    />
  }
}
