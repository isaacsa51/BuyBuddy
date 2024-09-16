package com.serranoie.android.buybuddy.ui.common

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.serranoie.android.buybuddy.data.persistance.entity.CategoryWithItemsEntity
import com.serranoie.android.buybuddy.ui.navigation.Route
import com.serranoie.android.buybuddy.ui.util.UiConstants.CONTENT_ANIMATION_DURATION
import com.serranoie.android.buybuddy.ui.util.UiConstants.basePadding
import com.serranoie.android.buybuddy.ui.util.UiConstants.largePadding
import com.serranoie.android.buybuddy.ui.util.UiConstants.smallPadding
import com.serranoie.android.buybuddy.ui.util.noRippleClickable
import com.serranoie.android.buybuddy.ui.util.weakHapticFeedback

@Composable
fun CategoryCard(
    categoryWithItems: CategoryWithItemsEntity,
    navController: NavController,
) {
    val view = LocalView.current
    var expanded by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(smallPadding)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = CONTENT_ANIMATION_DURATION,
                    easing = LinearOutSlowInEasing,
                ),
            )
            .clickable { expanded = !expanded },
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = categoryWithItems.category.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(
                    start = largePadding,
                    top = basePadding,
                    end = largePadding,
                    bottom = basePadding,
                ),
            )

            if (expanded) {
                categoryWithItems.items.forEach { item ->

                    Row(
                        modifier = Modifier
                            .noRippleClickable { }
                            .padding(
                                horizontal = largePadding,
                                vertical = basePadding,
                            )
                            .fillMaxWidth()
                            .clickable {
                                view.weakHapticFeedback()
                                navController.navigate(Route.Edit.editItemRoute(item.itemId!!))
                            },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (item.status) {
                            Icon(
                                modifier = Modifier
                                    .padding(start = smallPadding)
                                    .size(20.dp),
                                imageVector = Icons.Rounded.Check,
                                contentDescription = "Check icon",
                            )
                            Text(
                                text = item.name,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(horizontal = smallPadding),
                                style = MaterialTheme.typography.titleLarge,
                                textDecoration = TextDecoration.LineThrough,
                                fontStyle = FontStyle.Italic,
                            )
                        } else {
                            Icon(
                                modifier = Modifier
                                    .padding(start = smallPadding)
                                    .size(20.dp),
                                imageVector = Icons.Outlined.Circle,
                                contentDescription = "Icon",
                            )
                            Text(
                                text = item.name,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(horizontal = smallPadding),
                                style = MaterialTheme.typography.titleLarge,
                            )
                        }
                    }
                }
            }
        }
    }
}
