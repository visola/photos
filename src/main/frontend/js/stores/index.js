import Habits from './Habits';
import JournalEntries from './JournalEntries';
import JournalEntryDates from './JournalEntryDates';
import Photos from './Photos';
import Projects from './Projects';
import Security from './Security';

const habits = new Habits();
const journalEntries = new JournalEntries();
const journalEntryDates = new JournalEntryDates();
const projects = new Projects();
const security = new Security();

const photos = new Photos(security);

security.checkLoggedIn();

export default {
  habits,
  journalEntries,
  journalEntryDates,
  photos,
  projects,
  security,
};
