import { Button, Card, Input } from 'semantic-ui-react';
import { inject, observer } from 'mobx-react';
import React from 'react';

@inject('journalEntries')
@observer
export default class JournalEntryForm extends React.Component {
  constructor(props) {
    super(props);
    this.handleEntryChange = this.handleEntryChange.bind(this);
    this.handleSave = this.handleSave.bind(this);
    this.state = {
      entry: '',
    };
  }

  handleEntryChange(e) {
    this.setState({ entry: e.target.value });
  }

  handleSave() {
    this.props.journalEntries.create({entry: this.state.entry, date: Date.now()});
    this.setState({ entry: '' });
  }

  render() {
    return <Card className="journal-entry-card">
      <Card.Content>
        <Card.Header>Positivity Journal</Card.Header>
      </Card.Content>
      <Card.Content>
        <Input
          fluid
          onChange={this.handleEntryChange}
          value={this.state.entry}
        />
      </Card.Content>
      <Card.Content extra>
        <Button onClick={this.handleSave}>
          Save
        </Button>
      </Card.Content>
    </Card>;
  }
}
