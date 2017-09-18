# ResearchAssistance

Final Product

ResearchAssistance is a rudimentary search engine that is designed specifically for looking up academic research papers on particular
topics. It polls different online API's to find related papers to your search. Each paper is then given a research assistance rating
representing how relevant the given paper is, how well known the journal it is published in is, as well as who the author is. The user
is then given the authors, paper name, abstract, citation, link to open the paper online, and a list of other papers published by these
authors in the same field. Papers are obtained through API polling using multiple threads to give immediate results while other threads
continue to poll. After results are recieved a crawler thread is then started which retrieves data on the journal as well as authors.
The polling script and crawler are written in python while the central app is written using java and javafx. Results from the scripts
are then analyzed using a rating algorithm by the java program. They are then presented in the most relevant order to the user.

Current Features

Multi thread API polling to Springer Database.
Crawler activity to find journal impact rating.
Algorithmic determination of research assistance rating.
Rating determined by downloads, citations, relevance and journal impact.
Presentation of abstract, citation, title and authors to user.
Link to open web page with article online.

Upcoming Features

Bug fixes to thread termination.
Improve thread polling algorithms.
Alter the way scripts run and data is retrieved from them.
Increase crawler ability to crawl to other work by authors.
Use other work relevance in RA rating.
List relevant author work in report.
Increase number of API's polled.
Increase search accuracy.

Current Bugs

Threads do not terminate on exit.
Long startup thread time to begin search.
Bugs related to non-alphabetical symbols in abstract/title.
