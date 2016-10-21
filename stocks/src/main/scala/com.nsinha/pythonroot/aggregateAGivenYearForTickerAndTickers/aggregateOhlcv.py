
import pandas as pd
from pandas import Series, DataFrame
import matplotlib.pyplot
import numpy as np
import scipy as sci

def aggregateYears(years, rootdir):
    mapForYears = dict()
    for year in years:
        mapForYear = aggregateTheYear(year, rootdir)
        mapForYears.update(mapForYear)

    return mapForYears


def aggregateTheYear(year, rootdir):
    return createMapForYear(str(year), rootdir)



def createMapForYear(year, rootdir):
    effectivedir = rootdir + "/" + year + "/"
    close = "close"
    open = "open"
    high = "high"
    low = "low"
    volume = "volume"
    allfilestoConsider = { close:"output/closingPrice.json.csv", open :"output/openPrice.json.csv",
                           high: "output/highPrice.json.csv",volume :"output/volume.json.csv",low: "output/lowPrice.json.csv"}
    dictOfDfsForYear = dict()
    for key, val in allfilestoConsider.iteritems():
        file = effectivedir + val
        df = pd.read_csv(file,nrows = 3, usecols=range(0,3))
        dictOfDfsForYear.update({year +  key : df})

    return dictOfDfsForYear









