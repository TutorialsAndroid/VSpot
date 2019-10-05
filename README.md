![](https://github.com/TutorialsAndroid/VSpot/blob/master/sample/src/main/res/mipmap-hdpi/ic_launcher.png)

# VSpot  [![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)     [![Known Vulnerabilities](https://snyk.io//test/github/TutorialsAndroid/VSpot/badge.svg?targetFile=sample/build.gradle)](https://snyk.io//test/github/TutorialsAndroid/VSpot?targetFile=sample/build.gradle) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-CrashX-red.svg?style=flat-square)](https://android-arsenal.com/details/1/7581) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-green.svg)](https://GitHub.com/TutorialsAndroid/VSpot)

This library allows to show intro of your app or a specific view that you want to high-light when you add new features to app.

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
compile 'com.github.TutorialsAndroid:Spoti:v1.0.19'
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
