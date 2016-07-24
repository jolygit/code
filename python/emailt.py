#!/usr/bin/python

import smtplib
fromaddr = 'to.alex.joly@gmail.com'  
toaj  = 'alex.jaoshvili@gmail.com'
tolan  = 'ttbichlan@gmail.com'
msg= """From: Ip updater
Subject: Southampton house ip address
"""
import os
my_path = "/tmp/changed"
if  os.path.exists(my_path) and os.path.getsize(my_path) > 0:
    ip = open("/tmp/ip.new", "r+")
    str = ip.read(14)
    msg=msg+str
    ip.close()
    username = 'to.alex.joly'  
    password = 'aleko..82921'
    server = smtplib.SMTP('smtp.gmail.com', 587)  
    server.ehlo()
    server.starttls()
    server.login(username, password)  
    server.sendmail(fromaddr, toaj, msg)
    server.sendmail(fromaddr, tolan, msg)  
    server.quit()




