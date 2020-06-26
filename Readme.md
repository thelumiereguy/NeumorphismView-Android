# :sun_with_face: NeumorphismView :new_moon_with_face:  


### A set of Custom Viewgroup - FrameLayout and Constraintlayout that allows adding shadows/highlights to each of its children. You can customise the highlights, shadows, background color and stroke.  

<br></br>  

[![banner](https://user-images.githubusercontent.com/59196814/84686467-9b428700-af59-11ea-9fc2-14ca64f2eab3.png)  
](https://github.com/thelumiereguy/NeumorphismView-Android)  

[![Release](https://img.shields.io/badge/release-1.5-blue?style=for-the-badge)](https://jitpack.io/#thelumiereguy/NeumorphismView-Android) [![API](https://img.shields.io/badge/API-21%2B-orange?style=for-the-badge)](https://android-arsenal.com/api?level=21) [![Twitter](https://img.shields.io/badge/twitter-thelumiereguy-blue?style=for-the-badge)](https://twitter.com/thelumiereguy)  

# Table of Contents  

- [Example](#example)  
- [Installation](#installation)  
- [Features](#features)  
- [Usage](#usage)  
- [Contribute](#contribute)  
- [TODO](#todo)  
- [License](#license)  
  
# Example  

[![Iscreen](https://user-images.githubusercontent.com/59196814/84690464-26268000-af60-11ea-8589-0a027e215ba9.png)]()  

# Installation  

```gradle  
repositories {  
 maven { url 'https://jitpack.io' }  
}  
  
dependencies {  
 implementation 'com.github.thelumiereguy:NeumorphismView-Android:1.5'  
}  
```

# Features  

This comes with two custom viewgroups - Framelayout and Constraintlayout. When you just need to add the effect to a single view, use the Framelayout one and if you want to apply the effect to all children, use the Constrantlayout.  

*There are a few parameters to add a shadow to your view - deltaX, deltaY, color and shadow radius.*  

* DeltaX - Offset accross the X-Axis  
  
* DeltaY - Offset accross the Y-Axis  
  
* Shadow color - Color for the shadow, duh  
  
* Shadow radius - The blur radius used to render the layer's shadow  
  
# Usage  

### In case of NeumorphicCardView, the params are set to <ins>itself</ins>, but in case of NeumorphicConstraintLayout, the params are set to <ins>each of the children</ins>. (Refer to the example)  

- [Background](#background)  
- [Shadows](#shadows)  
- [Highlights](#highlights)  
- [Stroke](#stroke)  
  
#### Notes -  
* To enable preview in the design tab, set the "enable_preview" attribute.  
* To avoid the shadow being clipped by the parent view, you'll need to set the padding manually.  
  
  
## Background  

|                              | NeumorphicCardView      | NeumorphicConstraintLayout     |
| ---------------------------- | ----------------------- | ------------------------------ |
| BackgroundColor              | **neu_backgroundColor** | **layout_neu_backgroundColor** |
| BackgroundVerticalPadding    | **verticalPadding**     | **layout_verticalPadding**     |
| BackgroundHorizontalPadding  | **horizontalPadding**   | **layout_horizontalPadding**   |
| BackgroundCardRadius         | **cardRadius**          | **layout_cardRadius**          |

  

#### Note -  
 Background color is compulsory if you want to draw shadows/highlights as they dont work with Transparent color.

## Shadows  

|               | NeumorphicCardView | NeumorphicConstraintLayout |
| ------------- | ------------------ | -------------------------- |
| Enable Shadow | **enableShadow**   | **layout_enableShadow**    |
| DeltaX        | **shadowDx**       | **layout_shadowDx**        |
| DeltaY        | **shadowDy**       | **layout_shadowDy**        |
| Shadow color  | **shadowColor**    | **layout_shadowColor**     |
| Shadow radius | **shadowRadius**   | **layout_shadowRadius**    |

<br>  


![Shadows](https://user-images.githubusercontent.com/59196814/84823486-0b293e00-b03c-11ea-8bea-31cbf7c49660.png)  

  

## Highlights  

|                   | NeumorphicCardView  | NeumorphicConstraintLayout |
| ----------------- | ------------------- | -------------------------- |
| Enable Highlights | **enableHighlight** | **layout_enableHighlight** |
| DeltaX            | **highlightDx**     | **layout_highlightDx**     |
| DeltaY            | **highlightDy**     | **layout_highlightDy**     |
| Highlight color   | **highlightColor**  | **layout_highlightColor**  |
| Highlight radius  | **highlightRadius** | **layout_highlightRadius** |

<br>  


![HighlightsExample](https://user-images.githubusercontent.com/59196814/84823510-154b3c80-b03c-11ea-8dc8-4132053d1c91.png)  


## Stroke  

|               | NeumorphicCardView | NeumorphicConstraintLayout |
| ------------- | ------------------ | -------------------------- |
| Enable Stroke | **enableStroke**   | **layout_enableStroke**    |
| Stroke Color  | **stroke_color**   | **stroke_color**           |
| Stroke Width  | **stroke_width**   | **stroke_width**           |

</br>  

## Modify the Parameters Programmatically  

 <li> In case of NeumorphicConstraintLayout, the parameters are set to each of the children (Layout Params). Please use the following function on the direct children of the NeumorphicConstraintLayout like this -   

```kotlin  
childView.updateNeumorphicLayoutParams {  
                highlightColor = Color.RED  
                highlightRadius = 107F  
}  
```
<li> In case of NeumorphicCardView, you can directly modify the properties of the view itself.   

</br>  

# Contribute  

Feel free to fork this project, to optimise the code or to add new features.  

# TODO  
* Reduce redraw and improve performance  
  
# License  

     Copyright 2020 Piyush Pradeepkumar  
      
    Licensed under the Apache License, Version 2.0 (the "License");  
    you may not use this file except in compliance with the License.  
    You may obtain a copy of the License at  
      
       http://www.apache.org/licenses/LICENSE-2.0  
      
    Unless required by applicable law or agreed to in writing, software  
    distributed under the License is distributed on an "AS IS" BASIS,  
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  
    See the License for the specific language governing permissions and  
    limitations under the License.