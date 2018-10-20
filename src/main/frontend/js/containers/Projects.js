import { Link } from 'react-router-dom';
import { inject, observer } from 'mobx-react';
import { Table } from 'semantic-ui-react'
import React from 'react';

@inject('projects')
@observer
export default class Projects extends React.Component {
  componentWillMount() {
    this.props.projects.fetch();
  }

  render() {
    const { data, loading } = this.props.projects;
    if (loading) {
      return <p>Loading...</p>;
    }

    return <Table>
      <Table.Header>
        <Table.Row>
          <Table.HeaderCell>Name</Table.HeaderCell>
        </Table.Row>
      </Table.Header>
      <Table.Body>
        {data.map((p) => this.renderProject(p))}
      </Table.Body>
    </Table>;
  }

  renderProject(p) {
    return <Table.Row key={p.id}>
      <Table.Cell>
        <Link to={`/projects/${p.id}`}>
          {p.name}
        </Link>
      </Table.Cell>
    </Table.Row>
  }
}