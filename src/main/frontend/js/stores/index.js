import JournalEntries from './JournalEntries';
import Projects from './Projects';
import Security from './Security';

const journalEntries = new JournalEntries();
const projects = new Projects();
const security = new Security();

security.checkLoggedIn();

export default {
  journalEntries,
  projects,
  security,
};
