## 🛠 Available file templates

### Feature template

Feature template helps you speedup the creation of a new Feature. It will create these files for you: 

    .
    ├── domain
    │   ├── FeatureState.kt             
    │   ├── FeatureReducer.kt              
    │   ├── FeatureAction.kt             
    ├── ui
    │   ├── FeatureStoreViewModel.kt        
    │   ├── FeatureFragment.kt
    
    ├── res
    │   ├── fragment_feature.xml


**How to:** 
1. Create package for your feature. For example `calendar`

2. **Right click your package | New | Aurora | Feature** 

3. Use quick fix for importing you `R` in your fragment

4. Replace **TODO** with proper injection code in your fragment


## ⏬ How to import File Template to your Android Studio

Copy `aurora` folder to your file template folder

**MacOS:** `/Applications/Android Studio.app/Contents/plugins/android/lib/templates`

**Windows:** `{ANDROID_STUDIO_LOCATION}/plugins/android/lib/templates/`

And restart your Android Studio

## ⏫ How to export new File templates

1. Just copy your new file template to `aurora` folder

2. Let everyone know 📣


