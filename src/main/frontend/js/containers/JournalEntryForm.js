import Button from '@material-ui/core/Button';
import Card from '@material-ui/core/Card';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import CardHeader from '@material-ui/core/CardHeader';
import { inject, observer } from 'mobx-react';
import React from 'react';
import TextField from '@material-ui/core/TextField';

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
      <CardHeader title="Positivity Journal" subheader="Write something positive to remember!"/>
      <CardContent>
        <TextField
          fullWidth={true}
          label="Entry"
          multiline={true}
          onChange={this.handleEntryChange}
          value={this.state.entry}
        />
      </CardContent>
      <CardActions>
        <Button
          color="primary"
          onClick={this.handleSave}
          variant="contained"
        >
          Save
        </Button>
      </CardActions>
    </Card>;
  }
}
