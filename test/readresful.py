import urllib
import sys

content=open(sys.argv[1]).read()
link = "http://ec2-50-18-128-181.us-west-1.compute.amazonaws.com:6543/topic?text=\""
link += content
link += "\""
url=urllib.urlopen(link)
res = url.read()
print res
url.close()
