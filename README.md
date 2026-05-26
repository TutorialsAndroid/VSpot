<div align="center">

# ✨ VSpot

### Professional Android Spotlight, Feature Discovery & Product Tour Library

Build beautiful onboarding flows, feature highlights, guided tours, coach marks, and contextual user education screens with a clean Java API.

<br>

[![](https://jitpack.io/v/TutorialsAndroid/VSpot.svg)](https://jitpack.io/#TutorialsAndroid/VSpot)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.tutorialsandroid/vspot)](https://central.sonatype.com/artifact/io.github.tutorialsandroid/vspot)
<br>

<a href="https://github.com/TutorialsAndroid/VSpot/releases/tag/v4.0.0">
  <img src="https://img.shields.io/badge/version-4.0.0-6750A4?style=for-the-badge" alt="Version 4.0.0" />
</a>
<a href="https://developer.android.com">
  <img src="https://img.shields.io/badge/Android-View%20System-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android View System" />
</a>
<a href="https://github.com/TutorialsAndroid/VSpot/blob/master/LICENSE">
  <img src="https://img.shields.io/badge/license-MIT-111827?style=for-the-badge" alt="License" />
</a>
<a href="https://github.com/sponsors/TutorialsAndroid">
  <img src="https://img.shields.io/badge/Sponsor-GitHub%20Sponsors-EA4AAA?style=for-the-badge&logo=githubsponsors&logoColor=white" alt="Sponsor" />
</a>

<br><br>

**VSpot v4.0.0** is a modern, production-ready Android spotlight overlay library designed for onboarding screens, feature discovery, app walkthroughs, and interactive product tours.

It is lightweight, Java-friendly, customizable, and built for real Android apps that need a polished onboarding experience without heavy dependencies.

</div>

---

## 📌 Table of Contents

* [Overview](#-overview)
* [What is VSpot?](#-what-is-vspot)
* [What is New in v4.0.0](#-what-is-new-in-v400)
* [Why Use VSpot?](#-why-use-vspot)
* [Installation](#-installation)
* [Quick Start](#-quick-start)
* [Single Spotlight Example](#-single-spotlight-example)
* [Multi-Step Product Tour](#-multi-step-product-tour)
* [Professional Theme Example](#-professional-theme-example)
* [Per-Step Customization](#-per-step-customization)
* [Callbacks](#-callbacks)
* [Manual Controls](#-manual-controls)
* [Spannable Content](#-spannable-content)
* [Dismiss Modes](#-dismiss-modes)
* [Gravity Options](#-gravity-options)
* [Spotlight Shapes](#-spotlight-shapes)
* [Style API](#%EF%B8%8F-style-api)
* [Builder API](#%EF%B8%8F-builder-api)
* [Migration Guide](#-migration-guide)
* [Best Practices](#-best-practices)
* [Troubleshooting](#%EF%B8%8F-troubleshooting)
* [Changelog](#-changelog)
* [License](#-license)
* [Support](#-support-the-project)

---

## 🚀 Overview

**VSpot** helps Android developers highlight important UI elements with a beautiful dimmed overlay, spotlight cutout, message card, step indicator, connector line, and optional action controls.

Use it for:

* App onboarding
* Product walkthroughs
* Feature discovery
* First-time user education
* Coach marks
* Highlighting buttons, cards, forms, icons, images, and custom views
* Explaining new updates after an app release
* Guiding users through complex screens

VSpot is built for the classic Android View system and works smoothly with Java-based Android projects.

---

## ✨ What is VSpot?

VSpot creates an overlay above your Activity and highlights a target view using a transparent spotlight area.

It can show a message card near the target view with:

* Title
* Description
* Step number
* Back button
* Next button
* Done button
* Skip button
* Connector line
* Target stroke
* Pulse effect

You can show one spotlight or create a complete multi-step guided tour.

---

## 🆕 What is New in v4.0.0

Version **4.0.0** is a major professional upgrade focused on real-world production usage, cleaner APIs, safer lifecycle handling, and better UI customization.

### Major v4 Features

| Feature                      | Description                                                                            |
| ---------------------------- | -------------------------------------------------------------------------------------- |
| Multi-step tours             | Create full onboarding flows using `addStep(...)`.                                     |
| Modern message card          | Rounded, elevated, Material-style message card.                                        |
| Step indicator               | Shows progress like `1 / 5`.                                                           |
| Action buttons               | Built-in Back, Next, Done, and Skip controls.                                          |
| Circle spotlight             | Perfect for icons, avatars, profile images, and FABs.                                  |
| Rounded rectangle spotlight  | Great for buttons, cards, inputs, and layouts.                                         |
| Connector line               | Draws a clean line between target and message card.                                    |
| Target stroke                | Adds a professional border around the highlighted target.                              |
| Pulse effect                 | Adds subtle focus around the target area.                                              |
| Per-step settings            | Each step can have its own shape, padding, gravity, and dismiss mode.                  |
| Theme API                    | Customize colors, radius, text sizes, card width, animation, and controls.             |
| Accessibility support        | Announces step content for accessibility services.                                     |
| Safe layout listener cleanup | Prevents unnecessary listener leaks.                                                   |
| Better drawing performance   | No bitmap allocation inside `onDraw()`.                                                |
| Safer touch handling         | Uses overlay-local target/message coordinates.                                         |
| Backward compatible API      | Existing `setTargetView`, `setTitle`, `setContentText`, and old enum names still work. |

---

## 💎 Why Use VSpot?

Many onboarding libraries are either too heavy, outdated, hard to style, or focused only on simple one-time hints. VSpot v4.0.0 is designed to be small, flexible, and production-friendly.

### Developer Benefits

* Simple Java API
* No complex setup
* No XML dependency required for the overlay
* Works with any existing View
* Supports single and multi-step flows
* Clean callbacks for analytics and app logic
* Customizable without subclassing
* Easy to publish as a reusable Android library

### UI/UX Benefits

* Professional spotlight overlay
* Attractive message cards
* Smooth animations
* Clear visual connection between message and target
* User-friendly Back, Next, Skip, and Done flow
* Better onboarding completion experience

---

## 📸 Screenshots

<div align="center">

<table>
  <tr>
    <td align="center" width="25%">
      <img src="/art/Screenshot_1779781388.png" width="220" alt="VSpot Smart Text Highlight" />
      <br />
      <b>VSpot Smart Text Highlight</b>
      <br />
      <sub>Highlight any TextView, Button, ImageView, or custom view with a polished overlay.</sub>
    </td>
    <td align="center" width="25%">
      <img src="/art/Screenshot_1779781391.png" width="220" alt="VSpot Image & Icon Support" />
      <br />
      <b>Image & Icon Support</b>
      <br />
      <sub>VSpot v4.0.0 works with images and dynamic layouts without manual x/y calculations.</sub>
    </td>
  </tr>    
    <td align="center" width="25%">
      <img src="/art/Screenshot_1779781393.png" width="220" alt="VSpot Action Area" />
      <br />
      <b>Action Area</b>
      <br />
      <sub>Use this for CTA buttons, filters, checkout buttons, or important actions..</sub>
    </td>
    <td align="center" width="25%">
      <img src="/art/Screenshot_1779781395.png" width="220" alt="VSpot Form Fields" />
      <br />
      <b>Form Fields</b>
      <br />
      <sub>Great for onboarding users through profile forms, search boxes, and input fields..</sub>
    </td>
    <td align="center" width="25%">
      <img src="/art/Screenshot_1779781397.png" width="220" alt="VSpot Grouped Views" />
      <br />
      <b>Grouped Views</b>
      <br />
      <sub>You can target an entire layout group, not only a single small child view..</sub>
    </td>
  </tr>
</table>

</div>

---

## 📦 Installation

### Maven Central

Add Maven Central in your project-level `settings.gradle` or `settings.gradle.kts`.

#### Gradle Groovy DSL

```gradle
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "YourProjectName"
include ':app'
```

Add VSpot in your app module `build.gradle`:

```gradle
dependencies {
    implementation "io.github.tutorialsandroid:vspot:4.0.0"
}
```

#### Gradle Kotlin DSL

```kotlin
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "YourProjectName"
include(":app")
```

Add VSpot in your app module `build.gradle.kts`:

```kotlin
dependencies {
    implementation("io.github.tutorialsandroid:vspot:4.0.0")
}
```

---

## 🔗 JitPack Installation

Add JitPack in your project-level `settings.gradle`:

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency:

```gradle
dependencies {
    implementation "com.github.TutorialsAndroid:VSpot:4.0.0"
}
```

For Kotlin DSL:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

```kotlin
dependencies {
    implementation("com.github.TutorialsAndroid:VSpot:4.0.0")
}
```

---

## ⚙️ Requirements

| Requirement                  | Value                                  |
| ---------------------------- | -------------------------------------- |
| Platform                     | Android                                |
| Language                     | Java                                   |
| UI system                    | Android View system                    |
| Dependency style             | Maven Central / JitPack / local module |
| AndroidX required by library | No                                     |
| Activity context required    | Yes                                    |

> VSpot must be created using an Activity context because it attaches the overlay to the Activity decor view.

---

## ⚡ Quick Start

Import VSpot:

```java
import com.developer.spoti.vspoti.VSpotView;
```

Show a spotlight:

```java
View targetButton = findViewById(R.id.btnContinue);

new VSpotView.Builder(this)
        .setTargetView(targetButton)
        .setTitle("Continue Button")
        .setContentText("Tap here to continue to the next step.")
        .setGravity(VSpotView.Gravity.center)
        .setDismissType(VSpotView.DismissType.targetView)
        .show();
```

That is it. VSpot will automatically attach itself above your Activity content and highlight the selected view.

---

## 🎯 Single Spotlight Example

Use this when you want to explain one specific UI element.

```java
final View profileButton = findViewById(R.id.profileButton);

VSpotView spot = new VSpotView.Builder(this)
        .setTargetView(profileButton)
        .setTitle("Your Profile")
        .setContentText("Manage your account, preferences, and app settings from here.")
        .setGravity(VSpotView.Gravity.center)
        .setDismissType(VSpotView.DismissType.outside)
        .setAccentColor(0xFF6750A4)
        .build();

spot.show();
```

---

## 🧭 Multi-Step Product Tour

Use `addStep(...)` to build a complete onboarding tour.

```java
View searchView = findViewById(R.id.searchView);
View filterButton = findViewById(R.id.filterButton);
View profileButton = findViewById(R.id.profileButton);
View createButton = findViewById(R.id.createButton);

VSpotView tour = new VSpotView.Builder(this)
        .addStep(searchView,
                "Smart Search",
                "Quickly find content, users, or features using the search bar.")
        .addStep(filterButton,
                "Powerful Filters",
                "Refine your results with category, date, and status filters.")
        .addStep(profileButton,
                "Your Profile",
                "Manage your personal information and app preferences.")
        .addStep(createButton,
                "Create New Item",
                "Tap here whenever you want to create something new.")
        .setDismissType(VSpotView.DismissType.none)
        .setGravity(VSpotView.Gravity.center)
        .setAccentColor(0xFF6750A4)
        .show();
```

### Why use `DismissType.none` for tours?

For guided onboarding, it is better to keep users inside the tour flow and let them use **Next**, **Back**, **Done**, or **Skip**. This prevents accidental dismissals.

---

## 🎨 Professional Theme Example

You can create a premium-looking branded tour by customizing the visual style.

```java
VSpotView.Style style = new VSpotView.Style();
style.overlayColor = 0xE6000000;
style.cardColor = 0xFFFFFFFF;
style.cardStrokeColor = 0x1F000000;
style.titleColor = 0xFF111827;
style.contentColor = 0xFF4B5563;
style.accentColor = 0xFF6750A4;
style.stepBadgeColor = 0xFF6750A4;
style.stepBadgeTextColor = 0xFFFFFFFF;
style.connectorColor = 0xFFFFFFFF;
style.targetStrokeColor = 0xFFFFFFFF;
style.targetPulseColor = 0x66FFFFFF;

style.cardCornerRadiusDp = 24f;
style.cardElevationDp = 14f;
style.cardShadowDyDp = 5f;
style.targetCornerRadiusDp = 18f;
style.spotlightPaddingDp = 10f;
style.maxMessageWidthDp = 360;

style.titleTextSizeSp = 19;
style.contentTextSizeSp = 14;
style.buttonTextSizeSp = 14;
style.stepTextSizeSp = 12;

style.showControls = true;
style.showConnector = true;
style.showTargetStroke = true;
style.showPulse = true;
style.showStepIndicator = true;
style.showPreviousButton = true;
style.showSkipButton = true;

style.previousButtonText = "Back";
style.nextButtonText = "Next";
style.doneButtonText = "Done";
style.skipButtonText = "Skip";

style.animationDuration = 260L;

VSpotView tour = new VSpotView.Builder(this)
        .setStyle(style)
        .addStep(findViewById(R.id.view1), "Welcome", "Let us quickly show you around.")
        .addStep(findViewById(R.id.view2), "Explore", "This section helps you discover important actions.")
        .addStep(findViewById(R.id.view3), "You are ready", "Start using the app with confidence.")
        .setDismissType(VSpotView.DismissType.none)
        .show();
```

---

## 🧩 Per-Step Customization

Each step can override global settings such as shape, padding, gravity, and dismiss behavior.

```java
VSpotView.Step searchStep = new VSpotView.Step.Builder(findViewById(R.id.searchView))
        .setTitle("Search Anything")
        .setContentText("Use this search field to quickly find what you need.")
        .setGravity(VSpotView.Gravity.center)
        .setSpotlightShape(VSpotView.SpotlightShape.roundedRectangle)
        .setSpotlightPaddingDp(8)
        .build();

VSpotView.Step avatarStep = new VSpotView.Step.Builder(findViewById(R.id.avatarView))
        .setTitle("Profile")
        .setContentText("Tap your avatar to open account settings.")
        .setGravity(VSpotView.Gravity.right)
        .setSpotlightShape(VSpotView.SpotlightShape.circle)
        .setSpotlightPaddingDp(10)
        .build();

new VSpotView.Builder(this)
        .addStep(searchStep)
        .addStep(avatarStep)
        .setDismissType(VSpotView.DismissType.none)
        .show();
```

---

## 🔔 Callbacks

Use callbacks to track analytics, update UI, or run logic when the tour starts, changes step, clicks the target, or ends.

```java
VSpotView tour = new VSpotView.Builder(this)
        .addStep(findViewById(R.id.view1), "Step One", "This is the first feature.")
        .addStep(findViewById(R.id.view2), "Step Two", "This is the second feature.")
        .setCallback(new VSpotView.Callback() {
            @Override
            public void onShow(VSpotView spotView) {
                // Tour became visible.
            }

            @Override
            public void onStepChanged(View targetView, int stepIndex, VSpotView.Step step) {
                // Called whenever the active step changes.
                // Useful for analytics: onboarding_step_viewed.
            }

            @Override
            public void onTargetClick(View targetView, int stepIndex, VSpotView.Step step) {
                // Called when the target view is clicked using DismissType.targetView.
            }

            @Override
            public void onDismiss(View lastTargetView, boolean completed, int lastStepIndex) {
                if (completed) {
                    // User completed the tour.
                } else {
                    // User skipped or dismissed the tour.
                }
            }
        })
        .show();
```

---

## 🎮 Manual Controls

VSpot exposes useful control methods for advanced use cases.

```java
VSpotView tour = new VSpotView.Builder(this)
        .addStep(findViewById(R.id.view1), "One", "First step")
        .addStep(findViewById(R.id.view2), "Two", "Second step")
        .addStep(findViewById(R.id.view3), "Three", "Third step")
        .build();

tour.show();

// Go to next step
tour.next();

// Go to previous step
tour.previous();

// Jump to a specific step
tour.goToStep(2);

// Finish as completed
tour.finish();

// Dismiss without completion
tour.dismiss();
```

Useful getters:

```java
boolean showing = tour.isShowing();
int currentIndex = tour.getCurrentIndex();
int totalSteps = tour.getStepCount();
VSpotView.Step currentStep = tour.getCurrentStep();
View currentTarget = tour.getCurrentTargetView();
```

---

## 📝 Spannable Content

You can pass rich text using `Spannable` or any `CharSequence`.

```java
SpannableString content = new SpannableString("Use filters to save time and find better results.");
content.setSpan(
        new StyleSpan(Typeface.BOLD),
        4,
        11,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
);

new VSpotView.Builder(this)
        .setTargetView(findViewById(R.id.filterButton))
        .setTitle("Smart Filters")
        .setContentSpan(content)
        .show();
```

Required imports:

```java
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
```

---

## 👆 Dismiss Modes

VSpot supports multiple dismiss behaviors.

| Dismiss Type             | Behavior                                                            |
| ------------------------ | ------------------------------------------------------------------- |
| `DismissType.none`       | User cannot dismiss by touching overlay. Best for controlled tours. |
| `DismissType.outside`    | Dismisses when user taps outside the message card.                  |
| `DismissType.anywhere`   | Dismisses when user taps anywhere outside the card.                 |
| `DismissType.targetView` | Performs target click, then dismisses.                              |

Both lowercase and uppercase enum names are supported:

```java
VSpotView.DismissType.none
VSpotView.DismissType.NONE

VSpotView.DismissType.outside
VSpotView.DismissType.OUTSIDE

VSpotView.DismissType.anywhere
VSpotView.DismissType.ANYWHERE

VSpotView.DismissType.targetView
VSpotView.DismissType.TARGET_VIEW
```

Recommended usage:

```java
// Best for onboarding tours
.setDismissType(VSpotView.DismissType.none)

// Best for simple hints
.setDismissType(VSpotView.DismissType.outside)

// Best when you want target click behavior
.setDismissType(VSpotView.DismissType.targetView)
```

---

## 🧲 Gravity Options

Gravity controls horizontal placement of the message card around the highlighted target.

| Gravity          | Behavior                               |
| ---------------- | -------------------------------------- |
| `Gravity.auto`   | Default smart positioning.             |
| `Gravity.center` | Centers message card to target center. |
| `Gravity.left`   | Aligns message card to target left.    |
| `Gravity.right`  | Aligns message card to target right.   |

Both lowercase and uppercase enum names are supported:

```java
VSpotView.Gravity.auto
VSpotView.Gravity.center
VSpotView.Gravity.left
VSpotView.Gravity.right

VSpotView.Gravity.AUTO
VSpotView.Gravity.CENTER
VSpotView.Gravity.LEFT
VSpotView.Gravity.RIGHT
```

Example:

```java
new VSpotView.Builder(this)
        .setTargetView(findViewById(R.id.settingsButton))
        .setTitle("Settings")
        .setContentText("Manage your preferences here.")
        .setGravity(VSpotView.Gravity.right)
        .show();
```

---

## 🔦 Spotlight Shapes

VSpot supports two spotlight shapes.

| Shape              | Best For                                     |
| ------------------ | -------------------------------------------- |
| `roundedRectangle` | Buttons, cards, input fields, layout blocks. |
| `circle`           | Icons, avatars, FABs, small image buttons.   |

Both lowercase and uppercase enum names are supported:

```java
VSpotView.SpotlightShape.roundedRectangle
VSpotView.SpotlightShape.circle

VSpotView.SpotlightShape.ROUNDED_RECTANGLE
VSpotView.SpotlightShape.CIRCLE
```

Example:

```java
new VSpotView.Builder(this)
        .setTargetView(findViewById(R.id.fabAdd))
        .setTitle("Create")
        .setContentText("Tap here to create a new item.")
        .setSpotlightShape(VSpotView.SpotlightShape.circle)
        .show();
```

---

## 🎛️ Style API

`VSpotView.Style` gives full control over the visual design.

### Colors

| Property             | Description                      |
| -------------------- | -------------------------------- |
| `overlayColor`       | Dim overlay color.               |
| `cardColor`          | Message card background color.   |
| `cardStrokeColor`    | Message card border color.       |
| `titleColor`         | Title text color.                |
| `contentColor`       | Content text color.              |
| `accentColor`        | Primary action/accent color.     |
| `connectorColor`     | Connector line color.            |
| `targetStrokeColor`  | Highlighted target border color. |
| `targetPulseColor`   | Target pulse color.              |
| `stepBadgeColor`     | Step indicator badge background. |
| `stepBadgeTextColor` | Step indicator badge text color. |

### Dimensions

| Property                   | Description                             |
| -------------------------- | --------------------------------------- |
| `cardCornerRadiusDp`       | Message card corner radius.             |
| `cardStrokeWidthDp`        | Message card border width.              |
| `cardElevationDp`          | Message card shadow/elevation strength. |
| `cardShadowDyDp`           | Vertical card shadow offset.            |
| `targetCornerRadiusDp`     | Target cutout corner radius.            |
| `targetStrokeWidthDp`      | Highlighted target stroke width.        |
| `spotlightPaddingDp`       | Space around highlighted view.          |
| `connectorWidthDp`         | Connector line width.                   |
| `connectorDotRadiusDp`     | Connector dot radius.                   |
| `indicatorGapDp`           | Gap between target and message card.    |
| `screenHorizontalMarginDp` | Horizontal safe margin.                 |
| `screenVerticalMarginDp`   | Vertical safe margin.                   |
| `maxMessageWidthDp`        | Maximum message card width.             |

### Text

| Property            | Description           |
| ------------------- | --------------------- |
| `titleTextSizeSp`   | Title text size.      |
| `contentTextSizeSp` | Content text size.    |
| `buttonTextSizeSp`  | Button text size.     |
| `stepTextSizeSp`    | Step badge text size. |
| `titleTypeface`     | Title typeface.       |
| `contentTypeface`   | Content typeface.     |

### Visibility Flags

| Property             | Description                  |
| -------------------- | ---------------------------- |
| `showControls`       | Show or hide action buttons. |
| `showPreviousButton` | Show or hide Back button.    |
| `showSkipButton`     | Show or hide Skip button.    |
| `showStepIndicator`  | Show or hide step badge.     |
| `showConnector`      | Show or hide connector line. |
| `showTargetStroke`   | Show or hide target border.  |
| `showPulse`          | Show or hide pulse outline.  |

### Button Text

| Property             | Default |
| -------------------- | ------- |
| `previousButtonText` | `Back`  |
| `nextButtonText`     | `Next`  |
| `doneButtonText`     | `Done`  |
| `skipButtonText`     | `Skip`  |

### Animation

| Property            | Description                                 |
| ------------------- | ------------------------------------------- |
| `animationDuration` | Overlay animation duration in milliseconds. |
| `startScale`        | Overlay scale at show start.                |
| `endScale`          | Overlay scale at dismiss end.               |

---

## 🏗️ Builder API

### Core Methods

| Method                                                         | Description                                 |
| -------------------------------------------------------------- | ------------------------------------------- |
| `setTargetView(View view)`                                     | Sets target view for a single spotlight.    |
| `setTitle(String title)`                                       | Sets title for single spotlight mode.       |
| `setContentText(String contentText)`                           | Sets plain text content.                    |
| `setContentSpan(Spannable span)`                               | Sets spannable content.                     |
| `setContent(CharSequence content)`                             | Sets any `CharSequence` content.            |
| `addStep(View targetView, String title, String contentText)`   | Adds a tour step with plain string content. |
| `addStep(View targetView, String title, CharSequence content)` | Adds a tour step with rich text content.    |
| `addStep(VSpotView.Step step)`                                 | Adds a fully customized step.               |
| `setGravity(Gravity gravity)`                                  | Sets default message gravity.               |
| `setDismissType(DismissType dismissType)`                      | Sets default dismiss behavior.              |
| `setCallback(VSpotView.Callback callback)`                     | Sets lifecycle callbacks.                   |
| `setStyle(VSpotView.Style style)`                              | Applies complete custom style.              |
| `build()`                                                      | Builds a `VSpotView` instance.              |
| `show()`                                                       | Builds and immediately shows VSpot.         |

### Fast Styling Methods

| Method                                                                   | Description                           |
| ------------------------------------------------------------------------ | ------------------------------------- |
| `setOverlayColor(int color)`                                             | Sets overlay color.                   |
| `setCardColor(int color)`                                                | Sets card color.                      |
| `setTitleColor(int color)`                                               | Sets title color.                     |
| `setContentColor(int color)`                                             | Sets content color.                   |
| `setAccentColor(int color)`                                              | Sets action/step accent color.        |
| `setConnectorColor(int color)`                                           | Sets connector line color.            |
| `setTargetStrokeColor(int color)`                                        | Sets highlighted target stroke color. |
| `setCardCornerRadiusDp(float radiusDp)`                                  | Sets card corner radius.              |
| `setTargetCornerRadiusDp(float radiusDp)`                                | Sets target corner radius.            |
| `setSpotlightPaddingDp(float paddingDp)`                                 | Sets global spotlight padding.        |
| `setSpotlightShape(SpotlightShape shape)`                                | Sets global spotlight shape.          |
| `setMaxMessageWidthDp(int widthDp)`                                      | Sets max card width.                  |
| `setShowControls(boolean showControls)`                                  | Shows/hides action controls.          |
| `setShowConnector(boolean showConnector)`                                | Shows/hides connector line.           |
| `setShowStepIndicator(boolean showStepIndicator)`                        | Shows/hides step badge.               |
| `setShowSkipButton(boolean showSkipButton)`                              | Shows/hides skip button.              |
| `setShowPreviousButton(boolean showPreviousButton)`                      | Shows/hides previous button.          |
| `setButtonTexts(String previous, String next, String done, String skip)` | Customizes button labels.             |
| `setAnimationDuration(long durationMillis)`                              | Sets animation duration.              |

---

## 🔁 Migration Guide

### From old single-step API to v4.0.0

Old usage:

```java
VSpotView vSpot = new VSpotView.Builder(this)
        .setTitle("Guide Title")
        .setContentText("Guide description")
        .setGravity(VSpotView.Gravity.center)
        .setDismissType(VSpotView.DismissType.outside)
        .setTargetView(view1)
        .build();

vSpot.show();
```

New v4 usage still supports the same style:

```java
VSpotView vSpot = new VSpotView.Builder(this)
        .setTargetView(view1)
        .setTitle("Guide Title")
        .setContentText("Guide description")
        .setGravity(VSpotView.Gravity.center)
        .setDismissType(VSpotView.DismissType.outside)
        .build();

vSpot.show();
```

### Recommended v4 tour usage

Instead of manually building the next spotlight inside `onDismiss`, use `addStep(...)`:

```java
new VSpotView.Builder(this)
        .addStep(view1, "Step 1", "This is the first feature.")
        .addStep(view2, "Step 2", "This is the second feature.")
        .addStep(view3, "Step 3", "This is the third feature.")
        .setDismissType(VSpotView.DismissType.none)
        .show();
```

### What changed internally in v4

* Better layout calculation
* Safer Activity context resolution
* Cleaner callback system
* No repeated bitmap creation during drawing
* Global layout listener cleanup
* More predictable target and overlay touch handling
* Professional UI controls built into the message card

---

## ✅ Best Practices

### 1. Show VSpot after layout is ready

If your target view is inside a complex layout, RecyclerView, ViewPager, or dynamic screen, call VSpot after the view has been measured.

```java
findViewById(R.id.myTarget).post(new Runnable() {
    @Override
    public void run() {
        new VSpotView.Builder(MainActivity.this)
                .setTargetView(findViewById(R.id.myTarget))
                .setTitle("Ready")
                .setContentText("This view is now ready to highlight.")
                .show();
    }
});
```

### 2. Use `DismissType.none` for onboarding tours

```java
.setDismissType(VSpotView.DismissType.none)
```

This avoids accidental dismissal and keeps the flow controlled.

### 3. Use circle shape only for compact targets

Circle spotlight works best for icons, avatars, FABs, and small image buttons.

```java
.setSpotlightShape(VSpotView.SpotlightShape.circle)
```

### 4. Keep content short and clear

Good onboarding content should be easy to read.

Recommended:

```text
Create posts, alerts, and reports from here.
```

Avoid:

```text
This button is used for the purpose of creating many different types of content and it can be clicked whenever you want to perform the creation process.
```

### 5. Track completion

Use callbacks to save whether the user completed or skipped onboarding.

```java
.setCallback(new VSpotView.Callback() {
    @Override
    public void onDismiss(View lastTargetView, boolean completed, int lastStepIndex) {
        getSharedPreferences("app_prefs", MODE_PRIVATE)
                .edit()
                .putBoolean("home_tour_completed", completed)
                .apply();
    }
})
```

### 6. Show onboarding only once

```java
SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
boolean completed = prefs.getBoolean("home_tour_completed", false);

if (!completed) {
    new VSpotView.Builder(this)
            .addStep(findViewById(R.id.view1), "Welcome", "Let us show you around.")
            .addStep(findViewById(R.id.view2), "Explore", "Discover important actions here.")
            .setDismissType(VSpotView.DismissType.none)
            .setCallback(new VSpotView.Callback() {
                @Override
                public void onDismiss(View lastTargetView, boolean completed, int lastStepIndex) {
                    prefs.edit().putBoolean("home_tour_completed", true).apply();
                }
            })
            .show();
}
```

---

## 🧪 Complete Activity Example

```java
package com.example.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.developer.spoti.vspoti.VSpotView;

public class MainActivity extends AppCompatActivity {

    private VSpotView onboardingTour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final View searchView = findViewById(R.id.searchView);
        final View filterButton = findViewById(R.id.filterButton);
        final View profileButton = findViewById(R.id.profileButton);
        final View createButton = findViewById(R.id.createButton);

        searchView.post(new Runnable() {
            @Override
            public void run() {
                showOnboardingIfNeeded(searchView, filterButton, profileButton, createButton);
            }
        });
    }

    private void showOnboardingIfNeeded(View searchView,
                                        View filterButton,
                                        View profileButton,
                                        View createButton) {

        final SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean alreadyCompleted = prefs.getBoolean("home_tour_completed", false);

        if (alreadyCompleted) {
            return;
        }

        VSpotView.Style style = new VSpotView.Style();
        style.accentColor = 0xFF6750A4;
        style.stepBadgeColor = 0xFF6750A4;
        style.cardCornerRadiusDp = 24f;
        style.targetCornerRadiusDp = 18f;
        style.spotlightPaddingDp = 10f;
        style.maxMessageWidthDp = 360;
        style.showControls = true;
        style.showConnector = true;
        style.showTargetStroke = true;
        style.showPulse = true;

        onboardingTour = new VSpotView.Builder(this)
                .setStyle(style)
                .addStep(searchView,
                        "Smart Search",
                        "Quickly find anything inside the app using search.")
                .addStep(filterButton,
                        "Filters",
                        "Use filters to narrow results and save time.")
                .addStep(new VSpotView.Step.Builder(profileButton)
                        .setTitle("Profile")
                        .setContentText("Manage your account and preferences from here.")
                        .setSpotlightShape(VSpotView.SpotlightShape.circle)
                        .setSpotlightPaddingDp(10)
                        .build())
                .addStep(createButton,
                        "Create New",
                        "Tap here to create your first item.")
                .setGravity(VSpotView.Gravity.center)
                .setDismissType(VSpotView.DismissType.none)
                .setCallback(new VSpotView.Callback() {
                    @Override
                    public void onDismiss(View lastTargetView, boolean completed, int lastStepIndex) {
                        prefs.edit()
                                .putBoolean("home_tour_completed", true)
                                .apply();
                    }
                })
                .build();

        onboardingTour.show();
    }

    @Override
    protected void onDestroy() {
        if (onboardingTour != null && onboardingTour.isShowing()) {
            onboardingTour.dismiss();
        }
        super.onDestroy();
    }
}
```

---

## 🛠️ Troubleshooting

### VSpot is not showing

Make sure:

* You are using an Activity context, not application context.
* Target view is not `null`.
* Target view is already measured.
* You call VSpot after `setContentView(...)`.

Correct:

```java
new VSpotView.Builder(this)
        .setTargetView(findViewById(R.id.myButton))
        .setTitle("Button")
        .setContentText("This is a highlighted button.")
        .show();
```

Incorrect:

```java
new VSpotView.Builder(getApplicationContext())
        .setTargetView(findViewById(R.id.myButton))
        .show();
```

### Target view position is wrong

Use `post(...)` so the target view has completed layout.

```java
View target = findViewById(R.id.myButton);
target.post(new Runnable() {
    @Override
    public void run() {
        new VSpotView.Builder(MainActivity.this)
                .setTargetView(target)
                .setTitle("Ready")
                .setContentText("Now the position is calculated correctly.")
                .show();
    }
});
```

### Spotlight is too tight

Increase spotlight padding:

```java
.setSpotlightPaddingDp(14f)
```

Or per step:

```java
new VSpotView.Step.Builder(targetView)
        .setTitle("Feature")
        .setContentText("Description")
        .setSpotlightPaddingDp(14)
        .build();
```

### Message card is too wide

Set max message width:

```java
.setMaxMessageWidthDp(300)
```

### User can dismiss the tour accidentally

Use:

```java
.setDismissType(VSpotView.DismissType.none)
```

### I want only a simple highlight without buttons

Use:

```java
new VSpotView.Builder(this)
        .setTargetView(findViewById(R.id.myTarget))
        .setTitle("Simple Hint")
        .setContentText("This is a simple spotlight message.")
        .setShowControls(false)
        .setShowStepIndicator(false)
        .setDismissType(VSpotView.DismissType.outside)
        .show();
```

---

## 📚 API Summary

```java
new VSpotView.Builder(activity)
        .setTargetView(view)
        .setTitle("Title")
        .setContentText("Content")
        .setGravity(VSpotView.Gravity.center)
        .setDismissType(VSpotView.DismissType.outside)
        .setSpotlightShape(VSpotView.SpotlightShape.roundedRectangle)
        .setSpotlightPaddingDp(8f)
        .setAccentColor(0xFF6750A4)
        .setCardCornerRadiusDp(22f)
        .setMaxMessageWidthDp(340)
        .setShowControls(true)
        .setShowConnector(true)
        .setShowStepIndicator(true)
        .setButtonTexts("Back", "Next", "Done", "Skip")
        .setAnimationDuration(260L)
        .show();
```

---

## 🧱 Local Module Usage

If you are using VSpot as a local Android library module:

### settings.gradle

```gradle
include ':app'
include ':vspoti'
```

### app/build.gradle

```gradle
dependencies {
    implementation project(':vspoti')
}
```

---

## 📣 Versioning

VSpot follows semantic versioning:

```text
MAJOR.MINOR.PATCH
```

Example:

```text
4.0.0
```

Meaning:

* `4` = Major version with important API/features update
* `0` = Minor feature version
* `0` = Patch/fix version

---

## 🧾 Changelog

### v4.0.0

* Added multi-step guided tours
* Added Back, Next, Done, and Skip controls
* Added step indicator badge
* Added professional card UI
* Added style customization API
* Added circle spotlight support
* Added rounded rectangle spotlight support
* Added connector line between target and card
* Added target stroke and pulse effect
* Added accessibility announcements
* Added callback system
* Added manual controls: `next()`, `previous()`, `goToStep()`, `finish()`, `dismiss()`
* Added per-step shape, padding, gravity, and dismiss behavior
* Improved layout positioning
* Improved touch handling
* Improved lifecycle cleanup
* Removed expensive bitmap allocation from drawing cycle
* Kept old single-step builder API compatible

---

## 🤝 Contributing

Contributions are welcome.

You can help by:

* Reporting bugs
* Suggesting new features
* Improving documentation
* Adding sample screens
* Testing on different Android versions and screen sizes
* Creating pull requests

### Development Flow

```bash
git clone https://github.com/TutorialsAndroid/VSpot.git
cd VSpot
```

Open the project in Android Studio, run the sample app, make your changes, and submit a pull request.

---

## ⭐ Support the Project

If VSpot helps you build better Android apps, please consider supporting the project.

* Star the repository
* Share it with other Android developers
* Use it in your apps
* Report issues clearly
* Contribute improvements
* Sponsor the project on GitHub

<div align="center">

<a href="https://github.com/sponsors/TutorialsAndroid">
  <img src="https://img.shields.io/badge/Sponsor%20This%20Project-GitHub%20Sponsors-EA4AAA?style=for-the-badge&logo=githubsponsors&logoColor=white" alt="Sponsor VSpot" />
</a>

</div>

---

## 📄 License

```text
MIT License

Copyright (c) TutorialsAndroid

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

<div align="center">

## Made with ❤️ for Android Developers

**VSpot** — Beautiful feature discovery and onboarding tours for Android apps.

<br>

<a href="https://github.com/TutorialsAndroid/VSpot">GitHub</a>
 •  <a href="https://github.com/TutorialsAndroid/VSpot/issues">Issues</a>
 •  <a href="https://github.com/TutorialsAndroid/VSpot/releases">Releases</a>
 •  <a href="https://github.com/sponsors/TutorialsAndroid">Sponsor</a>

</div>
