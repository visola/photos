import { Divider, Loader } from 'semantic-ui-react';
import { DragDrop } from '@uppy/react';
import { inject, observer } from 'mobx-react';
import React from 'react';

@inject('photos')
@observer
export default class Photos extends React.Component {
  constructor(props) {
    super(props);

    this.handleUploadSuccess = this.handleUploadSuccess.bind(this);
  }

  componentWillMount() {
    this.props.photos.fetch();
    this.props.photos.uppy.on('upload-success', this.handleUploadSuccess);
  }

  componentWillUnmount() {
    this.props.photos.uppy.off('upload-success', this.handleUploadSuccess);
  }

  handleUploadSuccess() {
    this.props.photos.fetch();
  }

  render() {
    return <React.Fragment>
      <DragDrop
        height="120px"
        uppy={this.props.photos.uppy}
        width="400px"
      />
      <Divider />
      {this.renderPhotos()}
    </React.Fragment>;
  }

  renderPhotos() {
    if (this.props.photos.loading) {
      return <Loader />
    }

    return this.props.photos.map((p) => {
      return <div className="image-preview" key={p.id}>{p.name}</div>
    });
  }
}
