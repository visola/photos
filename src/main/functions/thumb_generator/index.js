const gm = require('gm').subClass({imageMagick: true});
const fs = require('fs');
const path = require('path');
const { Storage } = require('@google-cloud/storage');

const storage = new Storage();

exports.generateThumb = object => {
    if (object.resourceState === 'not_exists') {
        console.log('This is a deletion event.');
        return;
    } else if (!object.name) {
        console.log('This is a deploy event.');
        return;
    }

    const file = storage.bucket(object.bucket).file(object.name);
    const tempLocalPath = `/tmp/${path.parse(file.name).base}`;
    
    return file.download({destination: tempLocalPath})
        .catch(err => {
            console.error('Failed to download file.', err);
            return Promise.reject(err);
        }).then(() => {
            console.log(`Image ${file.name} has been downloaded to ${tempLocalPath}.`);
            return new Promise((resolve, reject) => {
                gm(tempLocalPath).resize(200, 200, '^')
                    .write(tempLocalPath, (err, stdout) => {
                        if (err) {
                            console.error('Failed to resize image: ' + file.name, err);
                            reject(err);
                            return;
                        }

                        resolve(stdout);
                    });
            });
        }).then(() => {
            console.log(`Thumbnail was generated. Uploading ${file.name} to thumbnails bucket.`);
            const toUpload = storage.bucket(process.env.THUMBNAIL_BUCKET).file(file.name);

            return toUpload.bucket.upload(tempLocalPath, { destination: file.name })
                .catch(err => {
                    console.err("Error while uploading thumbnail to bucket.");
                    return Promise.reject(err);
                });
        }).then(() => {
            console.log('Thumbnail uploaded successfully.');
            return new Promise((resolve, reject) => {
                fs.unlink(tempLocalPath, err => {
                    if (err) {
                        reject(err);
                    } else {
                        resolve();
                    }
                });
            });
        });
}
