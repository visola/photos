import Collection from './Collection';

export default class Habits extends Collection {
  get baseApi() {
    return '/api/v1/habits';
  }
}
