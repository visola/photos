import Collection from './Collection';

export default class Projects extends Collection {
  get baseApi() {
    return '/api/v1/projects';
  }
}
