import Uploads from './Uploads';
import Security from './Security';

const security = new Security();
const uploads = new Uploads(security);

security.checkLoggedIn();

export default {
  security,
  uploads,
};
