import { autorun } from 'mobx';
import Collection from './Collection';
import Uppy from '@uppy/core';
import XHRUpload from '@uppy/xhr-upload';

export default class Photos extends Collection {
  constructor(security) {
    super();

    this.uppy = Uppy({
      autoProceed: true,
      restrictions: {
        allowedFileTypes: ['image/*', '.jpg', '.jpeg', '.png', '.gif'],
      },
    });

    autorun(() => {
      if (!security.isLoggedIn) {
        return;
      }

      this.uppy.use(
        XHRUpload,
        {
          bundle: false,
          endpoint: '/api/v1/photos',
          fieldName: 'file',
          headers: { Authorization: `Bearer ${security.token}` }
        }
      );
    });
  }

  get baseApi() {
    return '/api/v1/photos';
  }
}
