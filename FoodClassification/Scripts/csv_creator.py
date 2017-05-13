import sys
import os
import csv
import glob

SPLIT_CHAR = '\\'
MAX_IMG_PER_CLASS = 5000000

# Path to input directory; subdir  conatain test  image files and name of subdir matches class name
path = sys.argv[1]

directories = [x[0] for x in os.walk(path)]

data = []

for directory in directories:
    tag = directory.rsplit(SPLIT_CHAR, 1)[-1]
    images = glob.glob(directory + "/*.jpg")

    if len(images) > 0:
        img_Cnt = 0
        for image in images:
            if img_Cnt <= MAX_IMG_PER_CLASS:
              taggedImage = [image, tag]
              data.append(taggedImage)
              print(taggedImage)
              img_Cnt += 1
            else:
              print("Got {0} images, breaking".format(img_Cnt))
              break

with open("images.csv", "w") as file:
    writer = csv.writer(file)
    writer.writerows(data)



input("All done. Press any key...")
