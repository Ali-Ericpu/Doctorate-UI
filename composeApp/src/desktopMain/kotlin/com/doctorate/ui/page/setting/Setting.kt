package com.doctorate.ui.page.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.doctorate.ui.config.LocalAppConfig
import com.doctorate.ui.view.FileDialog
import doctorateui.composeapp.generated.resources.Res
import doctorateui.composeapp.generated.resources.app_pack_name
import doctorateui.composeapp.generated.resources.custom_path
import doctorateui.composeapp.generated.resources.dark_mode
import doctorateui.composeapp.generated.resources.emulator_path
import doctorateui.composeapp.generated.resources.reset
import doctorateui.composeapp.generated.resources.save
import doctorateui.composeapp.generated.resources.select
import org.jetbrains.compose.resources.stringResource

/**
 * ClassName: Setting
 * Package: com.doctorate.ui.page
 * Description:
 * @author Raincc
 * @Create 2024/8/11 20:23
 * @Version 1.0
 */
@Composable
fun Setting() {
    val config = LocalAppConfig.current.config
    val onConfigChange = LocalAppConfig.current.onConfigChange
    Column {
        EditPathColumn(
            value = config.emulatorPath,
            label = stringResource(Res.string.emulator_path),
            onValueChange = { it?.let { onConfigChange(config.copy(emulatorPath = it)) } }
        )
        EditPathColumn(
            value = config.customPath,
            label = stringResource(Res.string.custom_path),
            onValueChange = { it?.let { onConfigChange(config.copy(customPath = it)) } }
        )
        EditValueRow(
            value = config.appPackageName,
            leading = stringResource(Res.string.app_pack_name),
            onValueSave = { onConfigChange(config.copy(appPackageName = it)) }
        )
        EditSwitch(
            content = stringResource(Res.string.dark_mode),
            state = config.darkMode,
            onCheckedChange = { onConfigChange(config.copy(darkMode = it)) }
        )
    }
}

@Composable
fun EditPathColumn(
    value: String,
    label: String,
    onValueChange: (String?) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(color = Color.LightGray)
            .padding(8.dp)
            .height(140.dp),
    ) {
        OutlinedTextField(
            singleLine = true,
            readOnly = true,
            value = value,
            onValueChange = {},
            label = { Text(text = label, color = Color.Black) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.Black,
                backgroundColor = Color.LightGray,
                unfocusedBorderColor = MaterialTheme.colors.onSurface,
                focusedBorderColor = Color.Black,
            ),
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        Row {
            val modifier = Modifier
                .fillMaxHeight()
                .width(100.dp)
                .clip(RoundedCornerShape(8.dp))
                .padding(8.dp)
            Button(
                onClick = { showDialog = true },
                modifier = modifier
            ) {
                Text(text = stringResource(Res.string.select))
                if (showDialog) {
                    FileDialog(
                        onCloseRequest = {
                            showDialog = false
                            onValueChange(it)
                        },
                    )
                }

            }
            Button(
                onClick = { onValueChange("") },
                modifier = modifier
            ) {
                Text(text = stringResource(Res.string.reset))
            }
        }
    }

}

@Composable
fun EditValueRow(
    value: String,
    leading: String,
    onValueSave: (String) -> Unit,
) {
    var value by remember { mutableStateOf(value) }
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(color = Color.LightGray)
            .padding(8.dp)
            .height(56.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            singleLine = true,
            value = value,
            onValueChange = { value = it },
            leadingIcon = { Text(text = leading, color = Color.Black, modifier = Modifier.padding(start = 8.dp)) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.Black,
                backgroundColor = Color.LightGray,
                unfocusedBorderColor = MaterialTheme.colors.onSurface,
                focusedBorderColor = Color.Black,
            ),
            modifier = Modifier.width(560.dp)
        )
        Button(
            onClick = { onValueSave(value) },
            modifier = Modifier
                .fillMaxHeight()
                .width(100.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            Text(text = stringResource(Res.string.save))
        }
    }

}

@Composable
fun EditSwitch(
    content: String,
    state: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    var state by remember { mutableStateOf(state) }
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(color = Color.LightGray)
            .padding(8.dp)
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = content, color = Color.Black, modifier = Modifier.padding(8.dp).fillMaxHeight())
        Switch(
            checked = state,
            onCheckedChange = { onCheckedChange(it.also { state = it }) }
        )
    }
}