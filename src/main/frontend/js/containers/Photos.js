import { DragDrop } from '@uppy/react';
import { inject, observer } from 'mobx-react';
import React from 'react';

@inject('photos')
@observer
export default class Photos extends React.Component {
  render() {
    return <React.Fragment>
      <p>Upload your photos here:</p>
      <DragDrop
        height="300px"
        uppy={this.props.photos.uppy}
        width="400px"
      />
    </React.Fragment>;
  }
}
