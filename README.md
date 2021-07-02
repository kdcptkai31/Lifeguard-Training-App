# Lifeguard Training App

### Description:
This desktop application has been developed for California State Parks to record and manage data as well as generate reports for their annual and statewide lifeguard training.
On a high level, it is designed to:
- reduce the time instructors and operators spend processing data and writing reports
- seamlessly collect initial data on a year of trainees from Google Forms via .csv files
- provide a user interface for training operators to dynamically modify training infrastructure data
- optimally walk training operators through daily tasks
- generate numerous reports including test statistics, administrative tools, and various data summaries in a printable format
- effortlessly switch application focus between all recorded training sessions

#### Background:
In the summer of 2020, I approached the California State Lifeguard Training Sergeant with a proposal to make a desktop application to manage all data and generate reports neccessary to administrate training. This is a roughly 80 hour, 8 day training program, with multiple sessions per summer. Previously, all administrative work was being completed by hand. Hearing how much time was being lost on tedious tasks screamed to me "make an app for this." I was about to start my senior year at CSUF studying Computer Science and I wanted to apply the skills I had learned, while giving back to the system I had worked with for the greater part of a decade. After a meeting describing what I could develop and how it would greatly reduce their time behind a desk, I was quickly contracted. I designed and developed the application while finishing my last semester and the application went into operation in June 2021.

### Process:
I followed the XP (Extreme Programming) Agile Methodology and held meetings with stakeholders once a month to show the current working version of the application to collect feedback. The database was designed as an Entity Relationship model, then converted to a Relational model; implemented with SQLite. I also provided hotfixes during the first two weeks of operation.

### Use Cases:

General App Usage:
* Initial startup lets user enter the initial session information, which also creates a new database if one does not exist.
* Switch which session's data is being shown at any time.
* Select an unscored test and enter in the trainees' scores.
* Select an unscored event and enter in the trainees' placements by cap numbers.
* Take daily attendance hours.
* Import daily instructor comments from the Google Form .csv.
* Take final evaluation comments and score changes for every trainee.

Reports:
* Creates an excel spreadsheet that displays the current rankings for the session.
* Creates a folder of word documents for each trainee's copletion certificate for the session.
* Creates an excel spreadsheet which summarizes each trainee's uniform orders, with session totals.
* Creates an excel spreadsheet timesheet for each trainee's hours worked, each day of training.
* Creates a folder with an excel spreadsheet for each district, summarizing rankings, scores and placements for each trainee in that district.
* Creates an excel spreadsheet for each trainee, packaged by district, which details scores, placements, rank, and all instructor comments about them.
* Creates an overall session summary as an excel spreadsheet with all rankings, scores and placements.
* Creates an excel spreadsheet which generates common statistics for each taken test.
* Creates an excel spreadsheet email list for the session.
* Creates an excel spreadsheet emergency contact roster.
* Creates an excel spreadsheet with each trainee's info who requires lodging services.
* Creates an excel spreadsheet with each trainee's profile picture, with their name and district.
* Creates an excel spreadsheet roster.
* Creates an excel spreadsheet to fit the Avery Custom Labels format for nametags and nametents.

Trainee:
* Import Trainee data from .csv for the entire year. This creates all needed sessions, using the most recent session's infrastrucure as a template.
* Import both Questionnaire data sets from .csv for the entire year.
* Upload a trainee's profile picture.
* Add, edit, or delete related trainee's data.
* Inactivate a trainee.
* Reactivate a trainee.
* Move trainee to a different session.

Tests:
* Add, edit, or delete tests.
* Unscore a test, deleting all associated test scores and marking it as "Not Taken".
* Update a test's total possible points, retroactively changing scores to fit the same ratio, if the test is already scored.

Events:
* Add, edit, or delete events.
* Unscore an event, deleting all associated event scores and marking it as "Not Taken".

Instructor:
* Add, edit, or delete instructor data.
* Upload an Instructor's profile picture.

District and Sectors:
* Add, edit, or delete Districts or Sectors.
* Assign or unassign Districts to Sectors.
