package com.serranoie.android.buybuddy.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.SwipeProgress
import androidx.wear.compose.material.SwipeableDefaults
import androidx.wear.compose.material.SwipeableState
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.util.UiConstants.commonCornerRadius
import com.serranoie.android.buybuddy.ui.util.UiConstants.sliderHeight
import com.serranoie.android.buybuddy.ui.util.UiConstants.smallPadding
import kotlin.math.roundToInt

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun SlideToConfirm(
    isLoading: Boolean,
    onUnlockRequested: () -> Unit,
    modifier: Modifier = Modifier,
    currentStatus: Boolean,
    onCancelPressed: () -> Unit,
): Boolean {
    val hapticFeedback = LocalHapticFeedback.current
    val swipeState =
        rememberSwipeableState(
            initialValue = Anchor.Start,
            confirmStateChange = { anchor ->
                if (anchor == Anchor.End) {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    onUnlockRequested()
                    true
                } else {
                    false
                }
            },
        )

    val swipeFraction by remember {
        derivedStateOf { calculateSwipeFraction(swipeState.progress) }
    }

    LaunchedEffect(isLoading) {
        swipeState.animateTo(if (isLoading) Anchor.End else Anchor.Start)
    }

    Track(
        swipeState = swipeState,
        swipeFraction = swipeFraction,
        enabled = !isLoading,
        modifier = modifier,
    ) {
        Hint(
            text = stringResource(R.string.swipe_to_confirm),
            swipeFraction = swipeFraction,
            isLoading = isLoading,
            onCancelPressed = onCancelPressed,
            modifier =
            Modifier
                .align(Alignment.Center)
                .padding(PaddingValues(horizontal = Thumb.Size + smallPadding))
                .fillMaxWidth(),
        )

        Thumb(
            isLoading = isLoading,
            modifier =
            Modifier.offset {
                IntOffset(swipeState.offset.value.roundToInt(), 0)
            },
            onCancelPressed = onCancelPressed,
        )
    }

    return swipeState.offset.value == Track.EndOfTrackPx
}

@OptIn(ExperimentalWearMaterialApi::class)
fun calculateSwipeFraction(progress: SwipeProgress<Anchor>): Float {
    val atAnchor = progress.from == progress.to
    val fromStart = progress.from == Anchor.Start
    return if (atAnchor) {
        if (fromStart) 0f else 1f
    } else {
        if (fromStart) progress.fraction else 1f - progress.fraction
    }
}

enum class Anchor { Start, End }

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun Track(
    swipeState: SwipeableState<Anchor>,
    swipeFraction: Float,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable (BoxScope.() -> Unit),
) {
    val density = LocalDensity.current
    var fullWidth by remember { mutableIntStateOf(0) }
    var endOfTrackPx by remember { mutableStateOf(0f) } // Store endOfTrackPx as state

    val horizontalPadding = smallPadding

    val startOfTrackPx = 0f

    val snapThreshold = 0.8f
    val thresholds = { from: Anchor, _: Anchor ->
        if (from == Anchor.Start) {
            FractionalThreshold(snapThreshold)
        } else {
            FractionalThreshold(1f - snapThreshold)
        }
    }

    LaunchedEffect(fullWidth) {
        if (fullWidth > 0) {
            endOfTrackPx = with(density) { fullWidth - (2 * horizontalPadding + Thumb.Size).toPx() }
        }
    }

    if (endOfTrackPx > 0) {
        Box(
            modifier =
            modifier
                .onSizeChanged { fullWidth = it.width }
                .height(sliderHeight)
                .fillMaxWidth()
                .swipeable(
                    enabled = enabled,
                    state = swipeState,
                    orientation = Orientation.Horizontal,
                    anchors =
                    mapOf(
                        startOfTrackPx to Anchor.Start,
                        endOfTrackPx to Anchor.End,
                    ),
                    thresholds = thresholds, velocityThreshold = Track.VelocityThreshold,
                )
                .background(
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(percent = 10),
                )
                .padding(
                    PaddingValues(
                        horizontal = horizontalPadding,
                        vertical = smallPadding,
                    ),
                ),
            content = content,
        )
    } else {
        // Placeholder or loading state while endOfTrackPx is being calculated
        Box(
            modifier = modifier
                .onSizeChanged { fullWidth = it.width }
                .height(sliderHeight)
                .fillMaxWidth()
        )
    }
}

@Composable
fun Thumb(
    isLoading: Boolean,
    modifier: Modifier,
    onCancelPressed: () -> Unit,
) {
    Box(
        modifier =
        modifier
            .size(Thumb.Size)
            .clickable { if (isLoading) onCancelPressed() }
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(commonCornerRadius),
            )
            .padding(smallPadding),
    ) {
        if (isLoading) {
            Icon(imageVector = Icons.Rounded.Done, contentDescription = "Mark it purchased")
        } else {
            Icon(imageVector = Icons.Rounded.KeyboardArrowRight, contentDescription = "Confirm")
        }
    }
}

@Composable
fun Hint(
    text: String,
    swipeFraction: Float,
    isLoading: Boolean,
    onCancelPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val hintText = if (isLoading) stringResource(R.string.tap_to_cancel) else text
    val hintColor =
        if (isLoading) MaterialTheme.colorScheme.surface else calculateHintTextColor(swipeFraction)

    Text(
        text = hintText,
        color = hintColor,
        style = MaterialTheme.typography.titleSmall,
        modifier =
        modifier
            .clickable { if (isLoading) onCancelPressed() }
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center),
    )
}

@Composable
fun calculateHintTextColor(swipeFraction: Float): Color {
    val endOfFadeFraction = 0.35f
    val fraction = (swipeFraction / endOfFadeFraction).coerceIn(0f..1f)
    return lerp(
        MaterialTheme.colorScheme.surface,
        MaterialTheme.colorScheme.surface.copy(alpha = 0f),
        fraction,
    )
}

private object Thumb {
    val Size = 40.dp
}

private object Track {
    @OptIn(ExperimentalWearMaterialApi::class)
    val VelocityThreshold = SwipeableDefaults.VelocityThreshold * 10
    var EndOfTrackPx: Float = 0f
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun Preview() {
    var isLoading by remember { mutableStateOf(false) }

    Surface {
        Column(
            verticalArrangement = Arrangement.Absolute.spacedBy(smallPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(modifier = Modifier.width(IntrinsicSize.Max)) {
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = "Normal")
                    Spacer(modifier = Modifier.weight(1f))
                    Thumb(isLoading = false, modifier = Modifier, onCancelPressed = { })
                }

                SlideToConfirm(
                    isLoading = isLoading,
                    onUnlockRequested = { isLoading = true },
                    onCancelPressed = { isLoading = false },
                    currentStatus = false,
                )

                Spacer(modifier = Modifier.padding(smallPadding))

                SlideToConfirm(
                    isLoading = !isLoading,
                    onUnlockRequested = { isLoading = false },
                    onCancelPressed = { isLoading = false },
                    currentStatus = true,
                )

                OutlinedButton(
                    colors = ButtonDefaults.outlinedButtonColors(),
                    shape = RoundedCornerShape(percent = 50),
                    onClick = { isLoading = false },
                ) {
                    Text(text = "Cancel loading", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}
