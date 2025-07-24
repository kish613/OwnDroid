package com.bintianqi.owndroid

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun StealthSettingsScreen(onNavigateUp: () -> Unit) {
    val context = LocalContext.current
    val sp = SharedPrefs(context)
    
    var isStealthEnabled by remember { 
        mutableStateOf(StealthManager.isStealthModeEnabled(context)) 
    }
    var showTokenDialog by remember { mutableStateOf(false) }
    var adbToken by remember { mutableStateOf("") }
    var showCommandsDialog by remember { mutableStateOf(false) }
    
    MyScaffold(R.string.stealth_mode, onNavigateUp, 0.dp) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Stealth Mode Toggle
            SwitchItem(
                title = R.string.enable_stealth_mode,
                checked = isStealthEnabled,
                onCheckedChange = { enabled ->
                    val success = StealthManager.setStealthMode(context, enabled)
                    if (success) {
                        isStealthEnabled = enabled
                    }
                }
            )
            
            // Generate ADB Token Button
            Button(
                onClick = { showTokenDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.generate_adb_token))
            }
            
            // Show ADB Commands Button
            FilledTonalButton(
                onClick = { showCommandsDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.show_adb_commands))
            }
            
            // Information Text
            Text(
                text = stringResource(R.string.stealth_mode_info),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
    
    // Generate Token Dialog
    if (showTokenDialog) {
        AlertDialog(
            onDismissRequest = { showTokenDialog = false },
            title = { Text(stringResource(R.string.adb_token)) },
            text = {
                Column {
                    Text(stringResource(R.string.adb_token_info))
                    Text(
                        text = StealthManager.generateAdbToken(context),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            },
            confirmButton = {
                Button(onClick = { showTokenDialog = false }) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }
    
    // ADB Commands Dialog
    if (showCommandsDialog) {
        val token = StealthManager.generateAdbToken(context)
        AlertDialog(
            onDismissRequest = { showCommandsDialog = false },
            title = { Text(stringResource(R.string.adb_commands)) },
            text = {
                Column {
                    Text(stringResource(R.string.adb_commands_info))
                    Text(
                        text = StealthManager.getHideCommand(context, token),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = StealthManager.getUnhideCommand(context, token),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            },
            confirmButton = {
                Button(onClick = { showCommandsDialog = false }) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }
}

@Composable
private fun SwitchItem(
    title: Int,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(stringResource(title))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
