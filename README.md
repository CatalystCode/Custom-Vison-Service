# IRIS Demo

Sample Android code demoing how to make REST calls to Microsoft Cognitive Services IRIS

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See Running for how to run on your environment.

### Prerequisites

Android Studio installed

```
Download from https://developer.android.com/studio/index.html
```

### Installing

Clone this git repo
```
git clone https://github.com/User1m/IRISDemo.git
```

Change Directory into AndroidAPP
```
cd  AndroidApp
```
In Android Studio, choose "Open an existing Android Studio Project" and find the cloned directory from the previous step

## Setup

Update IRIS endpoint using your own endpoint url
```
Locate MainActivity.java
```

Update the following 
```
private final String ENDPOINT = "your-iris-endpoint-url"
```

Change your URL to use a string format placeholder.

Your url will look like this:
```
"https://customvisionppe.azure-api.net/v1.0/Prediction/bccc6a..../url?iterationId=f5b74fd3..."
```
Change to this:
```
"https://customvisionppe.azure-api.net/v1.0/Prediction/bccc6a..../%s?iterationId=f5b74fd3..."
```

Update code to use your Prediction Key
```
Locate helpers/HttpHelper.java
```

Find where the prediction-key header KVPair is set
```
requestBuilder.addHeader("Prediction-Key","a5427...");
```

Update that line touse your Prediction Key Value

## Running

Run on the emulator using Android Studio IDE or run on an actual device to utilize image capture functionality.

## Running the tests

Run Local and Instrumented tests on Android Studio by going to the test file and hitting the test run button on Androir Studio.

## Built With

* Android Studio
* [Android-Image-Cropper](https://github.com/ArthurHub/Android-Image-Cropper)

## Authors

* **Claudius Mbemba** - *Initial work* - [User1m](https://github.com/user1m)

See also the list of [contributors](https://github.com/User1m/IRISDemo/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see this [URL](https://opensource.org/licenses/MIT) file for details

## Acknowledgments

* https://github.com/ArthurHub/Android-Image-Cropper
