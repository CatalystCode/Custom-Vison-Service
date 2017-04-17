 # Food Classification Demo

Sample Android code demoing how to make REST calls to the Microsoft Custom Vison Service

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See Running for how to run on your environment.

### Prerequisites

Android Studio installed

```
Download from https://developer.android.com/studio/index.html
```

### Installing

Clone this git repo using command below or other tools such as Visual Studio.
```
git clone https://github.com/CatalystCode/Custom-Vison-Service.git
cd FoodClassification
```

In Android Studio, choose "Open an existing Android Studio Project" and find the cloned directory from the previous step.

Open the `AndroidAPP` folder.

## Setup

Update IRIS endpoint using your own endpoint url in the java file

Locate the following file:
```
MainActivity.java
```

Update the following ENDPOINT string
```
private final String ENDPOINT = "your-iris-endpoint-url"
```

Change your URL to use a string format placeholder. In your Custom Vison Service  portal, go to the Performance tab, select an Iteration on the left side and click on Evalution URL.

**Your url will look like this and either contain the string 'url' or 'image'.**
```
"https://customvisionppe.azure-api.net/v1.0/Prediction/bccc6a..../url?iterationId=f5b74fd3..."
```
**Change 'url?' or 'image?' to "%s?". The code will replace "%s" with either "url" or "image" automatically.**
```
"https://customvisionppe.azure-api.net/v1.0/Prediction/bccc6a..../%s?iterationId=f5b74fd3..."
```

Update code to use your Prediction Key in the java file

Locate the following file:
```
helpers/HttpHelper.java
```

Find where the prediction-key header KVPair is set
```
requestBuilder.addHeader("Prediction-Key","a5427...");
```

Update that line to use your Prediction Key Value.
At this point the app will work with the Microsoft Custom Vision Service just fine.
But if you want to use the [Nutritionix Api](https://www.nutritionix.com/business/api) to see nutrition information then update the following...

Locate the following file:
```
MainActivity.java
```

Find the `requestNutritionInfo()` method and the where the Nutritionix appId & appKey are set:
```
nutriRequest.setParam("appId", "f72...");
nutriRequest.setParam("appKey", "9927906...");
```
Update those to reflect your Nutritionix App ID & Key.

## Running

Run on the emulator using Android Studio IDE or run on an actual device to utilize image capture functionality. 

Note: you will need to create an Android Virtual Device if you cannot find an emulator you are looking for. 

For Windows users, make sure that you turn off Hyper-V feature using "Turn Windows features on or off". Or, run the command line using local admin, bcdedit /set hypervisorlaunchtype off.

## Running the tests

Run Local and Instrumented tests on Android Studio by going to the test file and hitting the test run button on Android Studio.

## Built With

* Android Studio
* [Android-Image-Cropper](https://github.com/ArthurHub/Android-Image-Cropper)

## Authors

* **Claudius Mbemba** - *Android Application* - [User1m](https://github.com/user1m)
* **Olga Liakhovich** - *Custom Vison Service Training* - [olgaliak](https://github.com/olgaliak)
<!-- See also the list of [contributors](https://github.com/User1m/IRISDemo/contributors) who participated in this project. -->

## License

This project is licensed under the MIT License - see this [URL](https://opensource.org/licenses/MIT) for details

## Acknowledgments

* https://github.com/ArthurHub/Android-Image-Cropper
