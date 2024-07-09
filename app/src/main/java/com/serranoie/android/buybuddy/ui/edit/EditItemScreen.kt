package com.serranoie.android.buybuddy.ui.edit

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.common.AlertDialogModal
import com.serranoie.android.buybuddy.ui.common.SlideToConfirm
import com.serranoie.android.buybuddy.ui.common.TimePickerDialog
import com.serranoie.android.buybuddy.ui.util.UiConstants.basePadding
import com.serranoie.android.buybuddy.ui.util.UiConstants.extraSmallPadding
import com.serranoie.android.buybuddy.ui.util.UiConstants.largePadding
import com.serranoie.android.buybuddy.ui.util.UiConstants.mediumPadding
import com.serranoie.android.buybuddy.ui.util.UiConstants.smallPadding
import com.serranoie.android.buybuddy.ui.util.dateToString
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItemScreen(
    navController: NavController,
    itemId: Int,
    viewModel: EditItemViewModel = hiltViewModel(),
) {
    var isLoading by remember { mutableStateOf(false) }
    val openDialog = remember { mutableStateOf<DialogType?>(null) }
    var expanded by remember { mutableStateOf(false) }

    val currentItem by viewModel.currentItem.collectAsState()
    val currentCategory by viewModel.categoryInfo.collectAsState()
    val itemName by viewModel.itemName.collectAsState()
    val itemDescription by viewModel.itemDescription.collectAsState()
    val itemPrice by viewModel.itemPrice.collectAsState()
    val itemBenefits by viewModel.itemBenefits.collectAsState()
    val itemDisadvantages by viewModel.itemDisadvantages.collectAsState()
    val selectedDateTime by viewModel.selectedDateTime.collectAsState()
    var isValidPrice by remember { mutableStateOf(true) }
    var priceText by remember { mutableStateOf(itemPrice.toString()) }

    val itemUsage by viewModel.itemUsage.collectAsState()

    var formattedDate by remember(currentItem?.reminderDate) {
        mutableStateOf(
            currentItem?.reminderDate?.let { date ->
                dateToString(date)
            } ?: "Invalid date",
        )
    }

    val datePickerState =
        rememberDatePickerState(
            initialSelectedDateMillis = selectedDateTime?.time,
        )

    var showDatePicker by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState()
    var showTimePicker by remember { mutableStateOf(false) }
    val selectedDate by viewModel.selectedDateTime.collectAsState()

    val steps =
        listOf(
            R.string.usage_barely,
            R.string.usage_rarely,
            R.string.usage_ocasionally,
            R.string.usage_sometimes,
            R.string.usage_often,
            R.string.usage_almost_everyday,
        )

    val sliderRange = 0f..(steps.size - 1).toFloat()
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(itemId) {
        viewModel.getItemById(itemId)
        currentItem?.categoryId?.let { viewModel.getCategory(it) }
    }

    LaunchedEffect(itemPrice) {
        priceText = itemPrice.toString()
    }

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.product_info),
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { openDialog.value = DialogType.DELETE }) {
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
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(padding),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = mediumPadding),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier =
                        Modifier.background(
                            MaterialTheme.colorScheme.secondaryContainer,
                            shape = MaterialTheme.shapes.medium,
                        ),
                ) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription =
                                stringResource(
                                    R.string.edit_button_label,
                                ),
                        )
                    }
                }

                currentCategory?.let { category ->
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium,
                        modifier =
                            Modifier.padding(basePadding),
                    )
                }
            }

            Column(
                modifier =
                    Modifier.padding(
                        horizontal = basePadding,
                        vertical = basePadding,
                    ),
            ) {
                Text(
                    text = stringResource(R.string.current_item_information),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier =
                        Modifier
                            .padding(vertical = smallPadding)
                            .fillMaxWidth(),
                )
                OutlinedTextField(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    value = itemName,
                    label = { Text(stringResource(id = R.string.name)) },
                    onValueChange = { viewModel.updateItemName(it) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    maxLines = 2,
                    textStyle = MaterialTheme.typography.titleLarge,
                    shape = RoundedCornerShape(7.dp),
                )

                OutlinedTextField(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = smallPadding),
                    value = itemDescription,
                    label = { Text(stringResource(id = R.string.description)) },
                    onValueChange = { viewModel.updateItemDescription(it) },
                    trailingIcon = { Icons.Outlined.Edit },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    maxLines = 2,
                    textStyle = MaterialTheme.typography.titleLarge,
                    shape = RoundedCornerShape(7.dp),
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(id = R.string.price)) },
                    value = priceText,
                    onValueChange = { newValue ->
                        priceText = newValue
                        isValidPrice = newValue.matches(Regex("^\\d+(\\.\\d{0,2})?$"))

                        if (isValidPrice) {
                            viewModel.updateItemPrice(newValue.toDoubleOrNull() ?: 0.0)
                        }
                    },
                    isError = !isValidPrice,
                    keyboardOptions =
                        KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Number,
                        ),
                    maxLines = 1,
                    textStyle = MaterialTheme.typography.titleLarge,
                    shape = RoundedCornerShape(7.dp),
                )

                if (!isValidPrice) {
                    Text(
                        text = stringResource(R.string.invalid_price_format),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = smallPadding),
                    )
                }

                Text(
                    text = stringResource(R.string.usage_label),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth().padding(top = basePadding),
                )

                OutlinedCard(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = smallPadding)
                            .animateContentSize(
                                animationSpec =
                                    tween(
                                        durationMillis = 300,
                                        easing = LinearOutSlowInEasing,
                                    ),
                            ).clickable { expanded = !expanded },
                    colors =
                        CardDefaults.cardColors(
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
                            text =
                                steps.getOrNull(itemUsage)?.let { stringResource(it) }
                                    ?: "Empty",
                            modifier = Modifier.padding(basePadding),
                            fontSize = 22.sp,
                        )
                    }

                    if (expanded) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(basePadding),
                        ) {
                            Slider(
                                value = sliderPosition,
                                valueRange = sliderRange,
                                steps = steps.size - 2,
                                onValueChange = { newValue ->
                                    sliderPosition = newValue
                                    viewModel.updateItemUsage(newValue.roundToInt())
                                },
                            )
                        }
                    }
                }

                Text(
                    text = stringResource(R.string.benefits),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodySmall,
                    modifier =
                        Modifier
                            .padding(vertical = extraSmallPadding)
                            .fillMaxWidth(),
                )

                OutlinedTextField(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(vertical = extraSmallPadding),
                    value = itemBenefits,
                    onValueChange = { viewModel.updateItemBenefits(it) },
                    trailingIcon = { Icons.Outlined.Edit },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    maxLines = 5,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    shape = RoundedCornerShape(7.dp),
                )

                Text(
                    text = stringResource(R.string.disadvantages),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodySmall,
                    modifier =
                        Modifier
                            .padding(vertical = 4.dp)
                            .fillMaxWidth(),
                )

                OutlinedTextField(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(vertical = extraSmallPadding),
                    value = itemDisadvantages,
                    trailingIcon = { Icons.Outlined.Edit },
                    onValueChange = { viewModel.updateItemDisadvantages(it) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    maxLines = 5,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    shape = RoundedCornerShape(7.dp),
                )

                OutlinedCard(
                    modifier =
                        Modifier
                            .padding(vertical = basePadding)
                            .fillMaxWidth()
                            .clickable {
                                showDatePicker = true
                            },
                    colors =
                        CardDefaults.cardColors(
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
                                    currentItem?.itemId?.let { id ->
                                        viewModel.updateItemStatus(id, true)
                                    }
                                }
                            }
                        },
                        onCancelPressed = {
                            if (currentItem?.status!!) {
                                isLoading = false
                                coroutineScope.launch {
                                    currentItem?.itemId?.let { id ->
                                        viewModel.updateItemStatus(id, false)
                                    }
                                }
                            }
                        },
                    )
                }

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = largePadding),
                ) {
                    OutlinedButton(
                        modifier =
                            Modifier
                                .weight(1f)
                                .height(48.dp)
                                .padding(horizontal = extraSmallPadding),
                        onClick = { navController.navigateUp() },
                    ) {
                        Text(text = stringResource(R.string.cancel))
                    }

                    Button(
                        modifier =
                            Modifier
                                .weight(1f)
                                .height(48.dp)
                                .padding(horizontal = extraSmallPadding),
                        onClick = {
                            coroutineScope.launch {
                                currentItem?.itemId.let {
                                    if (it != null) {
                                        viewModel.updateItem(it)
                                    }
                                }
                            }

                            navController.navigateUp()
                        },
                    ) {
                        Text(text = stringResource(R.string.save))
                    }
                }

                if (showDatePicker) {
                    DatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    val selectedDateMillis = datePickerState.selectedDateMillis
                                    if (selectedDateMillis != null) {
                                        val calendar = Calendar.getInstance(TimeZone.getDefault())
                                        calendar.timeInMillis = selectedDateMillis
                                        calendar.set(Calendar.HOUR_OF_DAY, 0)
                                        calendar.set(Calendar.MINUTE, 0)
                                        calendar.set(Calendar.SECOND, 0)
                                        calendar.set(Calendar.MILLISECOND, 0)

                                        viewModel.updateSelectedDateTime(calendar.time)

                                        showDatePicker = false
                                        showTimePicker = true
                                    }
                                },
                            ) { Text(stringResource(R.string.ok)) }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDatePicker = false }) {
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
                        onDismissRequest = { showTimePicker = false },
                        confirmButton = {
                            TextButton(onClick = {
                                selectedDate?.let { nonNullDate ->
                                    val calendar = Calendar.getInstance(TimeZone.getDefault())
                                    calendar.time = nonNullDate
                                    calendar.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                                    calendar.set(Calendar.MINUTE, timePickerState.minute)
                                    viewModel.updateSelectedDateTime(calendar.time)

                                    Log.d("DEBUG", "Selected Date (raw): $selectedDate")
                                    Log.d("DEBUG", "Calendar Time Zone: ${calendar.timeZone.id}")
                                    Log.d(
                                        "DEBUG",
                                        "Selected Date (formatted): ${dateToString(selectedDate!!)}",
                                    )

                                    if (calendar.after(Calendar.getInstance())) {
                                        viewModel.updateSelectedDateTime(selectedDate!!)
                                        showTimePicker = false
                                        formattedDate = dateToString(selectedDate!!)
                                    }
                                }
                            }) { Text("OK") }
                        },
                        dismissButton = {
                            TextButton(onClick = { showTimePicker = false }) {
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

                when (openDialog.value) {
                    DialogType.DELETE -> {
                        AlertDialogModal(
                            onDismissRequest = { openDialog.value = null },
                            onConfirmation = {
                                openDialog.value = null
                                coroutineScope.launch {
                                    currentItem?.itemId?.let {
                                        viewModel.deleteItem(it, navController)
                                    }
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

private enum class DialogType { DELETE }
