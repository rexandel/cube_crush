package com.rexandel.cube_crush.ui.components.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.rexandel.cube_crush.data.repositories.UserRepositoryImpl
import com.rexandel.cube_crush.data.managers.StringResources
import com.rexandel.cube_crush.ui.components.common.ButtonColor
import com.rexandel.cube_crush.ui.components.common.PixelButton

@Composable
fun ChangeNicknameDialog(
    currentNickname: String,
    onDismiss: () -> Unit,
    onNicknameChanged: (String) -> Unit,
    userRepository: UserRepositoryImpl
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var newNickname by remember { mutableStateOf(currentNickname) }
    var errorMessage by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = StringResources.changeNickname,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = newNickname,
                    onValueChange = {
                        newNickname = it
                        errorMessage = ""
                    },
                    label = { Text(StringResources.newNickname) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = errorMessage.isNotEmpty()
                )

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PixelButton(
                    text = StringResources.cancel,
                    onClick = onDismiss,
                    buttonColor = ButtonColor.GREEN,
                    modifier = Modifier,
                    width = 120.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                PixelButton(
                    text = StringResources.save,
                    onClick = {
                        scope.launch {
                            when {
                                newNickname.isEmpty() -> {
                                    errorMessage = StringResources.nicknameEmpty(context)
                                    return@launch
                                }
                                newNickname.length < 3 -> {
                                    errorMessage = StringResources.nicknameTooShort(context)
                                    return@launch
                                }
                                newNickname == currentNickname -> {
                                    onDismiss()
                                    return@launch
                                }
                                userRepository.isNicknameExists(newNickname) -> {
                                    errorMessage = StringResources.nicknameExists(context)
                                    return@launch
                                }
                                else -> {
                                    try {
                                        userRepository.updateUserNickname(newNickname)
                                        onNicknameChanged(newNickname)
                                    } catch (e: java.io.IOException) {
                                        errorMessage = StringResources.getConnectionError(context)
                                    } catch (e: Exception) {
                                        errorMessage = e.message ?: StringResources.getUnknownError(context)
                                    }
                                }
                            }
                        }
                    },
                    buttonColor = ButtonColor.BLUE,
                    modifier = Modifier,
                    width = 120.dp
                )
            }
        },
        dismissButton = {}
    )
}