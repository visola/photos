import JournalEntries from './JournalEntries';
import Security from './Security';

const journalEntries = new JournalEntries();
const security = new Security();

security.checkLoggedIn();

export default {
  journalEntries,
  security,
};
