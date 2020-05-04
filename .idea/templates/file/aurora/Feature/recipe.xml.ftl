<?xml version="1.0" encoding="UTF-8"?>
<recipe>
  <instantiate from="res/layout/layout.xml.ftl" to="${escapeXmlAttribute(resOut)}/layout/${layoutName}.xml" />
  
  <instantiate from="src/app_package/Action.kt.ftl" to="${escapeXmlAttribute(srcOut)}/domain/${actionName}.kt" />
  <instantiate from="src/app_package/Reducer.kt.ftl" to="${escapeXmlAttribute(srcOut)}/domain/${reducerName}.kt" />
  <instantiate from="src/app_package/State.kt.ftl" to="${escapeXmlAttribute(srcOut)}/domain/${stateName}.kt" />

  <instantiate from="src/app_package/Fragment.kt.ftl" to="${escapeXmlAttribute(srcOut)}/ui/${fragmentName}.kt" />
  <instantiate from="src/app_package/StoreViewModel.kt.ftl" to="${escapeXmlAttribute(srcOut)}/ui/${storeViewModelName}.kt" />

  <open file="${escapeXmlAttribute(srcOut)}/ui/${fragmentName}.kt" />
</recipe>