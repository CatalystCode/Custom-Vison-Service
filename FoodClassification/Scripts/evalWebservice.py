# http://scikit-learn.org/stable/modules/model_evaluation.html
# PRECISION: What percent of positive predictions were correct? PR=TP/Total Predicted positive
# RECALL: What percent of the positive cases did you catch?? R=TP/Total
# Real Positive

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
endpoint = "https://customvisionppe.azure-api.net/v1.0/Prediction/a275583f-7105-474.../image?iterationId=954d73b5-0adf..."
key = '1f63500...'

print("Reading input csv with images paths and labels")
# "D:\\repos\\IRISDemo\\EvalWebservice\\EvalWebservice\\images.csv"
filePath = sys.argv[1]
print("file: {0}".format(filePath))
df = pd.read_csv(filePath)

y_true = []
y_pred = []

start_time = time.time()
for index, row in df.iterrows():
    if index > MAX_Iterations:
        break
    if index % 10 == 0:
        elapsed_time = time.time() - start_time
        print("processing interation {0}, elapsed time {1}".format(
            index, elapsed_time))
    path = row[0]
    label = row[1]
    resp = None
    try:
        data = open(path, 'rb').read()
        start_time_req = time.time()
        resp = requests.post(endpoint,
                             data=data,
                             headers={'Content-Type': 'application/octet-stream', 'Prediction-Key': key})
        elapsed_time_req = time.time() - start_time
        if index % 10 == 0:
            print("elapsed time  for req # {0} is {1}".format(
                index, elapsed_time_req))
    except Exception as e:
        print("Error opening file or sending request")
        print(e.__doc__)
        print(e.message)
    if resp != None and resp.status_code == requests.codes.ok:
        jsonResponse = resp.json()
        try:
            predictedClass = jsonResponse["Classifications"][0]["Class"]
            predictedProb = jsonResponse["Classifications"][0]["Probability"]
            y_true.append(label)
            y_pred.append(predictedClass)
        except Exception as e:
            print("Error parsing json response")
            print(e.__doc__)
            print(e.message)
    else:
        print("Got status code {0} for image '{1}' label {2}".format(
            resp.status_code, path, label))

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
