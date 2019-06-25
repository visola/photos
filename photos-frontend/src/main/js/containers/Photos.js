import { Divider, Loader } from 'semantic-ui-react';
import { DragDrop } from '@uppy/react';
import { inject, observer } from 'mobx-react';
import React from 'react';

@inject('photos', 'security')
@observer
export default class Photos extends React.Component {
  constructor(props) {
    super(props);

    props.photos.initializeUppy()
    this.handleUploadSuccess = this.handleUploadSuccess.bind(this);
  }

  componentWillMount() {
    this.props.photos.fetch();
    this.props.photos.uppy.on('upload-success', this.handleUploadSuccess);
  }

  componentWillUnmount() {
    const { photos } = this.props;
    photos.uppy.off('upload-success', this.handleUploadSuccess);
    photos.closeUppy();
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

    const { token } = this.props.security;

    return <div className="image-previews">
      {this.props.photos.map((p) => {
        return <img src={`/api/v1/photos/${p.id}/thumbnail?auth=${token}`} key={p.id} />
      })}
    </div>;
  }
}
