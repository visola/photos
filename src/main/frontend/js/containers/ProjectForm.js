import { Button, Form } from 'semantic-ui-react';
import { inject, observer } from 'mobx-react';
import React from 'react';

@inject('projects')
@observer
export default class ProjectForm extends React.Component {
  constructor(props) {
    super(props);

    const { projects } = props;
    const projectId = props.match.params.projectId;

    let project;
    if (projectId == "new") {
      project = { name: "" };
    } else {
      project = projects.findById(projectId);
    }

    if (project == null) {
      projects.fetch()
        .then(() => {
          const project = projects.findById(projectId);
          this.state = { project };
        });
    } else {
      this.state = { project };
    }
  }

  handleFieldChange(fieldName, e) {
    const { project } = this.state;
    project[fieldName] = e.target.value;
    this.setState({ project });
  }

  handleSubmit(e) {
    e.preventDefault();
    const { project } = this.state;
    this.props.projects.saveOne(project)
      .then(() => this.props.history.push('/projects'));
  }

  render() {
    const { projects } = this.props;
    const { loading } = projects;
    if (loading) {
      return <p>Loading...</p>;
    }

    const { project } = this.state;
    return <Form>
      <h3>Project</h3>
      <Form.Field inline>
        <label>Name</label>
        <input
          onChange={(e) => this.handleFieldChange('name', e)}
          placeholder="Name"
          value={project.name}
        />
      </Form.Field>
      <Button onClick={this.handleSubmit.bind(this)} type="submit">Save</Button>
    </Form>;
  }
}