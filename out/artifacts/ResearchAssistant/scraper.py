import urllib.request
import sys
from bs4 import BeautifulSoup

def scrapeSite(url):
    page = urllib.request.urlopen(url)
    return BeautifulSoup(page, "html.parser")

def getCitations(data):
    citRes = data.find(id='citations-count-number')
    if citRes is None:
        citations = 0
    else :
        citations = citRes.contents[0]
    return citations

def getDownloads(data):
    downRes = data.find(class_="article-metrics__views")
    if downRes is None:
        downloads = 0
    else:
        downloads = downRes.contents[0]
    return downloads

def getKeywords(data):
    keyRes = data.findAll('script')[2]
    if keyRes is None:
        keywords = "None"
    else :
        contents = keyRes.contents[0]
        startIndex = contents.find('Keywords') + len('Keywords')
        if (startIndex == 7):
            keywords = "None"
        else:
            endIndex = contents.find('Country')
            keywords = contents[startIndex+3:endIndex-1]
            keywords = keywords.rstrip()
            keywords = keywords[0:len(keywords)-2]
    return keywords

def getJournalData(journalLink):
    data = scrapeSite(journalLink)
    imprData = (data.find(class_="ListStack ListStack--float")).findAll('span')
    label = str(imprData[0].contents[0])
    if label is "Impact Factor":
        impactRes = float(imprData[1].contents[0])
    else:
        impactRes = 1.0
    return impactRes


