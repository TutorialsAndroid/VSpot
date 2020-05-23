![](https://github.com/TutorialsAndroid/VSpot/blob/master/sample/src/main/res/mipmap-hdpi/ic_launcher.png)

# VSpot  [![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=15)     [![Known Vulnerabilities](https://snyk.io//test/github/TutorialsAndroid/VSpot/badge.svg?targetFile=sample/build.gradle)](https://snyk.io//test/github/TutorialsAndroid/VSpot?targetFile=sample/build.gradle) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-green.svg)](https://GitHub.com/TutorialsAndroid/VSpot) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-VSpot-orange.svg?style=flat)](https://android-arsenal.com/details/1/7898)


[`Heatic Debate App Download Now`](https://play.google.com/store/apps/details?id=com.asm.heatic)


## Jitpack.io

[![](https://jitpack.io/v/TutorialsAndroid/VSpot.svg)](https://jitpack.io/#TutorialsAndroid/VSpot)

This library allows to show intro of your app or a specific view that you want to high-light when you add new features to app.

## Hire-Me

<p align="center">Are you having trouble in your android projects then let me help you with it just Email me. I love my users, so feel free to visit http://asm.life

## And Don't Forget To Follow Me On Instagram

<p align="center">Follow me on instagram to stay up-to-date https://instagram.com/akshaysunilmasram 
  

## Sample Screen

![](https://github.com/TutorialsAndroid/VSpot/blob/master/art/device-2019-09-30-193059.png)

## Installation
	
gradle:
	
Add it in your root build.gradle at the end of repositories:
```groovy	
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```	
	Step 2. Add the dependency
```groovy	
  implementation 'com.github.TutorialsAndroid:VSpot:v2.2.19'
```
## Sample usage in your activity:

     new VSpotView.Builder(this)
             .setTitle("Spoti Title Text")
             .setContentText("Spoti Description Text\n .....Spoti Description Text\n .....Spoti Description Text .....")
             .setGravity(VSpotView.Gravity.AUTO) //optional
             .setDismissType(VSpotView.DismissType.outSide) //optional - default dismissable by TargetView
             .setTargetView(view)
             .setContentTextSize(12)//optional
             .setTitleTextSize(14)//optional
             .build()
             .show();
	     
## Change type face

 	 new VSpotView.Builder(this)
                .setTitle("Spoti Title Text")
                .setContentText("Spoti Description Text\n .....Spoti Description Text\n .....Spoti Description Text .....")
                .setTargetView(view)
                .setContentTypeFace(Typeface)//optional
                .setTitleTypeFace(Typeface)//optional
                .build()
                .show();
  
## Change title and Content text size

   	new VSpotView.Builder(this)
                .setTitle("Spoti Title Text")
                .setContentText("Spoti Description Text\n .....Spoti Description Text\n .....Spoti Description Text .....")
                .setTargetView(view)
                .setContentTextSize(12)//optional
                .setTitleTextSize(14)//optional
                .build()
                .show();
		
## Change Gravity

	new VSpotView.Builder(this)
             .setTitle("Spoti Title Text")
             .setContentText("Spoti Description Text\n .....Spoti Description Text\n .....Spoti Description Text .....")
             .setGravity(VSpotView.Gravity.CENTER)//optional
             .setTargetView(view) 
             .build()
             .show();
	     
	     
## use Spannable for Content
	
	 new VSpotView.Builder(this)
                .setTitle("Spoti Title Text")
                .setTargetView(view)
                .setContentSpan((Spannable) Html.fromHtml("<font color='red'>testing spannable</p>"))
                .build()
                .show();
                	     
## Set Listener 
	
      new VSpotView.Builder(MainActivity.this)
                      .setTitle("Spoti Title Text")
                      .setContentText("Spoti Description Text\n .....Spoti Description Text\n .....Spoti Description Text .....")
                      .setGravity(VSpotView.Gravity.CENTER)
                      .setTargetView(view1)
                      .setVSpotListener(new VSpotView.VSpotListener() {
                          @Override
                          public void onDismiss(View view) {
                             //TODO ...
                          }
                       })
                       .build()
                       .show();


### DismissType Attribute

| Type | Description |
| ------ | ------ |
| outside | Dismissing with click on outside of MessageView |
| anywhere | Dismissing with click on anywhere |
| targetView | Dismissing with click on targetView(targetView is assigned with setTargetView method) |

## License

* [Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

```
Copyright 2019 VSpot

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

