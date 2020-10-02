#Code for scraping
import requests,time
import numpy as np
import pandas as pd
from bs4 import BeautifulSoup
import numpy as np
a=time.time()
start_page=1
stop_page=777
all_cols=['title', 'Address', 'City', 'State', 'Pin', 'Centre Manager', 'Contact', 'Email', 'Sectors', 'Courses']
dataset=pd.DataFrame(columns=all_cols)

for url_no in range(start_page,stop_page+1):
    x=requests.get("https://www.nsdcindia.org/New/training-center-list?page="+str(url_no))
    soup=BeautifulSoup(x.content)
    parentlis=soup.find("div",class_="tplisting")
    list_of_centres=parentlis.find_all("li")
    for i in list_of_centres:
    #    centreLocation=i.find("div",class_="news-down-heading news-down-heading-none").text
    #    print(i)
        if i.a:
            if i.a.get("href")!=None:
                continue
        
        try:
            if i.get("class")[0]=="current":
                continue
        except Exception as E:
            #print(E,"getting class")
            print(".")
        
        try:    
            centreLocation=i.find_all("div")[0].text
            dicti={"title":centreLocation}
            listi=[centreLocation]
            for j in range(1,10):
                try:
                    label=i.find_all("div")[j].label.text.strip(":").strip(" ").strip(":")
                except:
                    print("label error")
                try:
                    value=i.find_all("div")[j].span.text
                except:
                    print("value error")
                dicti[label]=value
                listi.append(value)
            print(len(listi))
            listi=np.array(listi)
            dataset.loc[len(dataset)]=listi
        except Exception as E:
            print(E)

#Change Directory
#dataset.to_excel("C:\\users\\acer\\desktop\\nim.xlsx",index=False)##Change directory
dataset.transpose().to_json("centerslocation.json")
print(time.time()-a)
