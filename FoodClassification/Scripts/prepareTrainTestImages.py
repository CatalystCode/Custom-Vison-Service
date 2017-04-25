import os
import numpy as np
from sklearn.cross_validation import train_test_split

dir_src = "TBD"
dir_test = "TBD"
class_dirs = os.listdir(dir_src)

for class_dir in class_dirs:
    origData = dir_src + class_dir + "\\"
    testData = dir_test + class_dir + "\\"

    print("processing {0}".format(origData))
    X = y = os.listdir(origData)
    # use 1/4 data for testing
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.25, random_state=0)
    os.makedirs(testData)
    for x in X_test:
        os.rename(origData + x, testData + x)

print("Done")
