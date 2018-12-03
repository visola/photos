import { Card, Icon } from 'semantic-ui-react';
import { inject, observer } from 'mobx-react';
import { Link } from 'react-router-dom'
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
      case "MANUAL":
        return "Manual";
      default:
        return "Unknown Type";
    }
  }

  render() {
    if (this.props.habits.loading) {
      return <p>Loading...</p>;
    }

    return <React.Fragment>
      <p><Link to="/habits/new">Create New</Link></p>
      {this.renderHabits()}
    </React.Fragment>;
  }

  renderHabit(habit) {
    return <Card key={habit.id}>
      <Card.Content>
        <Card.Header>
          <Link to={`/habits/${habit.id}`}>{habit.name}</Link>
        </Card.Header>
        <Card.Meta>{this.getTypeText(habit.type)}</Card.Meta>
      </Card.Content>
    </Card>
  }

  renderHabits() {
    return this.props.habits.map(h => this.renderHabit(h));
  }
}