package com.serranoie.android.buybuddy.ui.edit

import android.view.View
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.domain.model.Category
import com.serranoie.android.buybuddy.domain.model.Item
import com.serranoie.android.buybuddy.ui.common.AlertDialogModal
import com.serranoie.android.buybuddy.ui.common.NumberOutlinedField
import com.serranoie.android.buybuddy.ui.common.SlideToConfirm
import com.serranoie.android.buybuddy.ui.common.TextOutlinedField
import com.serranoie.android.buybuddy.ui.common.TimePickerDialog
import com.serranoie.android.buybuddy.ui.core.analytics.UserEventsTracker
import com.serranoie.android.buybuddy.ui.util.UiConstants.basePadding
import com.serranoie.android.buybuddy.ui.util.UiConstants.extraSmallPadding
import com.serranoie.android.buybuddy.ui.util.UiConstants.largePadding
import com.serranoie.android.buybuddy.ui.util.UiConstants.mediumPadding
import com.serranoie.android.buybuddy.ui.util.UiConstants.smallPadding
import com.serranoie.android.buybuddy.ui.util.Utils.dateToString
import com.serranoie.android.buybuddy.ui.util.strongHapticFeedback
import com.serranoie.android.buybuddy.ui.util.toToast
import com.serranoie.android.buybuddy.ui.util.weakHapticFeedback
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import kotlin.math.roundToInt

private enum class DialogType { DELETE }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItemScreen(
    userEventsTracker: UserEventsTracker,
    navController: NavController,
    productInfo: Item?,
    categoryInfo: Category?,
    isLoading: Boolean,
    nameItemResponse: String,
    onNameItemResponse: (String) -> Unit,
    itemDescription: String,
    onItemDescriptionResponse: (String) -> Unit,
    itemPrice: Double?,
    onItemPriceResponse: (Double) -> Unit,
    itemBenefits: String,
    onItemBenefitsResponse: (String) -> Unit,
    itemDisadvantages: String,
    onItemDisadvantagesResponse: (String) -> Unit,
    selectedDateTime: Date?,
    onSelectedDateTimeResponse: (Date) -> Unit,
    itemUsage: Int,
    onItemUsageResponse: (Int) -> Unit,
    onItemStatusResponse: (Boolean) -> Unit,
    onUpdateItemEvent: (Int) -> Job,
    onDeleteItemEvent: (Int) -> Job,
) {
    val view = LocalView.current
    val openDialog = remember { mutableStateOf<DialogType?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    var isItemNameValid by remember { mutableStateOf(true) }
    var isItemDescriptionValid by remember { mutableStateOf(true) }
    var isValidPrice by remember { mutableStateOf(true) }
    var isBenefitsValid by remember { mutableStateOf(true) }
    var isDisadvantagesValid by remember { mutableStateOf(true) }

    val isFormValid by remember {
        derivedStateOf {
            isItemNameValid && isItemDescriptionValid && isValidPrice && isBenefitsValid && isDisadvantagesValid
        }
    }

    LaunchedEffect(Unit) {
        userEventsTracker.logCurrentScreen("edit_screen")
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.product_info),
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        userEventsTracker.logButtonAction("back_button")
                        view.strongHapticFeedback()
                        navController.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        userEventsTracker.logButtonAction("delete_button")
                        view.strongHapticFeedback()
                        openDialog.value = DialogType.DELETE
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = stringResource(R.string.delete_current_item),
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding),
        ) {
            AnimatedVisibility(
                visible = !isLoading,
                enter = fadeIn() + slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
                exit = fadeOut() + slideOutVertically(),
            ) {
                CategoryHolder(categoryInfo?.name)
            }

            AnimatedVisibility(
                visible = !isLoading,
                enter = fadeIn() + slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
                exit = fadeOut() + slideOutVertically(),
            ) {
                BasicInfoHolder(
                    userEventsTracker,
                    nameItemResponse,
                    onNameItemResponse,
                    onItemNameValid = { isItemNameValid = it },
                    itemDescription,
                    onItemDescriptionResponse,
                    onItemDescriptionValid = { isItemDescriptionValid = it },
                    itemPrice,
                    onIsValidPrice = { isValidPrice = it },
                    onItemPriceResponse,
                    itemUsage,
                    onItemUsageResponse,
                    view
                )
            }

            Column(
                modifier = Modifier.padding(
                    horizontal = basePadding,
                )
            ) {
                AnimatedVisibility(
                    visible = !isLoading,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    ReasonsHolder(
                        itemBenefits,
                        onItemBenefitsResponse,
                        onBenefitsValid = { isBenefitsValid = it },
                        itemDisadvantages,
                        onItemDisadvantagesResponse,
                        onDisadvantagesValid = { isDisadvantagesValid = it }
                    )
                }

                AnimatedVisibility(
                    visible = !isLoading,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    DateHolder(
                        userEventsTracker, selectedDateTime, onSelectedDateTimeResponse, view
                    )
                }

                AnimatedVisibility(
                    visible = !isLoading,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    ActionsHolder(
                        userEventsTracker,
                        productInfo?.status,
                        onItemStatusResponse,
                        navController,
                        productInfo?.itemId!!,
                        onUpdateItemEvent,
                        isFormValid,
                        view
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (productInfo?.name?.lowercase() == "dubai") {
                        Text(
                            text = stringResource(id = R.string.congrats_finding_this),
                            style = MaterialTheme.typography.labelSmall.copy(textAlign = TextAlign.Center)
                        )
                    }
                }

                when (openDialog.value) {
                    DialogType.DELETE -> {
                        AlertDialogModal(
                            onDismissRequest = { openDialog.value = null },
                            onConfirmation = {
                                navController.navigateUp()
                                openDialog.value = null

                                coroutineScope.launch {
                                    Timber.v("onDeleteItemEvent: " + productInfo?.itemId!!)
                                    onDeleteItemEvent(productInfo.itemId)
                                }
                            },
                            dialogTitle = stringResource(R.string.title_delete_dialog),
                            dialogText = stringResource(R.string.description_delete_dialog),
                            icon = Icons.Rounded.Info,
                        )
                    }

                    null -> {}
                }
            }
        }
    }
}

@Composable
private fun CategoryHolder(name: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = mediumPadding),
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
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = stringResource(
                        R.string.edit_button_label,
                    ),
                )
            }
        }

        Text(
            text = name ?: "Null",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(basePadding),
        )
    }
}

@Composable
private fun BasicInfoHolder(
    userEventsTracker: UserEventsTracker?,
    itemName: String,
    onItemNameResponse: (String) -> Unit,
    onItemNameValid: (Boolean) -> Unit,
    itemDescription: String,
    onItemDescriptionResponse: (String) -> Unit,
    onItemDescriptionValid: (Boolean) -> Unit,
    itemPrice: Double?,
    onIsValidPrice: (Boolean) -> Unit,
    onItemPriceResponse: (Double) -> Unit,
    itemUsage: Int,
    onItemUsageResponse: (Int) -> Unit,
    view: View,
) {
    var expanded by remember { mutableStateOf(false) }
    var priceText by remember { mutableStateOf(itemPrice.toString()) }

    val steps = listOf(
        R.string.usage_barely,
        R.string.usage_rarely,
        R.string.usage_ocasionally,
        R.string.usage_sometimes,
        R.string.usage_often,
        R.string.usage_almost_everyday,
    )

    val sliderRange = 0f..(steps.size - 1).toFloat()
    var sliderPosition by remember { mutableFloatStateOf(itemUsage.toFloat()) }

    LaunchedEffect(itemPrice) {
        priceText = itemPrice.toString()
    }

    Column(
        modifier = Modifier.padding(
            horizontal = basePadding,
            vertical = basePadding,
        ),
    ) {
        Text(
            text = stringResource(R.string.current_item_information),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(vertical = smallPadding)
                .fillMaxWidth(),
        )

        TextOutlinedField(
            value = itemName,
            onValueChange = onItemNameResponse,
            label = { Text(stringResource(id = R.string.name)) },
            modifier = Modifier.fillMaxWidth(),
            isValid = {
                val valid = it.isNotBlank() && it.matches(Regex("^[a-zA-Z0-9\\s]+$"))
                onItemNameValid(valid)
                valid
            },
            errorMessage = stringResource(id = R.string.field_required),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            maxLines = 2,
            textStyle = MaterialTheme.typography.titleLarge,
            shape = RoundedCornerShape(7.dp)
        )

        TextOutlinedField(
            value = itemDescription,
            onValueChange = onItemDescriptionResponse,
            label = { Text(stringResource(id = R.string.description)) },
            modifier = Modifier.fillMaxWidth(),
            isValid = {
                val valid = it.isNotBlank() && it.matches(Regex("^[a-zA-Z0-9\\s]+$"))
                onItemDescriptionValid(valid)
                valid
            },
            errorMessage = stringResource(id = R.string.field_required),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            maxLines = 2,
            textStyle = MaterialTheme.typography.titleLarge,
            shape = RoundedCornerShape(7.dp)
        )

        NumberOutlinedField(
            value = priceText,
            onValueChange = { newValue ->
                priceText = newValue
                onItemPriceResponse(newValue.toDoubleOrNull() ?: 0.0)
            },
            label = { Text(stringResource(id = R.string.price)) },
            modifier = Modifier.fillMaxWidth(),
            isValid = {
                val valid = it.isNotBlank() && it.matches(Regex("^\\d+(\\.\\d+)?$"))
                onIsValidPrice(valid)
                valid
            },
            errorMessage = stringResource(id = R.string.invalid_price_format),
            prefix = { Text("$") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            textStyle = MaterialTheme.typography.titleLarge,
            shape = RoundedCornerShape(7.dp)
        )

        Text(
            text = stringResource(R.string.usage_label),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = basePadding),
        )

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = smallPadding)
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing,
                    ),
                )
                .clickable {
                    userEventsTracker?.logButtonAction("usage_button")
                    view.weakHapticFeedback()
                    expanded = !expanded
                },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            shape = RoundedCornerShape(7.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = steps.getOrNull(itemUsage)?.let { stringResource(it) } ?: "Empty",
                    modifier = Modifier.padding(basePadding),
                    fontSize = 22.sp,
                )
            }

            if (expanded) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(basePadding),
                ) {
                    Slider(
                        value = sliderPosition,
                        valueRange = sliderRange,
                        steps = steps.size - 2,
                        onValueChange = { newValue ->
                            userEventsTracker?.logAdditionalInfo("new usage value: $newValue")
                            view.strongHapticFeedback()
                            sliderPosition = newValue
                            onItemUsageResponse(newValue.roundToInt())
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun ReasonsHolder(
    itemBenefits: String,
    onItemBenefitsResponse: (String) -> Unit,
    onBenefitsValid: (Boolean) -> Unit,
    itemDisadvantages: String,
    onItemDisadvantagesResponse: (String) -> Unit,
    onDisadvantagesValid: (Boolean) -> Unit
) {
    Column {

        TextOutlinedField(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(vertical = extraSmallPadding),
            value = itemBenefits,
            onValueChange = onItemBenefitsResponse,
            maxLines = 5,
            isValid = {
                val valid = it.isNotBlank() && it.matches(Regex("^[a-zA-Z0-9\\s]+$"))
                onBenefitsValid(valid)
                valid
            },
            label = { Text(stringResource(id = R.string.benefits)) },
            errorMessage = stringResource(id = R.string.field_required),
        )

        TextOutlinedField(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(vertical = extraSmallPadding),
            value = itemDisadvantages,
            onValueChange = onItemDisadvantagesResponse,
            maxLines = 5,
            isValid = {
                val valid = it.isNotBlank() && it.matches(Regex("^[a-zA-Z0-9\\s]+$"))
                onDisadvantagesValid(valid)
                valid
            },
            label = { Text(stringResource(id = R.string.disadvantages)) },
            errorMessage = stringResource(id = R.string.field_required),
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateHolder(
    userEventsTracker: UserEventsTracker?,
    selectedDateTime: Date?,
    onSelectedDateTimeResponse: (Date) -> Unit,
    view: View
) {
    val context = LocalContext.current

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDateTime?.time,
    )

    var showDatePicker by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState()
    var showTimePicker by remember { mutableStateOf(false) }

    var formattedDate by remember(selectedDateTime) {
        mutableStateOf(
            selectedDateTime?.let { date ->
                dateToString(date)
            } ?: "Invalid date",
        )
    }

    OutlinedCard(
        modifier = Modifier
            .padding(vertical = basePadding)
            .fillMaxWidth()
            .clickable {
                userEventsTracker?.logButtonAction("date_button")
                view.weakHapticFeedback()
                showDatePicker = true
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shape = RoundedCornerShape(7.dp),
    ) {
        Text(
            text = stringResource(R.string.date_set_form, formattedDate),
            modifier = Modifier.padding(basePadding),
            fontSize = 22.sp,
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        userEventsTracker?.logButtonAction("ok_date_button")
                        view.weakHapticFeedback()

                        val selectedDateMillis = datePickerState.selectedDateMillis
                        if (selectedDateMillis != null) {
                            val selectedCalendar = Calendar.getInstance().apply {
                                timeInMillis = selectedDateMillis
                                timeZone = TimeZone.getDefault()
                            }

                            val currentCalendar = Calendar.getInstance().apply {
                                timeZone = TimeZone.getDefault()
                            }

                            // Check if the selected date is different from the current date
                            if (selectedCalendar.get(Calendar.YEAR) != currentCalendar.get(Calendar.YEAR) || selectedCalendar.get(
                                    Calendar.DAY_OF_YEAR
                                ) != currentCalendar.get(Calendar.DAY_OF_YEAR)
                            ) {
                                // Add one day if the selected date is not the current day
                                selectedCalendar.add(Calendar.DAY_OF_YEAR, 1)
                            }

                            onSelectedDateTimeResponse(selectedCalendar.time)
                            showDatePicker = false
                            showTimePicker = true
                        }
                    },
                ) { Text(stringResource(R.string.ok)) }
            },
            dismissButton = {
                TextButton(onClick = {
                    userEventsTracker?.logButtonAction("cancel_date_button")
                    view.weakHapticFeedback()
                    showDatePicker = false
                }) {
                    Text(
                        stringResource(
                            id = R.string.cancel,
                        ),
                    )
                }
            },
        ) {
            DatePicker(
                state = datePickerState,
                dateValidator = { dateInMillis ->
                    dateInMillis >= System.currentTimeMillis()
                },
            )
        }
    }

    if (showTimePicker) {
        TimePickerDialog(
            onDismissRequest = {
                userEventsTracker?.logButtonAction("cancel_time_button")
                showTimePicker = false
            },
            confirmButton = {
                TextButton(onClick = {
                    selectedDateTime?.let { nonNullDate ->
                        val calendar = Calendar.getInstance(TimeZone.getDefault())
                        calendar.time = nonNullDate
                        calendar.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                        calendar.set(Calendar.MINUTE, timePickerState.minute)
                        onSelectedDateTimeResponse(calendar.time)
                        calendar.timeZone = TimeZone.getDefault()


                        if (calendar.after(Calendar.getInstance())) {
                            onSelectedDateTimeResponse(selectedDateTime)
                            showTimePicker = false
                            formattedDate = dateToString(selectedDateTime)
                            userEventsTracker?.logAdditionalInfo("selected date: $formattedDate")
                        } else {
                            context.getString(R.string.error_selected_date).toToast(context)
                        }
                    }
                    userEventsTracker?.logButtonAction("ok_time_button")
                    view.weakHapticFeedback()
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = {
                    userEventsTracker?.logButtonAction("cancel_time_button")
                    view.weakHapticFeedback()
                    showTimePicker = false
                }) {
                    Text(
                        stringResource(
                            id = R.string.cancel,
                        ),
                    )
                }
            },
        ) {
            TimePicker(state = timePickerState)
        }
    }
}

@Composable
private fun ActionsHolder(
    userEventsTracker: UserEventsTracker?,
    currentItemStatus: Boolean?,
    onItemStatusResponse: (Boolean) -> Unit,
    navController: NavController,
    itemId: Int,
    onUpdateItemEvent: (Int) -> Job,
    isFormValid: Boolean,
    view: View,
) {
    var isSlideToConfirmLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column {
        Spacer(modifier = Modifier.weight(1f))

        SlideToConfirm(
            modifier = Modifier.padding(vertical = 16.dp),
            isLoading = currentItemStatus ?: false,
            currentStatus = currentItemStatus ?: false,
            onUnlockRequested = {
                if (!currentItemStatus!!) {
                    isSlideToConfirmLoading = true
                    coroutineScope.launch {
                        onItemStatusResponse(true)
                    }
                }
                userEventsTracker?.logButtonAction("status_complete_slide")
                view.strongHapticFeedback()
            },
            onCancelPressed = {
                if (currentItemStatus!!) {
                    isSlideToConfirmLoading = false
                    coroutineScope.launch {
                        coroutineScope.launch {
                            onItemStatusResponse(false)
                        }
                    }
                }
                userEventsTracker?.logButtonAction("status_pending_slide")
                view.strongHapticFeedback()
            },
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = largePadding),
        ) {
            OutlinedButton(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .padding(horizontal = extraSmallPadding),
                onClick = {
                    userEventsTracker?.logButtonAction("cancel_button")
                    view.strongHapticFeedback()
                    navController.navigateUp()
                },
            ) {
                Text(text = stringResource(R.string.cancel))
            }

            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .padding(horizontal = extraSmallPadding),
                enabled = isFormValid,
                onClick = {
                    userEventsTracker?.logButtonAction("save_button")
                    view.strongHapticFeedback()
                    coroutineScope.launch {
                        onUpdateItemEvent(itemId)
                    }
                    navController.navigateUp()
                },
            ) {
                Text(text = stringResource(R.string.save))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditScreenPreview() {
    val view = LocalView.current

    Surface {
        Column {
            CategoryHolder(name = "Category")

            BasicInfoHolder(
                userEventsTracker = null,
                itemName = "Item",
                onItemNameResponse = { },
                onItemNameValid = { },
                itemDescription = "Description",
                onItemDescriptionResponse = { },
                onItemDescriptionValid = { },
                itemPrice = 150.0,
                onIsValidPrice = { },
                onItemPriceResponse = { },
                itemUsage = 2,
                onItemUsageResponse = { },
                view = view
            )

            DateHolder(
                userEventsTracker = null,
                selectedDateTime = Date(),
                onSelectedDateTimeResponse = { },
                view = view
            )
        }
    }
}