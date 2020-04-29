## 🛠 Available live templates 

Abbreviation | Generated code
------------ | -------------
`rtest` | `@Test` annotated method with `reducer.testReduce` template code
`rstest` | `@Test` annotated method with `reducer.testReduceState` template code
`retest` | `@Test` annotated method with `reducer.testReduceEffects` template code

## ⏬ How to import Live Template settings to your Android Studio

1. **Choose File | Import Settings** from the menu.

2. Specify the path to the archive `liveTemplateSettings.zip` in this folder.

3. In the **Import Settings dialog**, select the **Live templates** checkbox and click OK.

After restarting IntelliJ IDEA, you will see the imported live templates on the **Editor | Live Templates page** of IntelliJ IDEA settings. All newly imported live templates should now appear under **Aurora** template group.

## ⏫ How to export new Live Templates

1. Make sure you have the latest set of live templates imported in your Android Studio

2. Choose **File | Manage IDE Settings | Export Settings** from the menu.

3. In the **Export Settings dialog**, make sure that the **Live templates(schemes)** checkbox is selected and specify the path and name of the archive, where the exported settings will be saved.

4. Click OK to generate the file based on live template configuration files.

5. Overwrite the `liveTemplateSettings.zip` in this folder with your new settings export.

6. Update the first section of this readme with your new live templates

7. Let everyone know 📣
