package com.serranoie.android.buybuddy.ui.edit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.common.AlertDialogModal
import com.serranoie.android.buybuddy.ui.common.SlideToConfirm
import com.serranoie.android.buybuddy.ui.util.dateToString
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItemScreen(
    navController: NavController, itemId: Int, viewModel: EditItemViewModel = hiltViewModel()
) {
    var isLoading by remember { mutableStateOf(false) }
    val openAlertDialog = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val currentItem by viewModel.currentItem.collectAsState()
    val currentCategory by viewModel.categoryInfo.collectAsState()

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    LaunchedEffect(itemId) {
        viewModel.getItemById(itemId)
    }

    Scaffold(
        topBar = {
            MediumTopAppBar(title = {
                Text(
                    text = currentItem?.name ?: stringResource(R.string.loading),
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }, navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                    )
                }
            }, actions = {
                IconButton(onClick = {
                    openAlertDialog.value = true
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(R.string.delete_current_item),
                    )
                }
            }, scrollBehavior = scrollBehavior
            )
        }, modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier.background(
                        MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.medium,
                    ),
                ) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Outlined.Edit, contentDescription = stringResource(
                                R.string.edit_button_label
                            )
                        )
                    }
                }

                currentCategory?.let { category ->
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 16.dp,
                        ),
                    )
                } ?: Text(
                    text = stringResource(R.string.loading_category),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 16.dp,
                    ),
                )
            }


            Column(
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 24.dp,
                ),
            ) {
                Text(
                    text = stringResource(R.string.current_item_information),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    value = currentItem?.name ?: "",
                    onValueChange = { /* Handle state change */ },
                    trailingIcon = { Icons.Outlined.Edit },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    maxLines = 2,
                    textStyle = MaterialTheme.typography.titleLarge,
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    value = currentItem?.description ?: "",
                    onValueChange = { /* Handle state change */ },
                    trailingIcon = { Icons.Outlined.Edit },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    maxLines = 2,
                    textStyle = MaterialTheme.typography.titleLarge,
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    value = currentItem?.price.toString(),
                    onValueChange = { /* Handle state change */ },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    maxLines = 1,
                    textStyle = MaterialTheme.typography.titleLarge,
                )

                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .clickable {
                            // TODO: Display modal dialog
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shape = RoundedCornerShape(5.dp),
                ) {
                    Text(
                        text = currentItem?.usage ?: "",
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 16.dp,
                        ),
                        fontSize = 22.sp,
                    )
                }

                Text(
                    text = stringResource(R.string.benefits),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth(),
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .padding(vertical = 8.dp),
                    value = currentItem?.benefits ?: "",
                    onValueChange = { /* Handle on change */ },
                    trailingIcon = { Icons.Outlined.Edit },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    maxLines = 5,
                    textStyle = MaterialTheme.typography.bodyLarge,
                )

                Text(
                    text = stringResource(R.string.disadvantages),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth(),
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .padding(vertical = 8.dp),
                    value = currentItem?.disadvantages ?: "",
                    trailingIcon = { Icons.Outlined.Edit },
                    onValueChange = { /* Handle on change */ },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    maxLines = 5,
                    textStyle = MaterialTheme.typography.bodyLarge,
                )

                OutlinedCard(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth()
                        .clickable {
                            // TODO: Display modal dialog
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shape = RoundedCornerShape(5.dp),
                ) {
                    Text(
                        text = stringResource(
                            R.string.date_set_form, dateToString(currentItem?.reminderDate)
                        ),
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 16.dp,
                        ),
                        fontSize = 22.sp,
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                currentItem?.status?.let {
                    SlideToConfirm(
                        modifier = Modifier.padding(vertical = 16.dp),
                        isLoading = it,
                        currentStatus = currentItem?.status ?: false,
                        onUnlockRequested = {
                            if (!currentItem?.status!!) {
                                isLoading = true
                                coroutineScope.launch {
                                    currentItem?.itemId?.let {
                                        viewModel.updateItemStatus(
                                            it, true
                                        )
                                    }
                                }
                            }
                        },
                        onCancelPressed = {
                            if (currentItem?.status!!) {
                                isLoading = false
                                coroutineScope.launch {
                                    currentItem?.itemId?.let {
                                        viewModel.updateItemStatus(
                                            it, false
                                        )
                                    }
                                }
                            }
                        },
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                ) {
                    OutlinedButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .padding(horizontal = 8.dp),
                        onClick = { navController.navigateUp() },
                    ) {
                        Text(text = stringResource(R.string.cancel))
                    }

                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .padding(horizontal = 8.dp),
                        onClick = {
                            // TODO: Handle in viewmodel changes on textinputs
                            navController.navigateUp()
                        },
                    ) {
                        Text(text = stringResource(R.string.save))
                    }
                }

                if (openAlertDialog.value) {
                    AlertDialogModal(
                        onDismissRequest = { openAlertDialog.value = false },
                        onConfirmation = {
                            openAlertDialog.value = false
                            coroutineScope.launch {
                                currentItem?.itemId?.let { viewModel.deleteItem(it, navController) }
                            }
                        },
                        dialogTitle = stringResource(R.string.title_delete_dialog),
                        dialogText = stringResource(R.string.description_delete_dialog),
                        icon = Icons.Rounded.Info,
                    )
                }
            }
        }
    }
}