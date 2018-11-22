import { Card, Icon } from 'semantic-ui-react';
import { inject, observer } from 'mobx-react';
import React from 'react';

@inject("habits")
@observer
export default class Habits extends React.Component {
  componentWillMount() {
    this.props.habits.fetch();
  }

  getTypeText(type) {
    switch(type) {
      case "ENTRY_IN_JOURNAL":
        return "Entry in Journal";
      default:
        return "Unknown Type";
    }
  }

  render() {
    if (this.props.habits.loading) {
      return <p>Loading...</p>;
    }

    return this.renderHabits();
  }

  renderHabit(habit) {
    return <Card key={habit.id}>
      <Card.Content>
        <Card.Header>{habit.name}</Card.Header>
        <Card.Meta>{this.getTypeText(habit.type)}</Card.Meta>
      </Card.Content>
    </Card>
  }

  renderHabits() {
    return this.props.habits.map(h => this.renderHabit(h));
  }
}