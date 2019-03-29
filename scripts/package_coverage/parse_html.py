import requests
from bs4 import BeautifulSoup

response = requests.get("https://developer.android.com/reference/androidx/packages")
soup = BeautifulSoup(response.text, "html.parser")


tds = soup.find_all("td", class_="jd-linkcol")

classes = []

with open("AndroidXPackages.txt", "w") as f:
    for td in tds:
        f.write("{0}\n".format(td.a.text))
        
