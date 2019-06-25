import Photos from './Photos';
import Security from './Security';

const security = new Security();
const photos = new Photos(security);

security.checkLoggedIn();

export default {
  photos,
  security,
};
