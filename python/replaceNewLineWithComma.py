import csv

f = open('/Users/aj/code/python/test.csv')
csv_f = csv.reader(f)
st=""
for row in csv_f:
  st+=","+row[0]+","+row[1]
print st
