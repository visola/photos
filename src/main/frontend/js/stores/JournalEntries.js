import Collection from './Collection';

export default class JournalEntries extends Collection {
  get baseApi() {
    return '/api/v1/journal-entries';
  }
}