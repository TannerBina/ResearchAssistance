import urllib.request
import json
import sys
import scraper

#apiKey for accessing Springer database
apiKey = "f4fdade35856c8aee7bb5b6f9ebc5b37"
journalSearch = "http://link.springer.com/journal/%s"

class Author:
    #init author class
    def __init__(self):
        self.name = ""
    

class Document:
    #init doucment data
    def __init__(self):
        self.identifier = ""
        self.url = ""
        self.title = ""
        self.pubName = ""
        self.publisher = ""
        self.pubDate = ""
        self.genre = ""
        self.abstract = ""
        self.authorsList = []

        self.journalId = ""

        self.rating = 0
        self.citations = 0
        self.downloads = 0
        self.journalImpact = 0
        self.keywords = ""

    #prints document to main output
    def printDoc(self):
        authors = ""
        for auth in self.authorsList:
            authors += " : "
            authors+=auth.name
        authors = authors.encode(encoding='UTF-8',errors='replace')
        print('%.3f' % self.rating)
        print(self.citations)
        print(self.keywords)
        print(self.identifier)
        print(self.url)
        print(self.title)
        print(self.pubName)
        print(self.publisher)
        print(self.pubDate)
        print(self.genre)
        print(self.abstract)
        print(authors[3:])

    #generates the rating for the documents
    def generateRating(self, search):
        self.rating = 1;
        
        if (self.title.decode().lower().count(search.lower()+" ") > 0):
            self.rating += 10
        elif (self.title.decode().lower().count(search.lower()) > 0):
            self.rating += 5            

        if (self.keywords.lower().count(search.lower()+",") > 0):
            self.rating += 10
        elif (self.keywords.lower().count(search.lower()) > 0):
            self.rating += 5
            
        if (self.abstract.decode().lower().count(search.lower()) > 0):
            self.rating += self.abstract.decode().lower().count(search.lower())

        if (self.downloads > 0):
            self.rating *= (1 + self.downloads/250.0)
            
        if (self.citations > 0):
            self.rating *= (1 + citations/10.0)

        self.rating = self.rating * self.journalImpact * self.journalImpact

    #sets the data of the document
    def setData(self, ident, url, title, pubName, publisher, pubDate, genre, abstract, authors, journalId):
        self.identifier = ident.encode(encoding='UTF-8',errors='replace')
        self.url = url.encode(encoding='UTF-8',errors='replace')
        self.title = title.encode(encoding='UTF-8',errors='replace')
        self.pubName = pubName.encode(encoding='UTF-8',errors='replace')
        self.publisher = publisher.encode(encoding='UTF-8',errors='replace')
        self.pubDate = pubDate.encode(encoding='UTF-8',errors='replace')
        self.genre = genre.encode(encoding='UTF-8',errors='replace')
        self.abstract = abstract.encode(encoding='UTF-8',errors='replace')
        for a in authors:
            auth = Author()
            auth.name = a['creator']
            self.authorsList.append(auth)

        self.journalId = journalId
        if self.journalId is "":
            self.journalImpact = 1
        else:
            self.journalImpact = scraper.getJournalData(journalSearch % self.journalId)

        pageData = scraper.scrapeSite(self.url.decode())
        self.citations = int(scraper.getCitations(pageData))
        self.downloads = int(scraper.getDownloads(pageData))
        self.keywords = scraper.getKeywords(pageData)

def submitRequest(searchType, search, start, num):
    #format search
    search = search.replace(" ", "%20")
    #create url
    url = "http://api.springer.com/metadata/json?q=%s%%22(%s)%%22&s=%s&p=%s&api_key=%s"
    #fetch data
    data = urllib.request.urlopen(url % (searchType, search, start, num, apiKey)).read()
    return data

def parseData(data):
    #parse original data
    jsonData = json.loads(data)
    #obtain record list
    recordList = jsonData['records']
    #parse each record
    documents = []
    for r in recordList:
        try:
            journalId = r['journalid']
        except KeyError:
            journalId = ""
        doc = Document()
        doc.setData(r['identifier'], r['url'][0]['value'], r['title'], r['publicationName'], r['publisher'], r['publicationDate'], r['genre'], r['abstract'][8:], r['creators'], journalId)
        documents.append(doc)

    return documents

#main code
#create search string from args
start = sys.argv[1]
numRes = int(sys.argv[2])

s = ""

for num in range(3, len(sys.argv)):
    s += " "
    s += sys.argv[num]

#trim first space
s = s[1:]
docList = []

#add title searches
docList.extend(parseData(submitRequest("keyword:", s, start, numRes)))
#if not enough, add content searches
if (len(docList) < numRes):
    docList.extend(parseData(submitRequest("title:", s, start, numRes)))
if (len(docList) < numRes):
    docList.extend(parseData(submitRequest("", s, start, numRes)))

#print all documents
for doc in docList:
    doc.generateRating(s)
    doc.printDoc()
