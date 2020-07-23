package com.toggl.settings.ui.composables

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Border
import androidx.ui.foundation.Box
import androidx.ui.foundation.Text
import androidx.ui.foundation.VerticalScroller
import androidx.ui.foundation.drawBackground
import androidx.ui.foundation.drawBorder
import androidx.ui.layout.Column
import androidx.ui.layout.padding
import androidx.ui.material.IconButton
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Scaffold
import androidx.ui.material.TopAppBar
import androidx.ui.material.icons.filled.ArrowBack
import androidx.ui.res.stringResource
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontWeight
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import androidx.ui.unit.sp
import com.toggl.models.domain.SettingsType
import com.toggl.settings.R
import com.toggl.settings.compose.ThemedPreview
import com.toggl.settings.domain.SettingsAction


data class LicenseUsageModel(
    val libraries: List<String>,
    val license: LicenseModel)

data class LicenseModel(
    val title: String,
    val text: String)

private val items = listOf(
    LicenseUsageModel(
        libraries = listOf("Library A"),
        license = LicenseModel(
            title = "The MIT License (MIT)",
            text = "Copyright (c) .NET Foundation Contributors\\r\\n\\r\\nPermission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the \\\"Software\\\"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:\\r\\n\\r\\nThe above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.\\r\\n\\r\\nTHE SOFTWARE IS PROVIDED \\\"AS IS\\\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.\\r\\n\\r\\n20160427")),
    LicenseUsageModel(
        libraries = listOf("Library with a really long name - so long that it spans over multiple lines of text", "Second library for the same license"),
        license = LicenseModel(
            title = "The MIT License (MIT)",
            text = "Copyright (c) .NET Foundation Contributors\\r\\n\\r\\nPermission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the \\\"Software\\\"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:\\r\\n\\r\\nThe above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.\\r\\n\\r\\nTHE SOFTWARE IS PROVIDED \\\"AS IS\\\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.\\r\\n\\r\\n20160427")),
    LicenseUsageModel(
        libraries = listOf("Library with a really long name - so long that it spans over multiple lines of text"),
        license = LicenseModel(
            title = "The MIT License (MIT)",
            text = "Copyright (c) .NET Foundation Contributors\\r\\n\\r\\nPermission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the \\\"Software\\\"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:\\r\\n\\r\\nThe above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.\\r\\n\\r\\nTHE SOFTWARE IS PROVIDED \\\"AS IS\\\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.\\r\\n\\r\\n20160427")),
    LicenseUsageModel(
        libraries = listOf("Library E", "Library F", "Library G", "Library H"),
        license = LicenseModel(
            title = "The MIT License (MIT)",
            text = "Copyright (c) .NET Foundation Contributors\\r\\n\\r\\nPermission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the \\\"Software\\\"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:\\r\\n\\r\\nThe above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.\\r\\n\\r\\nTHE SOFTWARE IS PROVIDED \\\"AS IS\\\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.\\r\\n\\r\\n20160427"))
)

@Composable
internal fun LicensesPage(
    dispatch: (SettingsAction) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.onSurface,
                title = { Text(stringResource(R.string.licenses)) },
                navigationIcon = {
                    IconButton(onClick = { dispatch(SettingsAction.SettingTapped(SettingsType.About)) }) { // todo: change the action
                        androidx.ui.foundation.Icon(androidx.ui.material.icons.Icons.Filled.ArrowBack)
                    }
                }
            )
        },
        bodyContent = { innerPadding ->
            VerticalScroller {
                Column(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    for (item in items) {
                        LibrariesUsingLicense(item.libraries, item.license)
                    }
                }
            }
        }
    )
}

@Composable
internal fun LibrariesUsingLicense(
    libraries: List<String>,
    license: LicenseModel
) {
    Column(modifier = Modifier.padding(16.dp)) {
        LibrariesTitle(libraries)
        LicenseBox(license.title, license.text)
    }
}

@Composable
internal fun LibrariesTitle(
    libraries: List<String>
) {
    Text(
        text = libraries.joinToString(separator = ",\n"),
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.sp
        ),
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable
internal fun LicenseBox(
    title: String,
    text: String
) {
    Box(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .drawBackground(MaterialTheme.colors.surface)
            .drawBorder(
                Border(
                    size = 1.dp,
                    color = MaterialTheme.colors.onSurface)
            )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = TextStyle(fontSize = 12.sp, lineHeight = 16.sp),
                modifier = Modifier.padding(bottom = 10.dp))
            Text(
                text = text,
                style = TextStyle(fontSize = 12.sp, lineHeight = 16.sp))
        }
    }
}

@Preview("Licenses light theme")
@Composable
fun PreviewLicensesPageLight() {
    ThemedPreview {
        LicensesPage()
    }
}

@Preview("Licenses dark theme")
@Composable
fun PreviewLicensesPageDark() {
    ThemedPreview(darkTheme = true) {
        LicensesPage()
    }
}