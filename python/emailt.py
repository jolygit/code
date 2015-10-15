#!/usr/bin/python

import smtplib
fromaddr = 'to.alex.joly@gmail.com'  
toaddrs  = 'to.alex.joly@gmail.com'
msg= """From: Alexander Jaoshvili <to.alex.joly@gmail.com>
To: Alexander Java <to.alex.joly@gmail.com>
MIME-Version: 1.0
Content-type: text/html
Subject: SMTP HTML e-mail test
This is an e-mail message to be sent in HTML format

<b>This is HTML message.</b>
<h1>This is headline.</h1>
"""
username = 'to.alex.joly'  
password = 'aleko..82921'

server = smtplib.SMTP('smtp.gmail.com', 587)  
server.ehlo()
server.starttls()
server.login(username, password)  
server.sendmail(fromaddr, toaddrs, msg)  
server.quit()




