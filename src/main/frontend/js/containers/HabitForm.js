import { Button, Dropdown, Form, Input, Message } from 'semantic-ui-react';
import { inject, observer } from 'mobx-react';
import React from 'react';

const FREQUENCY_KEYS = {
  "UNIT": "Unit",
  "TIME": "Time",
};

const FREQUENCY_TYPE = Object.keys(FREQUENCY_KEYS).map((k) => {
  return { text: FREQUENCY_KEYS[k], value: k };
});

const HABIT_TYPES = [
  { text: "Entry in Journal", value: "ENTRY_IN_JOURNAL" },
  { text: "Manual", value: "MANUAL" },
];

const INTERVAL_UNITS = [
  { text: "Year", value: "YEAR" },
  { text: "Month", value: "MONTH" },
  { text: "Week", value: "WEEK" },
  { text: "Day", value: "DAY" },
  { text: "Hour", value: "HOUR" },
];

@inject('habits')
@observer
export default class HabitForm extends React.Component {
  constructor(props) {
    super(props);

    const { habits } = props;
    const habitId = props.match.params.habitId;

    let habit;
    if (habitId === "new") {
      habit = {
        name: "",
        type: "MANUAL",
        interval: 1,
        intervalUnit: "WEEK",
        frequency: 3,
        frequencyType: "UNIT",
      };
    } else {
      habit = habits.findById(habitId);
    }

    if (habit == null) {
      habits.fetch()
        .then(() => this.setState({ habit: habits.findById(habitId) }));
    }

    this.state = { habit };
  }

  handleOnChange(field, newValue) {
    const { habit } = this.state;
    habit[field] = newValue;
    this.setState({ habit });
  }

  handleSave() {
    this.props.habits.saveOne(this.state.habit);
    this.props.history.push('/habits');
  }

  render() {
    const { habit } = this.state;
    if (this.props.habits.loading || habit == null) {
      return <p>Loading...</p>;
    }

    return <React.Fragment>
      <h2>{habit.id == null ? 'New' : 'Edit'} Habit</h2>
      {this.renderHabitDescription(habit)}
      <Form loading={this.props.habits.saving}>
        <Form.Field inline>
          <label>Name</label>
          <Input
            onChange={(e) => this.handleOnChange('name', e.target.value)}
            placeholder="Name"
            value={habit.name}
          />
        </Form.Field>
        <Form.Field inline>
          <label>Type</label>
          <Dropdown
            onChange={(e, data) => this.handleOnChange('type', data.value)}
            options={HABIT_TYPES}
            placeholder="Habit Type"
            selection
            value={habit.type}
          />
        </Form.Field>
        <Form.Field inline>
          <label>Interval</label>
          <Input
            onChange={(e) => this.handleOnChange('interval', e.target.value)}
            placeholder="Interval"
            value={habit.interval}
          />
        </Form.Field>
        <Form.Field inline>
          <label>Interval Unit</label>
          <Dropdown
            onChange={(e, data) => this.handleOnChange('intervalUnit', data.value)}
            options={INTERVAL_UNITS}
            placeholder="Interval Unit"
            selection
            value={habit.intervalUnit}
          />
        </Form.Field>
        <Form.Field inline>
          <label>Frequency</label>
          <Input
            onChange={(e) => this.handleOnChange('frequency', e.target.value)}
            placeholder="Frequency"
            value={habit.frequency}
          />
        </Form.Field>
        <Form.Field inline>
          <label>Frequency Type</label>
          <Dropdown
            onChange={(e, data) => this.handleOnChange('frequencyType', data.value)}
            options={FREQUENCY_TYPE}
            placeholder="Frequency Type"
            selection
            value={habit.frequencyType}
          />
        </Form.Field>
        {habit.frequencyType === "TIME" &&
          <Form.Field inline>
            <label>Frequency Unit</label>
            <Dropdown
              onChange={(e, data) => this.handleOnChange('frequencyUnit', data.value)}
              options={INTERVAL_UNITS}
              placeholder="Frequency Unit"
              selection
              value={habit.frequencyUnit}
            />
          </Form.Field>
        }
        <Button
          onClick={() => this.handleSave()}
          primary
          type="submit"
        >
          Save
        </Button>
      </Form>
    </React.Fragment>;
  }

  renderHabitDescription(habit) {
    let description = habit.name == "" ? "'Do this' " : `${habit.name} `;

    switch (habit.frequencyType) {
      case "TIME":
        description += `for ${habit.frequency} ${habit.frequencyUnit} `;
        break;
      case "UNIT":
        description += `${habit.frequency} ${habit.frequency === 1 ? 'time' : 'times'}`;
        break;
    }

    description += ` every ${habit.interval} ${habit.intervalUnit}`;

    switch (habit.type) {
      case "ENTRY_IN_JOURNAL":
        description += `, will be tracked using entries in your journal.`;
        break
      case "MANUAL":
        description += `, will be tracked manually.`;
        break
    }

    return <Message color="olive" compact>{description}</Message>
  }
}
