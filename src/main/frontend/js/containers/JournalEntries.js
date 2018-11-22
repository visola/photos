import { inject, observer } from 'mobx-react';
import { Icon, Table } from 'semantic-ui-react';
import moment from 'moment';
import React from 'react';

import JournalEntryForm from './JournalEntryForm';

@inject("journalEntryDates")
@observer
export default class JournalEntries extends React.Component {
  componentWillMount() {
    this.props.journalEntryDates.fetch();
  }

  getDays() {
    const min = moment().subtract(15, 'days').startOf('week');
    const max = moment().add(15, 'days').endOf('week').add(1, 'days');
    const result = [];

    let week;

    const d = min.clone();
    let previous = null;
    do {
      if (previous == null || !d.isSame(previous, 'week')) {
        week = [];
        result.push(week);
      }

      week.push(d.clone());
      previous = d.clone();
      d.add(1, 'days');
    } while (!d.isSame(max, 'day'));
    return result;
  }

  handleSave() {
    this.props.journalEntryDates.fetch();
  }

  render() {
    return <React.Fragment>
      <JournalEntryForm onSave={this.handleSave.bind(this)} />
      <br />
      <br />
      {this.renderDates()}
    </React.Fragment>
  }

  renderDates() {
    const { journalEntryDates } = this.props;
    if (journalEntryDates.loading) {
      return <p>Loading...</p>;
    }

    if (journalEntryDates.isEmpty) {
      return <p>No Journal entries found.</p>;
    }

    return this.renderGrid();
  }

  renderDay(day) {
    const entryDates = this.props.journalEntryDates.filter(d => day.isSame(d, 'day'));
    const symbol = [];

    if (entryDates.length === 0) {
      symbol.push("-");
    } else {
      entryDates.forEach(d => symbol.push("o"))
    }

    return <Table.Cell textAlign="center">
      {day.format('MMM DD')}
      <br />
      {symbol}
    </Table.Cell>;
  }

  renderGrid() {
    const days = this.getDays();
    return <Table collapsing>
      <Table.Body>
        {days.map(week => this.renderWeek(week))}
      </Table.Body>
    </Table>;
  }

  renderWeek(week) {
    return <Table.Row>
      {week.map(d => this.renderDay(d))}
    </Table.Row>;
  }
}
