#!/usr/bin/python

import sys
import os
import csv
import glob

def whereAmI():
    return os.path.dirname(os.path.realpath(__import__("__main__").__file__))

path = sys.argv[1]
absolutePath = whereAmI() + "/" + path

directories = [x[0] for x in os.walk(absolutePath)]

data = []

for directory in directories:
    tag = directory.rsplit('/', 1)[-1]
    images = glob.glob(directory + "/*.jpg")

    if len(images) > 0:
        for image in images:
            taggedImage = [image, tag]
            data.append(taggedImage)
            print taggedImage


with open("images.csv", "wb") as file:
    writer = csv.writer(file)
    writer.writerows(data)
