# http://scikit-learn.org/stable/modules/model_evaluation.html
# PRECISION: What percent of positive predictions were correct? PR=TP/Total Predicted positive
# RECALL: What percent of the positive cases did you catch?  R=TP/Total Real Positive

from sklearn import metrics
import requests
import os
import base64
import pandas as pd
import sys

import numpy as np
import matplotlib.pyplot as plt
import itertools

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

MAX_Iterations = 20000
endpoint = "https://customvisionppe.azure-api.net/v1.0/Prediction/bccc6a44-3a32-4b0d-8d21-474bf5d57a1f/image?iterationId=f5b74fd3-b9ee-4dc1-8a3a-ebeada985626"
key = 'a5427c45494e4e1aaf339e7406995140'

print("Reading input csv with images paths and labels")
filePath = sys.argv[1] #"D:\\repos\\IRISDemo\\EvalWebservice\\EvalWebservice\\images.csv"
print("file: {0}".format(filePath))
df = pd.read_csv(filePath)

y_true = []
y_pred = []

for index, row in df.iterrows():
  if index > MAX_Iterations:
    break
  if index % 10 == 0:
    print("processing interation {0}...".format(index))
  path = row[0]
  label = row[1]
  data = open(path, 'rb').read()
  resp = requests.post(endpoint,
                    data=data,
                    headers={'Content-Type':'application/octet-stream', 'Prediction-Key': key})     
  jsonResponse = resp.json()
  predictedClass = jsonResponse["Classifications"][0]["Class"]
  predictedProb = jsonResponse["Classifications"][0]["Probability"]
  y_true.append(label)
  y_pred.append(predictedClass)

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