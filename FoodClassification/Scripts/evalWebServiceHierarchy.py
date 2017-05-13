from sklearn import metrics
import requests
import os
import base64
import pandas as pd
import sys

import numpy as np
import matplotlib.pyplot as plt
import itertools
import time

from sklearn.metrics import confusion_matrix

SANDWICH_AND_BURGS = "bsandwich"
DESSERT = "desserts"
RED_FOOD = "belltomato"


class Endpoint:
    def __init__(self, iterationUrl, iterationKey):
        self.url = iterationUrl
        self.key = iterationKey


def makePredictionRequest(path, endpoint, key, index):
    resp = None
    try:
        f = open(path, 'rb')
        data = f.read()
        start_time_req = time.time()
        resp = requests.post(endpoint,
                             data=data,
                             headers={'Content-Type': 'application/octet-stream', 'Prediction-Key': key})
        elapsed_time_req = time.time() - start_time_req
        if index % 10 == 0:
            print("elapsed time  for req # {0} is {1}".format(index, elapsed_time_req))
    except Exception as e:
        print("Error opening file or sending request")
        print(e.__doc__)
        print(e.__cause__)
    finally:
        f.close()
    return resp


def plot_confusion_matrix(cm, classes,
                          normalize=False,
                          title='Confusion matrix',
                          cmap=plt.cm.Blues):
    """
    This function prints and plots the confusion matrix.
    Normalization can be applied by setting `normalize=True`.
    """
    plt.imshow(cm, interpolation='nearest', cmap=cmap)
    plt.title(title)
    plt.colorbar()
    tick_marks = np.arange(len(classes))
    plt.xticks(tick_marks, classes, rotation=45)
    plt.yticks(tick_marks, classes)

    if normalize:
        cm = cm.astype('float') / cm.sum(axis=1)[:, np.newaxis]
        print("Normalized confusion matrix")
    else:
        print('Confusion matrix, without normalization')

    print(cm)

    thresh = cm.max() / 2.
    for i, j in itertools.product(range(cm.shape[0]), range(cm.shape[1])):
        plt.text(j, i, cm[i, j],
                 horizontalalignment="center",
                 color="white" if cm[i, j] > thresh else "black")

    plt.tight_layout()
    plt.ylabel('True label')
    plt.xlabel('Predicted label')


MAX_Iterations = 200000
# TODO: CHANGE ME!!
endpoint = "<web service endpoint>"
key = '<web service key>'

print("Reading input csv with images paths and labels")
filePath = sys.argv[1]
print("file: {0}".format(filePath))
df = pd.read_csv(filePath)

y_true = []
y_pred = []

# TODO: CHANGE ME!!
endpointsDict = {
    SANDWICH_AND_BURGS: Endpoint(iterationKey="<key>",
                                 iterationUrl="<url>"),
    RED_FOOD: Endpoint(iterationKey="<key>",
                       iterationUrl="<url>")
}

start_time = time.time()
for index, row in df.iterrows():
    if index > MAX_Iterations:
        break
    if index % 10 == 0:
        elapsed_time = time.time() - start_time
        print("processing interation {0}, elapsed time {1}".format(index, elapsed_time))
    path = row[0]
    label = row[1]
    resp = makePredictionRequest(path, endpoint, key, index)
    if resp != None and resp.status_code == requests.codes.ok:
        jsonResponse = resp.json()
        try:
            predictedClass = jsonResponse["Predictions"][0]["Tag"]
            predictedProb = jsonResponse["Predictions"][0]["Probability"]
            if (predictedClass in endpointsDict):
                print("Making layer 2 call for {0}".format(predictedClass))
                subModelInfo = endpointsDict[predictedClass]
                resp = makePredictionRequest(path, subModelInfo.url, subModelInfo.key, index)
                if resp != None and resp.status_code == requests.codes.ok:
                    jsonResponse = resp.json()
                    predictedClass = jsonResponse["Predictions"][0]["Tag"]
                    predictedProb = jsonResponse["Predictions"][0]["Probability"]
                else:
                    respCode = "None"
                    if (resp != None):
                        respCode = resp.status_code
                    print("Response {0} for image {1}".format(respCode, path))
                    continue
            y_true.append(label)
            y_pred.append(predictedClass)

        except Exception as e:
            print("Error parsing json response")
            print(e.__doc__)
            print(e._c)
    else:
        print("Got status code {0} for image '{1}' label {2}".format(resp.status_code, path, label))

print(metrics.classification_report(y_true, y_pred))

# Compute confusion matrix
cnf_matrix = confusion_matrix(y_true, y_pred)
np.set_printoptions(precision=2)

# Plot non-normalized confusion matrix
plt.figure()
class_names = sorted(set(y_true))
plot_confusion_matrix(cnf_matrix, classes=class_names,
                      title='Confusion matrix, without normalization')

# Plot normalized confusion matrix
plt.figure()
plot_confusion_matrix(cnf_matrix, classes=class_names, normalize=True,
                      title='Normalized confusion matrix')

plt.show()

input("All done. Press any key...")
